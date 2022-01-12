package com.imooc.mapper;

import com.imooc.pojo.vo.ItemCommentVo;
import com.imooc.pojo.vo.SearchItemsVo;
import com.imooc.pojo.vo.ShopCartVo;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/** @author afu */
public interface ItemsMapperCustom {

  /**
   * 组装商品评价 vo
   *
   * @param map 参数列表
   * @return 评价列表
   */
  List<ItemCommentVo> queryItemComments(@Param("paramsMap") Map<String, Object> map);

  /**
   * 组装查询商品 vo
   *
   * @param map 参数列表
   * @return 商品列表
   */
  List<SearchItemsVo> searchItems(@Param("paramsMap") Map<String, Object> map);

  /**
   * 组装查询三级商品 vo
   *
   * @param map 参数列表
   * @return 商品列表
   */
  List<SearchItemsVo> searchItemsByThirdCat(@Param("paramsMap") Map<String, Object> map);

  /**
   * 组装根据多个规格 id 查询 vo
   *
   * @param specIdsList 参数列表
   * @return 商品列表
   */
  List<ShopCartVo> queryItemsBySpecIds(@Param("paramsList") List<String> specIdsList);

  /**
   * 减库存
   *
   * @param specId 规格 id
   * @param pendingCounts 出库数量
   * @return 剩余数量
   */
  int decreaseItemSpecStock(
      @Param("specId") String specId, @Param("pendingCounts") int pendingCounts);
}
