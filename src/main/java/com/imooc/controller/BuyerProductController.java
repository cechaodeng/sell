package com.imooc.controller;

import com.imooc.VO.ProductInfoVO;
import com.imooc.VO.ProductVO;
import com.imooc.VO.ResultVO;
import com.imooc.dataobject.ProductCategory;
import com.imooc.dataobject.ProductInfo;
import com.imooc.service.CategoryService;
import com.imooc.service.ProductService;
import com.imooc.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kent
 * @date 2017-10-27.
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/list")
    //@Cacheable(cacheNames = "product",key = "123")
    public ResultVO list() {
        //1.查询所有上架商品
        List<ProductInfo> productInfoList = productService.findUpAll();
        //2.查询上面的商品的类目的名称
        //这里采用Java8的新特性,lambda表达式,获取上面所有商品的类型id
        List<Integer> categoryTypeList = productInfoList.stream().map(e -> e.getCategoryType()).collect(Collectors.toList());
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);
        //3.数据拼装返回
        //两层循环拼装数据
        List<ProductVO> productVOList = new ArrayList<ProductVO>();
        for (ProductCategory productCategory : productCategoryList) {
            //再遍历产品信息,构造出ProductVO对象
            //保存每个分类下面的商品列表
            List<ProductInfoVO> productInfoVOList = new ArrayList<ProductInfoVO>();
            for(ProductInfo productInfo : productInfoList) {
                //对所有查出来的商品循环,分别放到不同类型的list里面去
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    //传统方法
                    //ProductInfoVO productInfoVO = new ProductInfoVO(productInfo.getProductId(),productInfo.getProductName(),productInfo.getProductPrice(),productInfo.getProductDescription(),productInfo.getProductIcon());
                    //对象拷贝方法
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVOList.add(new ProductVO(productCategory.getCategoryName(),productCategory.getCategoryType(),productInfoVOList));
        }
        return ResultVOUtil.success(productVOList);

        /*
        //构造一个ProductInfoVO
        ProductInfoVO productInfoVO = new ProductInfoVO();
        productInfoVO.setProductId("productId123");
        productInfoVO.setProductDescription("descri好吃");
        productInfoVO.setProductIcon("http://dsddd.com");
        productInfoVO.setProductName("name猪脚饭");
        productInfoVO.setProductPrice(new BigDecimal(12));

        //构造一个ProductInfoVO的数组
        List<ProductInfoVO> productInfoVOList = new ArrayList<ProductInfoVO>();
        productInfoVOList.add(productInfoVO);

        //构造一个ProductVO
        ProductVO productVO = new ProductVO("热榜",1,productInfoVOList);

        //构造一个ProductVO的数组
        List<ProductVO> productVOList = new ArrayList<ProductVO>();
        productVOList.add(productVO);

        //构造一个RusultVO并返回
        return new ResultVO(0,"成功",productVOList);*/
    }
}
