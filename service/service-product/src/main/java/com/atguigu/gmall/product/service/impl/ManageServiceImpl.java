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
        //  查询平台属性的时候，可以顺便把平台属性值查询出来！
        //  多表关联查询： 必然要使用mapper.xml
        return baseAttrInfoMapper.selectAttrInfoList(category1Id,category2Id,category3Id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        if(baseAttrInfo.getId() != null) {
            //修改数据
            baseAttrInfoMapper.updateById(baseAttrInfo);
        } else {
            //添加数据
            baseAttrInfoMapper.insert(baseAttrInfo);
        }

        //修改数据 1删除数据 2 添加数据
        QueryWrapper queryWrapper = new QueryWrapper<BaseAttrValue>();
        queryWrapper.eq("attr_id",baseAttrInfo.getId());
        baseAttrValueMapper.delete(queryWrapper);

        //获取页面传递过来的所有平台属性
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        //循环遍历
        if(!CollectionUtils.isEmpty(attrValueList)) {
            for (BaseAttrValue baseAttrValue : attrValueList) {
                //  base_attr_value 页面传递数据的时候 attr_id 为空。 baseAttrInfo.id =  baseAttrValue.attr_id
                // 保存id为null, 但是，insert 方法已经在前面执行了！ @TableId(type = IdType.AUTO) 获取主键自增！
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
        //  获取平台属性对象
        BaseAttrInfo baseAttrInfo = this.baseAttrInfoMapper.selectById(attrId);
        if (baseAttrInfo!=null){
            //  赋值平台属性值集合数据！
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
        // 先保存spuInfo
        spuInfoMapper.insert(spuInfo);  //  获取主键自增！
        //  spuImage
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        //  判断
        if (!CollectionUtils.isEmpty(spuImageList)){
            //  插入数据！
            spuImageList.stream().forEach(spuImage -> {
                //  插入数据！
                //  赋值spuId  spuId 在哪？
                Long spuId = spuInfo.getId();
                spuImage.setSpuId(spuId);
                spuImageMapper.insert(spuImage);
            });
        }

        //  spuPoster
        List<SpuPoster> spuPosterList = spuInfo.getSpuPosterList();
        if (!CollectionUtils.isEmpty(spuPosterList)){
            spuPosterList.forEach(spuPoster -> {
                //  插入数据：
                spuPoster.setSpuId(spuInfo.getId());    //  好像是：空！
                spuPosterMapper.insert(spuPoster);
            });
        }

        //  获取销售属性
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if (!CollectionUtils.isEmpty(spuSaleAttrList)){
            spuSaleAttrList.forEach(spuSaleAttr -> {
                //  数据补全！
                spuSaleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insert(spuSaleAttr);

                //  获取到销售属性对应的销售属性值！
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                if (!CollectionUtils.isEmpty(spuSaleAttrValueList)){
                    spuSaleAttrValueList.forEach(spuSaleAttrValue -> {
                        //  看的字段是否全部有值！
                        spuSaleAttrValue.setSpuId(spuInfo.getId());
                        //  销售属性名
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
            //  循环插入
            skuAttrValueList.forEach(skuAttrValue -> {
                //  赋值
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insert(skuAttrValue);
            });
        }

        //  skuSaleAttrValue
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if (!CollectionUtils.isEmpty(skuSaleAttrValueList)){
            skuSaleAttrValueList.forEach(skuSaleAttrValue -> {
                //  赋值：
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
        //本质 is_sale = 1
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(1);
        this.skuInfoMapper.updateById(skuInfo);
    }

    @Override
    public void cancelSale(Long skuId) {
        //本质 is_sale = 0
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(0);
        this.skuInfoMapper.updateById(skuInfo);
    }

    @Override
    public SkuInfo getSkuInfo(Long skuId) {
        //获取skuInfo信息
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        //获取skuImage
        List<SkuImage> skuImageList = skuImageMapper.selectList(new QueryWrapper<SkuImage>().eq("sku_id", skuId));
        //赋值
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
        //  声明个Map 集合
        HashMap<Object, Object> map = new HashMap<>();
        //  map.put(key,value)  map.put("3724|3726","skuId");
        //  第一种方案：自定义一个实体类： skuId valueIds  第二种方案：map 接收！
        /*
            class Param{
                private Long skuId;
                private String valueIds;
            }
            List<Param> list = skuSaleAttrValueMapper.selectSkuValueIdsMap(spuId);
            map.put("skuId",21);    new Param().setSkuId(21);
            map.put("valueIds","3724|3726");    new Param().setValueIds("3724|3726");

            综上所述： map 可以代替class！
         */
        List<Map> mapList = skuSaleAttrValueMapper.selectSkuValueIdsMap(spuId);
        //  判断循环
        if (!CollectionUtils.isEmpty(mapList)){
            for (Map maps : mapList) {
                //  将所有的数据放入map 集合中！
                map.put(maps.get("value_ids"),maps.get("sku_id"));
            }
        }
        //  返回数据
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
