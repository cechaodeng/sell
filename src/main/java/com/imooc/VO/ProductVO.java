package com.imooc.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Kent
 * @date 2017-10-27.
 */
@Data
public class ProductVO implements Serializable {

    private static final long serialVersionUID = -6222592342784659291L;
    @JsonProperty("name")
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;

    public ProductVO() {
    }


    public ProductVO(String categoryName, Integer categoryType, List<ProductInfoVO> productInfoVOList) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
        this.productInfoVOList = productInfoVOList;
    }
}
