package com.atguigu.gmall.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jiale
 * @date 2022/1/16 - 17:52
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public Map<String, Object> getItem(Long skuId) {
        Map<String ,Object> map = new HashMap<>();
        //  获取skuInfo 数据
        SkuInfo skuInfo = this.productFeignClient.getSkuInfo(skuId);
        //  获取分类数据
        BaseCategoryView categoryView = this.productFeignClient.getCategoryView(skuInfo.getCategory3Id());
        //  获取价格
        BigDecimal price = this.productFeignClient.getSkuPrice(skuId);
        //  销售属性回显
        List<SpuSaleAttr> spuSaleAttrList = this.productFeignClient.getSpuSaleAttrListCheckBySku(skuId, skuInfo.getSpuId());
        //  获取切换的json 字符串
        Map idsMap = this.productFeignClient.getSkuValueIdsMap(skuInfo.getSpuId());
        //  将map 转换为json 字符串
        String strJson = JSON.toJSONString(idsMap);
        System.out.println("strJson:\t"+strJson);

        //  获取海报信息
        List<SpuPoster> spuPosterList = this.productFeignClient.getSpuPosterBySpuId(skuInfo.getSpuId());

        //  平台属性：特殊待遇.....
        List<BaseAttrInfo> attrList = this.productFeignClient.getAttrList(skuId);
        //  页面显示的时候，需要的attrName：平台属性名 ,attrValue：平台属性值的名称！
        List<HashMap<String, Object>> skuAttrList = attrList.stream().map(baseAttrInfo -> {
            HashMap<String, Object> mp = new HashMap<>();
            mp.put("attrName", baseAttrInfo.getAttrName());
            //  请问为什么是 0 ? 1 2 3呢?
            mp.put("attrValue", baseAttrInfo.getAttrValueList().get(0).getValueName());
            return mp;
        }).collect(Collectors.toList());

        //  map 的key 应该是谁? 页面存储的可以 ${skuInfo.skuName}
        map.put("skuInfo",skuInfo);
        map.put("categoryView",categoryView);
        map.put("price",price);
        map.put("spuSaleAttrList",spuSaleAttrList);
        map.put("skuAttrList",skuAttrList);
        map.put("valuesSkuJson",strJson);
        map.put("spuPosterList",spuPosterList);
        //  返回数据
        return map;
    }
}
