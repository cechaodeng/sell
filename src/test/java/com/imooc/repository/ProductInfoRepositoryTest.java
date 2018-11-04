package com.imooc.repository;

import com.imooc.dataobject.ProductInfo;
import com.imooc.enums.ProductStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Kent
 * @date 2017-10-26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoRepositoryTest {

    @Autowired
    private ProductInfoRepository repository;

    @Test
    public void save() throws Exception {
        ProductInfo productInfo = new ProductInfo("红烧肉",new BigDecimal(15),10,"好吃","htt;://xxxx.com",0,4);
        productInfo.setProductId("123456");
        ProductInfo result = repository.save(productInfo);
        Assert.assertNotNull(result);

    }

    @Test
    public void findAll() throws Exception {
        List<ProductInfo> productInfoList = repository.findAll();
        Assert.assertNotEquals(0,productInfoList.size());
    }
    @Test
    public void findByProductStatus() throws Exception {
        List<ProductInfo> productInfoList = repository.findByProductStatus(ProductStatusEnum.UP.getCode());
        Assert.assertNotNull(productInfoList);
    }

}