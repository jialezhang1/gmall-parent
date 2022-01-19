package com.atguigu.gmall.item.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author jiale
 * @date 2022/1/16 - 17:57
 */
@RestController
@RequestMapping("api/item")
public class ItemApiController {

    @Autowired
    private ItemService itemService;

    //根据skuId获取sku详情
    @GetMapping("{skuId}")
    public Result getItem(@PathVariable Long skuId) {
        Map<String, Object> map = itemService.getItem(skuId);
        return Result.ok(map);
    }
}
