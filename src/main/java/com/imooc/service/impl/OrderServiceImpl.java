package com.imooc.service.impl;

import com.imooc.converter.OrderMaster2OrderDTOConverter;
import com.imooc.dataobject.OrderDetail;
import com.imooc.dataobject.OrderMaster;
import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDTO;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.repository.OrderDetailRepository;
import com.imooc.repository.OrderMasterRepository;
import com.imooc.repository.ProductInfoRepository;
import com.imooc.service.OrderService;
import com.imooc.service.PayService;
import com.imooc.service.ProductService;
import com.imooc.service.WebSocket;
import com.imooc.utils.KeyUtil;
import com.imooc.utils.ResultVOUtil;
import freemarker.cache.OrMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kent
 * @date 2017-10-28.
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PayService payService;

    @Autowired
    private WebSocket webSocket;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        /**
         * 1.计算商品价格
         * 2.存入数据库
         * 3.减库存
         */
        //生成订单主键
        String orderId = KeyUtil.genUniqueKey();

        //买家订单总金额
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);

        //获取买家下单的商品的列表
        List<OrderDetail> orderDetailList = orderDTO.getOrderDetailList();
        for (OrderDetail orderDetail : orderDetailList) {
            Integer orderDetailProductQuantity = orderDetail.getProductQuantity();
            //得到每一个买家下单的商品,根据商品的id去找商品的价格以及判断库存
            ProductInfo productInfo = productInfoRepository.findOne(orderDetail.getProductId());
            //判断是否存在该商品
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIT);
            }
            /**
             * 判断库存
             * 我认为应该放在这里来做,如果库存不足的话就不需要继续往下进行了
             * 但是却在产品信息的service里面做的判断,也是有道理的
             * 我是订单服务,就应该只负责订单的处理,具体产品的情况,应该由产品服务去负责
             */
            /*if (productInfo.getProductStock() < orderDetailProductQuantity) {
                //库存不足
                return null;//ResultVOUtil.error();
            }*/

            //买家订单中的某一项商品的总金额
            BigDecimal orderDetailAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetailProductQuantity));
            //将这一项商品添加到订单的总金额
            orderAmount = orderAmount.add(orderDetailAmount);

            //将买家订单的一件商品的详情入库
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            //对象拷贝
            BeanUtils.copyProperties(productInfo,orderDetail);
            /*orderDetail.setProductPrice(productInfo.getProductPrice());
            orderDetail.setProductName(productInfo.getProductName());
            orderDetail.setProductIcon(productInfo.getProductIcon());*/

            //保存订单详情
            orderDetailRepository.save(orderDetail);
        }

        //将订单信息存入数据库
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);

        //减库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().
                map(e -> new CartDTO(e.getProductId(),e.getProductQuantity())).
                collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);

        webSocket.sendMessage("有新的订单,订单号为:" + orderDTO.getOrderId());

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        /**
         * 1.判断订单状态,是否能被取消
         * 2.取消订单,也就是更新订单状态
         * 3.返还库存
         * 4.如果已经支付,退款
         */
        //1.判断订单状态,是否能被取消
        OrderMaster orderMaster = orderMasterRepository.findOne(orderDTO.getOrderId());
        if (!orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("[取消订单]订单状态不正确,orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //2.取消订单,也就是更新订单状态
        orderMaster.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        //BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("[取消订单]更新失败,orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        //3.返还库存,根据商品详情返回,买了什么,还什么
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderMaster.getOrderId());
        if (orderDetailList == null) {
            log.error("[取消订单]订单中无商品详情,orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e -> new CartDTO(e.getProductId(),e.getProductQuantity())).collect(Collectors.toList());
        //加库存
        productService.increaseStock(cartDTOList);

        //4.如果已经支付,退款
        if (orderMaster.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            log.info("退款成功");
            //payService.refund(orderDTO);
        }

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid,pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        return new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        OrderMaster orderMaster = orderMasterRepository.findOne(orderDTO.getOrderId());
        if (!orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("[完结订单]订单状态错误,orderId={},orderState={}",orderMaster.getOrderId(),orderMaster.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("[完结订单]更新失败,orderMaser={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        /**
         * 1.判断订单状态
         * 2.修改订单状态
         */
        OrderMaster orderMaster = orderMasterRepository.findOne(orderDTO.getOrderId());
        if (!orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            //订单状态不正确
            log.error("[支付订单]订单状态不正确,orderMasterId={},orderStatus={}",orderMaster.getOrderId(),orderMaster.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        if (!orderMaster.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            //支付状态不正确
            log.error("[支付订单]支付状态不正确,orderId={},payStatus={}",orderMaster.getOrderId(),orderMaster.getPayStatus());
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        //2.修改订单状态
        orderMaster.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("[支付订单]更新失败,orderMaser={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
        if (orderMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (orderDetailList == null) {
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        return new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());
    }
}
