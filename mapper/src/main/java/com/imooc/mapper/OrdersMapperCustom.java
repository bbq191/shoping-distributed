package com.imooc.mapper;

import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.vo.MyOrdersVo;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/** @author afu */
public interface OrdersMapperCustom {

  /**
   * 查询用户订单
   *
   * @param map 订单详情子列表
   * @return 订单列表封装
   */
  List<MyOrdersVo> queryMyOrders(@Param("paramsMap") Map<String, Object> map);

  /**
   * 查询订单数量
   *
   * @param map 参数map
   * @return 订单数量
   */
  int getMyOrderStatusCounts(@Param("paramsMap") Map<String, Object> map);

  /**
   * 订单动向
   *
   * @param map 参数列表
   * @return 订单动向列表
   */
  List<OrderStatus> getMyOrderTrend(@Param("paramsMap") Map<String, Object> map);
}
