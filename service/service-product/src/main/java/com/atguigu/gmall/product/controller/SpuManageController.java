package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.service.ManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jiale
 * @date 2022/1/12 - 20:40
 */
@RestController // @ResponseBody + @Controller
@RequestMapping("admin/product")
public class SpuManageController {

    @Autowired
    private ManageService manageService;

    //根据分类id 查询spuInfo 集合数据
    @GetMapping("{page}/{limit}")
    public Result spuList(@PathVariable Long page,
                          @PathVariable Long limit,
                          SpuInfo spuInfo) {
        Page<SpuInfo> spuInfoPage = new Page<>(page,limit);
        //  调用服务层方法
        IPage iPage = manageService.getSpuList(spuInfoPage,spuInfo);

        //  返回数据
        return Result.ok(iPage);
    }

    //获取销售属性数据
    @GetMapping("baseSaleAttrList")
    public Result getBaseSaleAttrList(){
        List<BaseSaleAttr> baseSaleAttrList = this.manageService.getBaseSaleAttrList();
        return Result.ok(baseSaleAttrList);
    }

    //spu保存
    @PostMapping("saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo){
        this.manageService.saveSpuInfo(spuInfo);
        return Result.ok();
    }

    //根据spuid获取数据列表
    @GetMapping("spuImageList/{spuId}")
    public Result getSpuImageList(@PathVariable Long spuId) {
         List<SpuImage> spuImageList = this.manageService.getSpuImageList(spuId);
        return Result.ok(spuImageList);
    }

    //根据spuId 查询销售属性集合
    @GetMapping("spuSaleAttrList/{spuId}")
    public Result getSpuSaleAttrList(@PathVariable Long spuId) {
         List<SpuSaleAttr> spuSaleAttrList = this.manageService.getSpuSaleAttrList(spuId);
        return Result.ok(spuSaleAttrList);
    }

    @GetMapping("/list/{page}/{limit}")
    public Result getSkuInfoList(
            @PathVariable Long page,
            @PathVariable Long limit,
            SkuInfo skuInfo) {
        Page<SkuInfo> skuInfoPage = new Page<>(page, limit);
        IPage iPage = this.manageService.getSkuInfoList(skuInfoPage,skuInfo);
        return  Result.ok(iPage);
    }

    }
