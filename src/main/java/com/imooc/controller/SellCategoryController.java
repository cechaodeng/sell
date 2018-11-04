package com.imooc.controller;

import com.imooc.dataobject.ProductCategory;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.form.CategoryForm;
import com.imooc.service.CategoryService;
import com.imooc.service.ProductService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author Kent
 * @date 2017-11-08.
 */
@Controller
@RequestMapping("/seller/category")
@Slf4j
public class SellCategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ModelAndView list(Map<String, Object> map) {
        List<ProductCategory> productCategoryList = categoryService.findAll();
        map.put("productCategoryList", productCategoryList);
        return new ModelAndView("category/list2",map);
    }

    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "categoryId",required = false) Integer categoryId,
                              Map<String, Object> map) {
        if (categoryId == null) {
            //新增
            return new ModelAndView("category/index");
        }

        //去更新页面更新
        try {
            ProductCategory productCategory = categoryService.findOne(categoryId);
            map.put("productCategory", productCategory);
        } catch (SellException se) {
            log.error("[卖家端修改分类]发生异常categoryId={}",categoryId);

            map.put("msg", se.getMessage());
            map.put("url", "/sell/seller/category/list");
            return new ModelAndView("/common/error", map);
        }
        return new ModelAndView("category/index", map);
    }

    /**
     * 更新和新增类目
     * @param form
     * @param bindingResult
     * @param map
     * @return
     */
    @PostMapping("/save")
    public ModelAndView save(@Valid CategoryForm form,
                             BindingResult bindingResult,
                             Map<String, Object> map) {
        if (bindingResult.hasErrors()) {
            //如果校验不通过
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/sell/seller/category/index");
            return new ModelAndView("/common/error", map);
        }

        //根据categoryId来判断是新增还是更新
        if (form.getCategoryId() == null) {
            //新增
            ProductCategory productCategory = new ProductCategory();
            try {
                BeanUtils.copyProperties(form, productCategory);
                categoryService.save(productCategory);
            } catch (SellException se) {
                log.error("[新增类目]保存失败,productCategory={}", productCategory);
                map.put("msg", se.getMessage());
                map.put("url", "/sell/seller/category/list");
                return new ModelAndView("/common/error", map);
            }

            map.put("msg", ResultEnum.CATEGORY_SAVE_SUCCESS.getMessage());
            map.put("url", "/sell/seller/category/list");
            return new ModelAndView("/common/success", map);
        }

        //更新
        try {
            ProductCategory productCategory = categoryService.findOne(form.getCategoryId());
            BeanUtils.copyProperties(form, productCategory);
            categoryService.save(productCategory);

        } catch (SellException se) {
            log.error("[更新类目]保存失败,categoryId={}", form.getCategoryId());
            map.put("msg", se.getMessage());
            map.put("url", "/sell/seller/category/list");
            return new ModelAndView("/common/error", map);
        }

        map.put("msg", ResultEnum.CATEGORY_UPDATE_SUCCESS.getMessage());
        map.put("url", "/sell/seller/category/list");
        return new ModelAndView("/common/success", map);

    }
}
