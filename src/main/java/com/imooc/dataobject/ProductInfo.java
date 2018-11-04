package com.imooc.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imooc.enums.ProductStatusEnum;
import com.imooc.utils.EnumUtil;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Kent
 * @date 2017-10-26.
 */
@Entity
@Data
@DynamicUpdate
public class ProductInfo implements Serializable {

    private static final long serialVersionUID = 3268350373126464031L;
    @Id
    //@GeneratedValue
    private String productId;

    private String  productName;
    private BigDecimal productPrice;

    /**
     * 库存
     */
    private Integer productStock;
    private String productDescription;
    private String productIcon;
    /**
     * 商品状态,0在架,1下架
     */
    private Integer productStatus = ProductStatusEnum.UP.getCode();
    private Integer categoryType;
    private Date createTime;
    private Date updateTime;

    @JsonIgnore
    public ProductStatusEnum getProductStatusEnum() {
        return EnumUtil.getByCode(this.productStatus,ProductStatusEnum.class);
    }

    public ProductInfo(String productName, BigDecimal productPrice, Integer productStock, String productDescription, String productIcon, Integer productStatus, Integer categoryType) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productDescription = productDescription;
        this.productIcon = productIcon;
        this.productStatus = productStatus;
        this.categoryType = categoryType;
    }

    public ProductInfo() {
    }
}
