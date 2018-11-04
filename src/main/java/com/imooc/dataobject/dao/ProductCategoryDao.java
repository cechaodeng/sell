package com.imooc.dataobject.dao;

import com.imooc.dataobject.ProductCategory;
import com.imooc.dataobject.mapper.ProductCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author Kent
 * @date 2017-11-15.
 */
public class ProductCategoryDao {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    public int insertByMap(Map<String, Object> map) {
        return productCategoryMapper.insertByMap(map);
    }
}
