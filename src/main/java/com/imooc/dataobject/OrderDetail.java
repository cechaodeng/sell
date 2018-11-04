package com.imooc.dataobject;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * @author Kent
 * @date 2017-10-27.
 */
@Entity
@Data
public class OrderDetail {

    @Id
    private String detailId;
    private String orderId;
    private String productId;
    private String productName;
    private BigDecimal productPrice;

    /**
     * 商品数量,表示一个订单中某件商品买了多少件
     */
    private Integer productQuantity;

    /**
     * 商品小图
     */
    private String productIcon;

    /**
     * 买家下单时候的构造方法
     * @param productId
     * @param productQuantity
     */
    public OrderDetail(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }

    public OrderDetail() {
    }
}
