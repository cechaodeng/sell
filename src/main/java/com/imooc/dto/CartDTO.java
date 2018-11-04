package com.imooc.dto;

import lombok.Data;

/**
 * @author Kent
 * @date 2017-10-28.
 */
@Data
public class CartDTO {
    private String productId;

    /**
     * 商品数量
     */
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
