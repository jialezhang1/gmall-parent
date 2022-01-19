package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.service.ManageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jiale
 * @date 2022/1/11 - 16:39
 */
@Api(tags = "商品基础属性接口")
@RestController
@RequestMapping("admin/product")
@RefreshScope
public class BaseManageController{

    @Autowired
    private ManageService manageService;

    //查询所有一级分类信息
    @GetMapping("getCategory1")
    public Result getCategory1(){
        List<BaseCategory1> baseCategory1List = manageService.getCategory1();
        return Result.ok(baseCategory1List);
    }

    //根据一级分类id，获取二级分类数据。
    @GetMapping("getCategory2/{category1Id}")
    public Result getCategory2(@PathVariable Long category1Id) {
        List<BaseCategory2> baseCategory2List = manageService.getCategory2(category1Id);
        return Result.ok(baseCategory2List);
    }

    //根据二级分类id，获取三级分类数据。
    @GetMapping("getCategory3/{category2Id}")
    public Result getCategory3(@PathVariable Long category2Id) {
        List<BaseCategory3> baseCategory3List = manageService.getCategory3(category2Id);
        return Result.ok(baseCategory3List);
    }

    //根据分类id来获取平台属性数据
    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result attrInfoList(@PathVariable Long category1Id,
                               @PathVariable Long category2Id,
                               @PathVariable Long category3Id){
        //根据分类id查询数据
        List<BaseAttrInfo> baseAttrInfoList = this.manageService.getAttrInfoList(category1Id,category2Id,category3Id);
        return Result.ok(baseAttrInfoList);
    }

    //保存平台属性
    @PostMapping("saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo) {
        if(baseAttrInfo != null) {
            // 前台数据都被封装到该对象中baseAttrInfo
            this.manageService.saveAttrInfo(baseAttrInfo);
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //根据平台属性Id 获取到平台属性值集合(回显)
    @GetMapping("getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable Long attrId){
        //  从业务完整性的角度讲：
        BaseAttrInfo baseAttrInfo =  this.manageService.getBaseAttrInfo(attrId);
        //  List<BaseAttrValue> baseAttrValueList = this.manageService.getAttrValueList(attrId);
        return Result.ok(baseAttrInfo.getAttrValueList());
    }


}
