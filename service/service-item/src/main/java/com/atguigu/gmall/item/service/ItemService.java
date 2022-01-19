package com.atguigu.gmall.item.service;

import java.util.Map;

/**
 * @author jiale
 * @date 2022/1/16 - 17:47
 */
public interface ItemService {

    /**
     * 获取sku详情信息
     * @param skuId
     * @return
     */
    Map<String, Object> getItem(Long skuId);

}
