package com.imooc.service;

import com.imooc.dto.OrderDTO;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

/**
 * @author Kent
 * @date 2017-10-28.
 */
public interface OrderService {
    /**
     * 创建订单
     */
    OrderDTO create(OrderDTO orderDTO);

    /**
     * 取消订单
     */
    OrderDTO cancel(OrderDTO orderDTO);

    /**
     * 查询订单
     */
    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);

    /**
     * 完结订单
     */
    OrderDTO finish(OrderDTO orderDTO);

    /**
     * 支付订单
     */
    OrderDTO paid(OrderDTO orderDTO);

    /**
     * 查询单个订单
     */
    OrderDTO findOne(String orderId);

    /**
     * 查询所有人的订单的列表
     * @param pageable
     * @return
     */
    Page<OrderDTO> findList(Pageable pageable);


}
