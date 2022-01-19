package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jiale
 * @date 2022/1/11 - 18:55
 */
@Mapper
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {
    /**
     * 根据分类id查询属性数据
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    List<BaseAttrInfo> selectAttrInfoList(@Param("category1Id") Long category1Id, @Param("category2Id") Long category2Id, @Param("category3Id") Long category3Id);

    /**
     *  根据skuId 来获取到对应的平台属性数据
     * @param skuId
     * @return
     */
    List<BaseAttrInfo> selectAttrList(Long skuId);
}
