package com.imooc.mapper;

import com.imooc.pojo.vo.CategoryVo;
import com.imooc.pojo.vo.NewItemsVo;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/** @author afu */
public interface CategoryMapperCustom {

  /**
   * 自定义商品列表 vo
   *
   * @param rootCatId 根id
   * @return 商品列表
   */
  List<CategoryVo> getSubCatList(Integer rootCatId);

  /**
   * 查询首页每个一级分类下的 6 个商品
   *
   * @param map 品名map
   * @return 商品list
   */
  List<NewItemsVo> getSixNewItemLazy(@Param("paramMap") Map<String, Object> map);
}
