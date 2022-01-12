package com.imooc.service.center;

import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.OrderStatusCountsVo;
import com.imooc.utils.PagedGridResult;

/** @author afu */
public interface MyOrdersService {

  /**
   * 查询我的订单列表
   *
   * @param userId 用户id
   * @param orderStatus 订单状态
   * @param page 第几页
   * @param pageSize 每页数量
   * @return 订单分页列表
   */
  PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);

  /**
   * 订单状态 --> 商家发货
   *
   * @param orderId 订单id
   */
  void updateDeliverOrderStatus(String orderId);

  /**
   * 查询我的订单
   *
   * @param userId 用户 id
   * @param orderId 订单 id
   * @return 用户关联订单
   */
  Orders queryMyOrder(String userId, String orderId);

  /**
   * 更新订单状态 —> 确认收货
   *
   * @param orderId 订单id
   * @return 成功失败
   */
  boolean updateReceiveOrderStatus(String orderId);

  /**
   * 删除订单（逻辑删除）
   *
   * @param userId 用户 id
   * @param orderId 订单id
   * @return 成功失败
   */
  boolean deleteOrder(String userId, String orderId);

  /**
   * 查询用户订单数
   *
   * @param userId 用户id
   * @return 分状态订单数量
   */
  OrderStatusCountsVo getOrderStatusCounts(String userId);

  /**
   * 获得分页的订单动向
   *
   * @param userId 用户id
   * @param page 分页
   * @param pageSize 每页数量
   * @return 订单动向分页列表
   */
  PagedGridResult getOrdersTrend(String userId, Integer page, Integer pageSize);
}
