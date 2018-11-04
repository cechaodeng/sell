package com.imooc.controller;

import com.imooc.dataobject.ProductCategory;
import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.ProductStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.form.ProductFrom;
import com.imooc.service.CategoryService;
import com.imooc.service.ProductService;
import com.imooc.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author Kent
 * @date 2017-11-07.
 */
@Controller
@RequestMapping("/seller/product")
@Slf4j
public class SellProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "5") Integer size,
                             Map<String, Object> map) {
        PageRequest request = new PageRequest(page - 1, size);
        Page<ProductInfo> productInfoPage = productService.findAll(request);
        map.put("productInfoPage", productInfoPage);
        map.put("currentPage", page);
        //productInfoPage.getTotalPages().getSize()getContent()

        //orderDTOPage.getSize()
        return new ModelAndView("product/list2", map);
    }


    @GetMapping("off_sale")
    public ModelAndView offSale(@RequestParam("productId") String productId,
                                Map<String, Object> map) {
        try {
            productService.offSale(productId);
        } catch (SellException se) {
            log.error("[产品下架]错误productId={}", productId);
            map.put("msg", se.getMessage());
            map.put("url", "/sell/seller/product/list2?productId=" + productId);
            return new ModelAndView("/common/error", map);
        }

        map.put("msg", ResultEnum.PRODUCT_OFF_SALE_SUCCESS.getMessage());
        map.put("url", "/sell/seller/product/list?productId=" + productId);
        return new ModelAndView("/common/success", map);
    }

    @GetMapping("on_sale")
    public ModelAndView onSale(@RequestParam("productId") String productId,
                               Map<String, Object> map) {
        try {
            productService.onSale(productId);
        } catch (SellException se) {
            log.error("[产品上架]错误productId={}", productId);
            map.put("msg", se.getMessage());
            map.put("url", "/sell/seller/product/list?productId=" + productId);
            return new ModelAndView("/common/error", map);
        }

        map.put("msg", ResultEnum.PRODUCT_ON_SALE_SUCCESS.getMessage());
        map.put("url", "/sell/seller/product/list?productId=" + productId);
        return new ModelAndView("/common/success", map);
    }

    /**
     * 新增或修改页面
     * @param productId
     * @param map
     * @return
     */
    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "productId", required = false) String productId,
                              Map<String, Object> map) {
        List<ProductCategory> productCategoryList = categoryService.findAll();
        map.put("productCategoryList", productCategoryList);

        if (productId == null) {
            //新增页面
            return new ModelAndView("/product/index", map);
        }

        //修改
        try {
            ProductInfo productInfo = productService.findOne(productId);
            map.put("productInfo", productInfo);
        } catch (SellException se) {
            log.error("[修改订单]订单不存在");
            map.put("msg", se.getMessage());
            map.put("url", "/sell/seller/product/list");
            return new ModelAndView("/common/error", map);
        }

        return new ModelAndView("/product/index", map);
    }

    @PostMapping("/save")
    //@CachePut(cacheNames = "product",key = "123") 返回的是ModelAndView无法序列化,所以不能用
    //@CacheEvict(cacheNames = "product",key = "123")//每次执行当前方法都会将缓存删除
    public ModelAndView save(@Valid ProductFrom from,
                             BindingResult bindingResult,
                             Map<String, Object> map) {
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/sell/seller/product/index");
            return new ModelAndView("/common/error", map);
        }

        String productId = from.getProductId();
        if (StringUtils.isEmpty(productId)) {
            //新增
            ProductInfo productInfo = new ProductInfo();
            BeanUtils.copyProperties(from, productInfo);
            productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
            productInfo.setProductId(KeyUtil.genUniqueKey());
            try {
                productService.save(productInfo);
                map.put("msg", ResultEnum.PRODUCT_SAVE_SUCCESS.getMessage());
                map.put("url", "/sell/seller/product/list");
                return new ModelAndView("/common/success", map);
            } catch (SellException se) {
                log.error("[新增商品]保存失败,productInfo={}", productInfo);
                map.put("msg", se.getMessage());
                map.put("url", "/sell/seller/product/index");
                return new ModelAndView("/common/error", map);
            }
        }

        //修改
        ProductInfo productInfo = productService.findOne(from.getProductId());
        BeanUtils.copyProperties(from, productInfo);
        try {
            productService.save(productInfo);
        } catch (SellException se) {
            log.error("[修改商品]保存失败,productInfo={}", productInfo);
            map.put("msg", se.getMessage());
            map.put("url", "/sell/seller/product/index?productId=" + productId);
            return new ModelAndView("/common/error", map);
        }

        //修改成功
        map.put("msg", ResultEnum.PRODUCT_UPDATE_SUCCESS.getMessage());
        map.put("url", "/sell/seller/product/list?productId=" + productId);
        return new ModelAndView("/common/success", map);
    }
}


