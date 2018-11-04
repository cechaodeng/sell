package com.imooc.repository;

import com.imooc.dataobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Kent
 * @date 2017-10-26.
 */
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {//第一个参数表示目标类,第二个表示主键类型,那联合主键怎么办??

    /**
     * 根据状态查询商品
     * @param productStatus
     * @return
     */
    List<ProductInfo> findByProductStatus(Integer productStatus);

    //void increaseStock
}
