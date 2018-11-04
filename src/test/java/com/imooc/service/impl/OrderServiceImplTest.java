package com.imooc.service.impl;

import com.imooc.dataobject.OrderDetail;
import com.imooc.dataobject.OrderMaster;
import com.imooc.dto.CartDTO;
import com.imooc.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Kent
 * @date 2017-10-28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    private final String BUYER_OPENID = "110110";

    private final String ORDER_ID = "1509185817571656639";

    @Test
    public void create() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        //买家信息
        orderDTO.setBuyerName("封姗");
        orderDTO.setBuyerAddress("厦滘");
        orderDTO.setBuyerPhone("13654357862");
        orderDTO.setBuyerOpenid(BUYER_OPENID);

        //订单详情
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        orderDetailList.add(new OrderDetail("123",1));
        orderDetailList.add(new OrderDetail("123456",1));

        orderDTO.setOrderDetailList(orderDetailList);
        OrderDTO result = orderService.create(orderDTO);
        log.info("[创建订单] result = {}",result);
        Assert.assertNotNull(result);
    }

    @Test
    public void cancel() throws Exception {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        orderService.cancel(orderDTO);
    }

    @Test
    public void findList() throws Exception {
        PageRequest request = new PageRequest(0,2);
        Page<OrderDTO> orderDTOPage = orderService.findList(BUYER_OPENID,request);
        //Assert.assertNotEquals(0,orderDTOPage.getTotalElements());
        Assert.assertTrue("查询所有订单列表",orderDTOPage.getTotalElements() > 0);
    }

    @Test
    public void findListTest() throws Exception {
        PageRequest request = new PageRequest(0,2);
        Page<OrderDTO> orderDTOPage = orderService.findList(request);
        Assert.assertNotNull(orderDTOPage);
    }

    @Test
    public void finish() throws Exception {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        orderService.finish(orderDTO);
    }

    @Test
    public void paid() throws Exception {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        orderService.paid(orderDTO);
    }

    @Test
    public void findOne() throws Exception {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        //System.out.println(orderDTO);
        Assert.assertNotNull(orderDTO);
    }



}