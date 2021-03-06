package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.*;
import com.atguigu.gmall.product.service.ManageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiale
 * @date 2022/1/11 - 17:01
 */
@Service
public class ManageServiceImpl implements ManageService {

    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;

    @Autowired
    private BaseCategory2Mapper baseCategory2Mapper;

    @Autowired
    private BaseCategory3Mapper baseCategory3Mapper;

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    private SpuInfoMapper spuInfoMapper;

    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    private SpuImageMapper spuImageMapper;

    @Autowired
    private SpuPosterMapper spuPosterMapper;

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SkuImageMapper skuImageMapper;

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    private BaseCategoryViewMapper baseCategoryViewMapper;

    @Override
    public List<BaseCategory1> getCategory1() {
        return baseCategory1Mapper.selectList(new QueryWrapper<>(null));
    }

    @Override
    public List<BaseCategory2> getCategory2(Long category1Id) {
        return baseCategory2Mapper.selectList(new QueryWrapper<BaseCategory2>().eq("category1_id",category1Id));
    }

    @Override
    public List<BaseCategory3> getCategory3(Long category2Id) {
        return baseCategory3Mapper.selectList(new QueryWrapper<BaseCategory3>().eq("category2_id",category2Id));
    }

    @Override
    public List<BaseAttrInfo> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        //  ???????????????????????????????????????????????????????????????????????????
        //  ????????????????????? ???????????????mapper.xml
        return baseAttrInfoMapper.selectAttrInfoList(category1Id,category2Id,category3Id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        if(baseAttrInfo.getId() != null) {
            //????????????
            baseAttrInfoMapper.updateById(baseAttrInfo);
        } else {
            //????????????
            baseAttrInfoMapper.insert(baseAttrInfo);
        }

        //???????????? 1???????????? 2 ????????????
        QueryWrapper queryWrapper = new QueryWrapper<BaseAttrValue>();
        queryWrapper.eq("attr_id",baseAttrInfo.getId());
        baseAttrValueMapper.delete(queryWrapper);

        //?????????????????????????????????????????????
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        //????????????
        if(!CollectionUtils.isEmpty(attrValueList)) {
            for (BaseAttrValue baseAttrValue : attrValueList) {
                //  base_attr_value ??????????????????????????? attr_id ????????? baseAttrInfo.id =  baseAttrValue.attr_id
                // ??????id???null, ?????????insert ????????????????????????????????? @TableId(type = IdType.AUTO) ?????????????????????
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insert(baseAttrValue);
            }
        }

    }

    @Override
    public List<BaseAttrValue> getAttrValueList(Long attrId) {
        return baseAttrValueMapper.selectList(new QueryWrapper<BaseAttrValue>().eq("attr_id",attrId));
    }

    @Override
    public BaseAttrInfo getBaseAttrInfo(Long attrId) {
        //  ????????????????????????
        BaseAttrInfo baseAttrInfo = this.baseAttrInfoMapper.selectById(attrId);
        if (baseAttrInfo!=null){
            //  ????????????????????????????????????
            baseAttrInfo.setAttrValueList(getAttrValueList(attrId));
        }
        return baseAttrInfo;
    }

    @Override
    public IPage getSpuList(Page<SpuInfo> spuInfoPage, SpuInfo spuInfo) {
        QueryWrapper<SpuInfo> spuInfoQueryWrapper = new QueryWrapper<>();
        spuInfoQueryWrapper.eq("category3_id",spuInfo.getCategory3Id());
        spuInfoQueryWrapper.orderByDesc("id");
        return spuInfoMapper.selectPage(spuInfoPage,spuInfoQueryWrapper);
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return baseSaleAttrMapper.selectList(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSpuInfo(SpuInfo spuInfo) {
        // ?????????spuInfo
        spuInfoMapper.insert(spuInfo);  //  ?????????????????????
        //  spuImage
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        //  ??????
        if (!CollectionUtils.isEmpty(spuImageList)){
            //  ???????????????
            spuImageList.stream().forEach(spuImage -> {
                //  ???????????????
                //  ??????spuId  spuId ?????????
                Long spuId = spuInfo.getId();
                spuImage.setSpuId(spuId);
                spuImageMapper.insert(spuImage);
            });
        }

        //  spuPoster
        List<SpuPoster> spuPosterList = spuInfo.getSpuPosterList();
        if (!CollectionUtils.isEmpty(spuPosterList)){
            spuPosterList.forEach(spuPoster -> {
                //  ???????????????
                spuPoster.setSpuId(spuInfo.getId());    //  ??????????????????
                spuPosterMapper.insert(spuPoster);
            });
        }

        //  ??????????????????
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if (!CollectionUtils.isEmpty(spuSaleAttrList)){
            spuSaleAttrList.forEach(spuSaleAttr -> {
                //  ???????????????
                spuSaleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insert(spuSaleAttr);

                //  ????????????????????????????????????????????????
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                if (!CollectionUtils.isEmpty(spuSaleAttrValueList)){
                    spuSaleAttrValueList.forEach(spuSaleAttrValue -> {
                        //  ?????????????????????????????????
                        spuSaleAttrValue.setSpuId(spuInfo.getId());
                        //  ???????????????
                        spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                        spuSaleAttrValueMapper.insert(spuSaleAttrValue);
                    });
                }
            });
        }
    }

    @Override
    public List<SpuImage> getSpuImageList(Long spuId) {
        return spuImageMapper.selectList(new QueryWrapper<SpuImage>().eq("spu_id",spuId));
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(Long spuId) {
        return this.spuSaleAttrMapper.selectSpuSaleAttrList(spuId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSkuInfo(SkuInfo skuInfo) {
        this.skuInfoMapper.insert(skuInfo);

        //skuImage
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if(!CollectionUtils.isEmpty(skuImageList)) {
            skuImageList.forEach(skuImage -> {
                skuImage.setSkuId(skuInfo.getId());
                skuImageMapper.insert(skuImage);
            });
        }

        //  skuAttrValue
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if (!CollectionUtils.isEmpty(skuAttrValueList)) {
            //  ????????????
            skuAttrValueList.forEach(skuAttrValue -> {
                //  ??????
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insert(skuAttrValue);
            });
        }

        //  skuSaleAttrValue
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if (!CollectionUtils.isEmpty(skuSaleAttrValueList)){
            skuSaleAttrValueList.forEach(skuSaleAttrValue -> {
                //  ?????????
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
                skuSaleAttrValueMapper.insert(skuSaleAttrValue);
            });
        }
    }

    @Override
    public IPage getSkuInfoList(Page<SkuInfo> skuInfoPage, SkuInfo skuInfo) {
        //  select * from sku_info where category3_id = ? and is_deleted = 0 order by id desc limit 0 ,10
        QueryWrapper<SkuInfo> skuInfoQueryWrapper = new QueryWrapper<>();
        skuInfoQueryWrapper.eq("category3_id",skuInfo.getCategory3Id());
        skuInfoQueryWrapper.orderByDesc("id");
        return skuInfoMapper.selectPage(skuInfoPage,skuInfoQueryWrapper);
    }

    @Override
    public void onSale(Long skuId) {
        //?????? is_sale = 1
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(1);
        this.skuInfoMapper.updateById(skuInfo);
    }

    @Override
    public void cancelSale(Long skuId) {
        //?????? is_sale = 0
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(0);
        this.skuInfoMapper.updateById(skuInfo);
    }

    @Override
    public SkuInfo getSkuInfo(Long skuId) {
        //??????skuInfo??????
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        //??????skuImage
        List<SkuImage> skuImageList = skuImageMapper.selectList(new QueryWrapper<SkuImage>().eq("sku_id", skuId));
        //??????
        skuInfo.setSkuImageList(skuImageList);
        return skuInfo;
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        QueryWrapper<SkuInfo> skuInfoQueryWrapper = new QueryWrapper<>();
        skuInfoQueryWrapper.select("price");
        skuInfoQueryWrapper.eq("id",skuId);
        SkuInfo skuInfo = skuInfoMapper.selectOne(skuInfoQueryWrapper);

        return skuInfo!=null?skuInfo.getPrice():new BigDecimal("0");
    }

    @Override
    public BaseCategoryView getCategoryViewByCategory3Id(Long category3Id) {
        return baseCategoryViewMapper.selectById(category3Id);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(skuId, spuId);
    }

    @Override
    public Map getSkuValueIdsMap(Long spuId) {
        //  ?????????Map ??????
        HashMap<Object, Object> map = new HashMap<>();
        //  map.put(key,value)  map.put("3724|3726","skuId");
        //  ????????????????????????????????????????????? skuId valueIds  ??????????????????map ?????????
        /*
            class Param{
                private Long skuId;
                private String valueIds;
            }
            List<Param> list = skuSaleAttrValueMapper.selectSkuValueIdsMap(spuId);
            map.put("skuId",21);    new Param().setSkuId(21);
            map.put("valueIds","3724|3726");    new Param().setValueIds("3724|3726");

            ??????????????? map ????????????class???
         */
        List<Map> mapList = skuSaleAttrValueMapper.selectSkuValueIdsMap(spuId);
        //  ????????????
        if (!CollectionUtils.isEmpty(mapList)){
            for (Map maps : mapList) {
                //  ????????????????????????map ????????????
                map.put(maps.get("value_ids"),maps.get("sku_id"));
            }
        }
        //  ????????????
        return map;
    }

    @Override
    public List<SpuPoster> findSpuPosterBySpuId(Long spuId) {
        QueryWrapper<SpuPoster> spuPosterQueryWrapper = new QueryWrapper<>();
        spuPosterQueryWrapper.eq("spu_id",spuId);
        List<SpuPoster> spuPosterList  = spuPosterMapper.selectList(spuPosterQueryWrapper);
        return spuPosterList ;
    }

    @Override
    public List<BaseAttrInfo> getAttrList(Long skuId) {
        return baseAttrInfoMapper.selectAttrList(skuId);
    }




}
