package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseCategoryTrademark;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.CategoryTrademarkVo;
import com.atguigu.gmall.product.mapper.BaseCategoryTrademarkMapper;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import com.atguigu.gmall.product.service.BaseCategoryTrademarkService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiale
 * @date 2022/1/14 - 15:10
 */
@Service
public class BaseCategoryTrademarkServiceImpl extends ServiceImpl<BaseCategoryTrademarkMapper,BaseCategoryTrademark> implements BaseCategoryTrademarkService {
    @Autowired
    private BaseCategoryTrademarkMapper baseCategoryTrademarkMapper;

    @Autowired
    private BaseTrademarkMapper baseTrademarkMapper;

    @Override
    public List<BaseTrademark> findTrademarkList(Long category3Id) {
        //  传递的三级分类Id ，先通过三级分类Id 找到trademark_id
        QueryWrapper<BaseCategoryTrademark> baseCategoryTrademarkQueryWrapper = new QueryWrapper<>();
        baseCategoryTrademarkQueryWrapper.eq("category3_id",category3Id);
        List<BaseCategoryTrademark> baseCategoryTrademarkList = baseCategoryTrademarkMapper.selectList(baseCategoryTrademarkQueryWrapper);
        //  需要的是品牌Id集合！
        //  List<Long> tmIdList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(baseCategoryTrademarkList)){
            //            for (BaseCategoryTrademark baseCategoryTrademark : baseCategoryTrademarkList) {
            //                Long trademarkId = baseCategoryTrademark.getTrademarkId();
            //                tmIdList.add(trademarkId);
            //            }

            //  根据品牌Id 查询品牌数据！
            //  select * from base_trademark where id in (1,2,3,4,5);
            //  baseTrademarkMapper.selectList();
            //  List<BaseTrademark> baseTrademarkList = baseTrademarkMapper.selectBatchIds(tmIdList);
            //  jdk8 map 处理映射关系 口诀： 复制小括号，写死右箭头，落地大括号！
            //            baseCategoryTrademarkList.stream().map((baseCategoryTrademark -> {
            //               return null;
            //            }));
            List<Long> tmIdList = baseCategoryTrademarkList.stream().map(baseCategoryTrademark -> {
                //  获取品牌Id
                return baseCategoryTrademark.getTrademarkId();
            }).collect(Collectors.toList());
            //  根据品牌Id 查询品牌列表！
            List<BaseTrademark> baseTrademarkList = baseTrademarkMapper.selectBatchIds(tmIdList);
            //  返回数据！
            return baseTrademarkList;
        }
        //  默认返回！
        return null;
    }

    @Override
    public List<BaseTrademark> findCurrentTrademarkList(Long category3Id) {
        //根据三级分类id 查询已绑定好的品牌列表、
        QueryWrapper<BaseCategoryTrademark> baseCategoryTrademarkQueryWrapper = new QueryWrapper<>();
        baseCategoryTrademarkQueryWrapper.eq("category3_id",category3Id);
        List<BaseCategoryTrademark> baseCategoryTrademarkList = baseCategoryTrademarkMapper.selectList(baseCategoryTrademarkQueryWrapper);

        if(!CollectionUtils.isEmpty(baseCategoryTrademarkList)) {
            //  获取到小米，苹果，华为对应的品牌Id！
            List<Long> tmIdList = baseCategoryTrademarkList.stream().map(baseCategoryTrademark -> {
                return baseCategoryTrademark.getTrademarkId();
            }).collect(Collectors.toList());

            //  过滤小米，苹果，华为对应的品牌Id！  获取到所有的品牌Id
            //  baseTrademark 所有的品牌对象！
            //  获取到可选的品牌对象数据！
            List<BaseTrademark> baseTrademarkList = baseTrademarkMapper.selectList(null).stream().filter(baseTrademark -> {
                return !tmIdList.contains(baseTrademark.getId());
            }).collect(Collectors.toList());
            return baseTrademarkList;

        }
        //  当对应的三级分类Id 没有品牌的时候，应该查询所有的品牌数据！
        return baseTrademarkMapper.selectList(null);
    }

    @Override
    public void save(CategoryTrademarkVo categoryTrademarkVo) {
        //  本质： 插入数据 base_category_trademark
        /*
        private Long category3Id;
        private List<Long> trademarkIdList;
         */
        List<Long> trademarkIdList = categoryTrademarkVo.getTrademarkIdList();
        //  判断
        if (!CollectionUtils.isEmpty(trademarkIdList)) {
            //  循环遍历 第一种方案：
            //            for (Long tmId : trademarkIdList) {
            //                //  声明对象
            //                BaseCategoryTrademark baseCategoryTrademark = new BaseCategoryTrademark();
            //                //  给对象赋值
            //                baseCategoryTrademark.setCategory3Id(categoryTrademarkVo.getCategory3Id());
            //                baseCategoryTrademark.setTrademarkId(tmId);
            //                //  保存数据！
            //                baseCategoryTrademarkMapper.insert(baseCategoryTrademark);
            //            }
            //  批量插入数据！
            List<BaseCategoryTrademark> baseCategoryTrademarkList = trademarkIdList.stream().map(tmId -> {
                //  声明对象
                BaseCategoryTrademark baseCategoryTrademark = new BaseCategoryTrademark();

                baseCategoryTrademark.setCategory3Id(categoryTrademarkVo.getCategory3Id());
                baseCategoryTrademark.setTrademarkId(tmId);
                //  返回数据
                return baseCategoryTrademark;
            }).collect(Collectors.toList());

            //  可以批量添加！
            this.saveBatch(baseCategoryTrademarkList);
        }
    }

    @Override
    public void remove(Long category3Id, Long trademarkId) {
        //  本质：逻辑删除 修改is_delete  执行的update 语句，并非delete！
        QueryWrapper<BaseCategoryTrademark> baseCategoryTrademarkQueryWrapper = new QueryWrapper<>();
        baseCategoryTrademarkQueryWrapper.eq("category3_id",category3Id);
        baseCategoryTrademarkQueryWrapper.eq("trademark_id",trademarkId);
        baseCategoryTrademarkMapper.delete(baseCategoryTrademarkQueryWrapper);
    }


}
