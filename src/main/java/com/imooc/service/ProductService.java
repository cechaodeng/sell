package com.imooc.service;

import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Kent
 * @date 2017-10-26.
 */
public interface ProductService {
    /**
     * 查看所有上架的商品
     * @return
     */
    List<ProductInfo> findUpAll();

    /**
     * 分页查看所有商品,管理员用
     * 返回一个页面,注意返回类型
     * @return
     */
    Page<ProductInfo> findAll(Pageable pageable);

    /**
     *
     * @param productId
     * @return
     */
    ProductInfo findOne(String productId);

    ProductInfo save(ProductInfo productInfo);

    //ProductInfo findByCategory();

    /**
     * 加库存
     */
    void increaseStock(List<CartDTO> cartDTOList);
    /**
     * 减库存
     */
    void decreaseStock(List<CartDTO> cartDTOList);

    ProductInfo offSale(String productId);

    ProductInfo onSale(String productId);
}
