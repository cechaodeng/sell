package com.imooc.repository;

import com.imooc.dataobject.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.Order;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Kent
 * @date 2017-10-28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository repository;

    @Test
    public void save() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId("2");
        orderDetail.setDetailId("2");
        orderDetail.setProductIcon("http://dddd.com");
        orderDetail.setProductId("1");
        orderDetail.setProductName("封姗傻子");
        orderDetail.setProductPrice(new BigDecimal(111));
        orderDetail.setProductQuantity(new Integer(2));
        OrderDetail result = repository.save(orderDetail);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByOrderId() throws Exception {
        List<OrderDetail> orderDetailList = repository.findByOrderId("1");
        Assert.assertNotNull(orderDetailList);
    }

}