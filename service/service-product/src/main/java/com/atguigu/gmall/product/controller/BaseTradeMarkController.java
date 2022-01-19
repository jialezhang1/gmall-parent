package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author jiale
 * @date 2022/1/12 - 21:05
 */
@RestController
@RequestMapping("/admin/product/baseTrademark")
public class BaseTradeMarkController {

    @Autowired
    private BaseTrademarkService baseTrademarkService;

    //分页列表
    @GetMapping("{page}/{limit}")
    public Result getTradeMarkList(@PathVariable Long page ,
                                   @PathVariable Long limit){
        Page<BaseTrademark> baseTrademarkPage = new Page<>();
        IPage iPag = baseTrademarkService.getTradeMarkList(baseTrademarkPage);
        return Result.ok(iPag);
    }


    //保存数据
    @PostMapping("save")
    public Result save(@RequestBody BaseTrademark baseTrademark) {
        this.baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    //更新数据
    @PutMapping("update")
    public Result update(@RequestBody BaseTrademark baseTrademark){
        //  调用服务层方法
        this.baseTrademarkService.updateById(baseTrademark);
        //  返回数据
        return Result.ok();
    }

    //删除
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        //  调用服务层方法
        this.baseTrademarkService.removeById(id);
        //  返回数据
        return Result.ok();
    }

    //回显
    @GetMapping("get/{id}")
    public Result getById(@PathVariable Long id){
        //  调用服务层方法
        BaseTrademark baseTrademark = this.baseTrademarkService.getById(id);
        //  返回数据
        return Result.ok(baseTrademark);
    }

}
