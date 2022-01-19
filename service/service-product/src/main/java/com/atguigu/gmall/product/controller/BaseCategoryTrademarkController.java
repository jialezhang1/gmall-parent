package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.CategoryTrademarkVo;
import com.atguigu.gmall.product.service.BaseCategoryTrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jiale
 * @date 2022/1/14 - 13:42
 */
@RestController
@RequestMapping("admin/product/baseCategoryTrademark")
public class BaseCategoryTrademarkController {

    @Autowired
    private BaseCategoryTrademarkService baseCategoryTrademarkService;

    //根据三级分类id查询品牌列表
    @GetMapping("findTrademarkList/{category3Id}")
    public Result findTrademarkList(@PathVariable Long category3Id){
        //  调用服务层方法 返回品牌列表集合
        List<BaseTrademark> baseTrademarkList = baseCategoryTrademarkService.findTrademarkList(category3Id);
        //  返回数据！
        return Result.ok(baseTrademarkList);
    }

    //获取可选品牌列表
    @GetMapping("findCurrentTrademarkList/{category3Id}")
    public Result findCurrentTrademarkList(@PathVariable Long category3Id){
        List<BaseTrademark> baseTrademarkList = baseCategoryTrademarkService.findCurrentTrademarkList(category3Id);
        return Result.ok(baseTrademarkList);
    }

    //保存分类品牌关联
    @PostMapping("save")
    public Result save(@RequestBody CategoryTrademarkVo categoryTrademarkVo){
        this.baseCategoryTrademarkService.save(categoryTrademarkVo);
        return Result.ok();
    }

    //根据分类Id 与品牌Id 删除数据
    @DeleteMapping("/remove/{category3Id}/{trademarkId}")
    public Result remove(@PathVariable Long category3Id,
                         @PathVariable Long trademarkId){
        this.baseCategoryTrademarkService.remove(category3Id,trademarkId);
        return Result.ok();
    }
}
