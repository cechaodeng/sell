package com.imooc.form;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Kent
 * @date 2017-11-08.
 */
@Data
public class ProductFrom {
    private String productId;

    private String  productName;
    private BigDecimal productPrice;

    /**
     * 库存
     */
    private Integer productStock;
    private String productDescription;
    private String productIcon;
    private Integer categoryType;
}
