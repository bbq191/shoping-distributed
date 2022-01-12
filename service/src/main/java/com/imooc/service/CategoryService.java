package com.imooc.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVo;
import com.imooc.pojo.vo.NewItemsVo;
import java.util.List;

/** @author afu */
public interface CategoryService {

  /**
   * 商品列表一级分类
   *
   * @return 图片列表
   */
  List<Category> queryAllRootLevelCat();

  /**
   * 获取懒加载商品列表子分类
   *
   * @param rootCatId 父级id
   * @return 商品列表 list
   */
  List<CategoryVo> getSubCatList(Integer rootCatId);

  /**
   * 查询首页每个一级分类下的 6 个商品
   *
   * @param rootCatId 商品根列表 id
   * @return 商品列表
   */
  List<NewItemsVo> getSixNewItemLazy(Integer rootCatId);
}
