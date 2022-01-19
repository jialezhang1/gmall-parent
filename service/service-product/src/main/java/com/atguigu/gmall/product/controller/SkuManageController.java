package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author jiale
 * @date 2022/1/15 - 21:10
 */
@RestController
@RequestMapping("/admin/product/")
public class SkuManageController {

    @Autowired
    private ManageService manageService;

    //保存saveSkuInfo
    @PostMapping("saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo){
        this.manageService.saveSkuInfo(skuInfo);
        return Result.ok();
    }

    //  上架
    @GetMapping("onSale/{skuId}")
    public Result onSale(@PathVariable Long skuId){
        //  调用服务层方法
        this.manageService.onSale(skuId);
        //  返回数据
        return Result.ok();
    }

    //  下架
    @GetMapping("cancelSale/{skuId}")
    public Result cancelSale(@PathVariable Long skuId){
        //  调用服务层方法
        this.manageService.cancelSale(skuId);
        //  返回数据
        return Result.ok();
    }

    }
