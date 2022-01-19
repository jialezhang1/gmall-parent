package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author jiale
 * @date 2022/1/12 - 21:07
 */
public interface BaseTrademarkService extends IService<BaseTrademark> {

    /**
     * com.atguigu.pojo bean entity domain
     * 查询品牌列表
     * @param baseTrademarkPage
     * @return
     */
    IPage getTradeMarkList(Page<BaseTrademark> baseTrademarkPage);
}
