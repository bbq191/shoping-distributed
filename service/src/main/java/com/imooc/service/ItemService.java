package com.imooc.service;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVo;
import com.imooc.pojo.vo.ShopCartVo;
import com.imooc.utils.PagedGridResult;
import java.util.List;

/** @author afu */
public interface ItemService {

  /**
   * 根据商品 id 查询商品
   *
   * @param itemId 商品 id
   * @return 商品对象
   */
  Items queryItemById(String itemId);

  /**
   * 根据商品 id 查询商品图片列表
   *
   * @param itemId 商品 id
   * @return 商品对象
   */
  List<ItemsImg> queryItemImgList(String itemId);
  /**
   * 根据商品 id 查询商品规格列表
   *
   * @param itemId 商品 id
   * @return 商品对象
   */
  List<ItemsSpec> queryItemSpecList(String itemId);
  /**
   * 根据商品 id 查询商品参数
   *
   * @param itemId 商品 id
   * @return 商品参数对象
   */
  ItemsParam queryItemParam(String itemId);

  /**
   * 查询商品评价数量
   *
   * @param itemId 商品 id
   * @return 评价数量 vo
   */
  CommentLevelCountsVo queryCommentCounts(String itemId);

  /**
   * 查询评价详情（分页）
   *
   * @param itemId 商品 id
   * @param level 评价级别
   * @param page 显示页
   * @param pageSize 当前页条数
   * @return 评价详情列表
   */
  PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);

  /**
   * 搜索商品列表
   *
   * @param keywords 搜索关键字
   * @param sort 排序规则
   * @param page 显示页
   * @param pageSize 每页数量
   * @return 商品分页列表
   */
  PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);

  /**
   * 根据分类 id 搜索商品列表
   *
   * @param catId 分类 id
   * @param sort 排序规则
   * @param page 显示页
   * @param pageSize 每页数量
   * @return 商品分页列表
   */
  PagedGridResult searchItemsByThirdCat(String catId, String sort, Integer page, Integer pageSize);

  /**
   * 根据规格 ids 查询最新购物车中的商品数据
   *
   * @param specIds 规格 id 拼接串
   * @return 商品列表
   */
  List<ShopCartVo> queryItemsBySpecIds(String specIds);

  /**
   * 根据 specId 获取商品规格信息
   *
   * @param specId 规格 id
   * @return 商品规格
   */
  ItemsSpec queryItemSpecById(String specId);

  /**
   * 根据商品 id 获取主图片
   *
   * @param itemId 商品 id
   * @return 图片地址
   */
  String queryItemMainImgById(String itemId);

  /**
   * 减少库存
   *
   * @param itemSpecId 规格 id
   * @param buyCounts 减少数量
   */
  void decreaseItemSpecStock(String itemSpecId, int buyCounts);
}
