package com.imooc.repository;

import com.imooc.dataobject.OrderMaster;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;


/**
 * @author Kent
 * @date 2017-10-28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    private final static String OPENID = "abc123";

    @Autowired
    private  OrderMasterRepository repository;

    @Test
    public void save() throws Exception {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("3");
        orderMaster.setBuyerName("封姗");
        orderMaster.setBuyerAddress("厦滘");
        orderMaster.setBuyerOpenid(OPENID);
        orderMaster.setBuyerPhone("13524577676");
        orderMaster.setOrderAmount(new BigDecimal(5));
        //有默认值
        //orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        //orderMaster.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster result = repository.save(orderMaster);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByBuyerOpenid() throws Exception {
        //repository.findByBuyerOpenid()
        PageRequest request = new PageRequest(0,4);
        Page<OrderMaster> result = repository.findByBuyerOpenid(OPENID,request);
        Assert.assertNotNull(result);
    }

}