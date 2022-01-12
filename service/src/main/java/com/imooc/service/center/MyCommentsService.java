package com.imooc.service.center;

import com.imooc.pojo.OrderItems;
import com.imooc.pojo.bo.center.OrderItemsCommentBo;
import com.imooc.utils.PagedGridResult;
import java.util.List;

/** @author afu */
public interface MyCommentsService {

  /**
   * 根据订单id查询关联的商品待评价列表
   *
   * @param orderId 订单编号
   * @return 评价列表
   */
  List<OrderItems> queryPendingComment(String orderId);

  /**
   * 保存用户的评论
   *
   * @param orderId 订单编号
   * @param userId 用户编号
   * @param commentList 前端传入评论列表
   */
  void saveComments(String orderId, String userId, List<OrderItemsCommentBo> commentList);

  /**
   * 我的评价查询 分页
   *
   * @param userId 用户 id
   * @param page 分页
   * @param pageSize 每页数据
   * @return 分页评价列表
   */
  PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);
}
