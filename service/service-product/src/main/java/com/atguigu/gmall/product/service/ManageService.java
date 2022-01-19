package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author jiale
 * @date 2022/1/11 - 16:45
 */

public interface ManageService {

    /**
     * 查询所有一级分类数据
     * @return
     */
    List<BaseCategory1> getCategory1();

    /**
     * 根据一级分类Id 查询二级分类数据
     * @param category1Id
     * @return
     */
    List<BaseCategory2> getCategory2(Long category1Id);

    /**
     * 根据二级分类id，获取三级分类数据。
     * @param category2Id
     * @return
     */
    List<BaseCategory3> getCategory3(Long category2Id);

    /**
     * 根据分类id来获取平台属性数据
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    List<BaseAttrInfo> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id);

    /**
     * 保存平台属性
     * @param baseAttrInfo
     */
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    /**
     *
     * @param attrId
     * @return
     */
    List<BaseAttrValue> getAttrValueList(Long attrId);

    /**
     * 根据属性id查询属性数据
     * @param attrId
     * @return
     */
    BaseAttrInfo getBaseAttrInfo(Long attrId);

    /**
     * 根据分类id 查询spuInfo 集合数据
     * @param spuInfoPage
     * @param spuInfo
     * @return
     */
    IPage getSpuList(Page<SpuInfo> spuInfoPage, SpuInfo spuInfo);

    /**
     * 获取销售属性数据
     * @return
     */
    List<BaseSaleAttr> getBaseSaleAttrList();

    /**
     * spu保存
     * @param spuInfo
     */
    void saveSpuInfo(SpuInfo spuInfo);

    /**
     * 根据spuid获取数据列表
     * @param spuId
     * @return
     */
    List<SpuImage> getSpuImageList(Long spuId);

    /**
     * 根据spuId 查询销售属性集合
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrList(Long spuId);

    /**
     * 保存saveSkuInfo
     * @param skuInfo
     */
    void saveSkuInfo(SkuInfo skuInfo);

    /**
     * 查询skuInfo 分页列表
     * @param skuInfoPage
     * @param skuInfo
     * @return
     */
    IPage getSkuInfoList(Page<SkuInfo> skuInfoPage, SkuInfo skuInfo);

    /**
     * 商品上架
     * @param skuId
     */
    void onSale(Long skuId);

    /**
     * 商品下架
     * @param skuId
     */
    void cancelSale(Long skuId);

    /**
     * 根据skuId获取skuInfo和skuImageList
     * @param skuId
     * @return
     */
    SkuInfo getSkuInfo(Long skuId);

    /**
     * 查询价格
     * @param skuId
     * @return
     */
    BigDecimal getSkuPrice(Long skuId);

    /**
     * 通过三级分类id查询分类信息
     * @param category3Id
     * @return
     */
    BaseCategoryView getCategoryViewByCategory3Id(Long category3Id);

    /**
     * 根据spuId，skuId 查询销售属性集合
     * @param skuId
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId);

    /**
     * 根据spuId 查询map 集合属性
     * @param spuId
     * @return
     */
    Map getSkuValueIdsMap(Long spuId);

    /**
     * 根据spuId 获取海报数据
     * @param spuId
     * @return
     */
    List<SpuPoster> findSpuPosterBySpuId(Long spuId);

    /**
     * 通过skuId 集合来查询数据
     * @param skuId
     * @return
     */
    List<BaseAttrInfo> getAttrList(Long skuId);
}
