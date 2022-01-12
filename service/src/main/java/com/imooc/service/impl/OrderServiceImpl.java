package com.imooc.service.impl;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.ShopCartBo;
import com.imooc.pojo.bo.SubmitOrderBo;
import com.imooc.pojo.vo.MerchantOrdersVo;
import com.imooc.pojo.vo.OrderVo;
import com.imooc.service.AddressService;
import com.imooc.service.ItemService;
import com.imooc.service.OrderService;
import com.imooc.utils.DateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** @author afu */
@Service
public class OrderServiceImpl implements OrderService {
  @Autowired private OrdersMapper ordersMapper;
  @Autowired private OrderItemsMapper orderItemsMapper;
  @Autowired private OrderStatusMapper orderStatusMapper;
  @Autowired private AddressService addressService;
  @Autowired private ItemService itemService;
  @Autowired private Sid sid;

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  @Override
  public OrderVo createOrder(List<ShopCartBo> shopcartList, SubmitOrderBo submitOrderBo) {
    String userId = submitOrderBo.getUserId();
    String addressId = submitOrderBo.getAddressId();
    String itemSpecIds = submitOrderBo.getItemSpecIds();
    Integer payMethod = submitOrderBo.getPayMethod();
    String leftMsg = submitOrderBo.getLeftMsg();
    // 包邮费用设置为0
    int postAmount = 0;
    String orderId = sid.nextShort();
    UserAddress address = addressService.queryUserAddres(addressId, userId);
    // 1. 新订单数据保存
    Orders newOrder = new Orders();
    newOrder.setId(orderId);
    newOrder.setUserId(userId);
    newOrder.setReceiverName(address.getReceiver());
    newOrder.setReceiverMobile(address.getMobile());
    newOrder.setReceiverAddress(
        address.getProvince()
            + " "
            + address.getCity()
            + " "
            + address.getDistrict()
            + " "
            + address.getDetail());
    newOrder.setPostAmount(postAmount);
    newOrder.setPayMethod(payMethod);
    newOrder.setLeftMsg(leftMsg);
    newOrder.setIsComment(YesOrNo.NO.type);
    newOrder.setIsDelete(YesOrNo.NO.type);
    newOrder.setCreatedTime(new Date());
    newOrder.setUpdatedTime(new Date());

    // 2. 循环根据itemSpecIds保存订单商品信息表
    String[] itemSpecIdArr = itemSpecIds.split(",");
    // 商品原价累计
    int totalAmount = 0;
    // 优惠后的实际支付价格累计
    int realPayAmount = 0;
    List<ShopCartBo> toBeRemovedShopcatdList = new ArrayList<>();

    for (String itemSpecId : itemSpecIdArr) {
      ShopCartBo cartItem = getBuyCountsFromShopcart(shopcartList, itemSpecId);
      // 整合redis后，商品购买的数量重新从redis的购物车中获取
      int buyCounts = cartItem.getBuyCounts();
      // 2.1 根据规格id，查询规格的具体信息，主要获取价格
      ItemsSpec itemSpec = itemService.queryItemSpecById(itemSpecId);
      totalAmount += itemSpec.getPriceNormal() * buyCounts;
      realPayAmount += itemSpec.getPriceDiscount() * buyCounts;

      // 2.2 根据商品id，获得商品信息以及商品图片
      String itemId = itemSpec.getItemId();
      Items item = itemService.queryItemById(itemId);
      String imgUrl = itemService.queryItemMainImgById(itemId);

      // 2.3 循环保存子订单数据到数据库
      String subOrderId = sid.nextShort();
      OrderItems subOrderItem = new OrderItems();
      subOrderItem.setId(subOrderId);
      subOrderItem.setOrderId(orderId);
      subOrderItem.setItemId(itemId);
      subOrderItem.setItemName(item.getItemName());
      subOrderItem.setItemImg(imgUrl);
      subOrderItem.setBuyCounts(buyCounts);
      subOrderItem.setItemSpecId(itemSpecId);
      subOrderItem.setItemSpecName(itemSpec.getName());
      subOrderItem.setPrice(itemSpec.getPriceDiscount());
      orderItemsMapper.insert(subOrderItem);

      // 2.4 在用户提交订单以后，规格表中需要扣除库存
      itemService.decreaseItemSpecStock(itemSpecId, buyCounts);
    }

    newOrder.setTotalAmount(totalAmount);
    newOrder.setRealPayAmount(realPayAmount);
    ordersMapper.insert(newOrder);

    // 3. 保存订单状态表
    OrderStatus waitPayOrderStatus = new OrderStatus();
    waitPayOrderStatus.setOrderId(orderId);
    waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
    waitPayOrderStatus.setCreatedTime(new Date());
    orderStatusMapper.insert(waitPayOrderStatus);

    // 4. 构建商户订单，用于传给支付中心
    MerchantOrdersVo merchantOrdersVo = new MerchantOrdersVo();
    merchantOrdersVo.setMerchantOrderId(orderId);
    merchantOrdersVo.setMerchantUserId(userId);
    merchantOrdersVo.setAmount(realPayAmount + postAmount);
    merchantOrdersVo.setPayMethod(payMethod);

    // 5. 构建自定义订单vo
    OrderVo orderVo = new OrderVo();
    orderVo.setOrderId(orderId);
    orderVo.setMerchantOrdersVo(merchantOrdersVo);
    orderVo.setToBeRemovedShopcatdList(toBeRemovedShopcatdList);

    return orderVo;
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  @Override
  public void updateOrderStatus(String orderId, Integer orderStatus) {

    OrderStatus paidStatus = new OrderStatus();
    paidStatus.setOrderId(orderId);
    paidStatus.setOrderStatus(orderStatus);
    paidStatus.setPayTime(new Date());

    orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
  }

  @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
  @Override
  public OrderStatus queryOrderStatusInfo(String orderId) {
    return orderStatusMapper.selectByPrimaryKey(orderId);
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  @Override
  public void closeOrder() {

    // 查询所有未付款订单，判断时间是否超时（1天），超时则关闭交易
    OrderStatus queryOrder = new OrderStatus();
    queryOrder.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
    List<OrderStatus> list = orderStatusMapper.select(queryOrder);
    for (OrderStatus os : list) {
      // 获得订单创建时间
      Date createdTime = os.getCreatedTime();
      // 和当前时间进行对比
      int days = DateUtil.daysBetween(createdTime, new Date());
      if (days >= 1) {
        // 超过1天，关闭订单
        doCloseOrder(os.getOrderId());
      }
    }
  }

  /**
   * 关闭订单
   *
   * @param orderId 订单 id
   */
  private void doCloseOrder(String orderId) {
    OrderStatus close = new OrderStatus();
    close.setOrderId(orderId);
    close.setOrderStatus(OrderStatusEnum.CLOSE.type);
    close.setCloseTime(new Date());
    orderStatusMapper.updateByPrimaryKeySelective(close);
  }

  /**
   * 从redis中的购物车里获取商品counts
   *
   * @param shopcartList 购物车列表
   * @param specId 商品规格id
   * @return 购物车对象
   */
  private ShopCartBo getBuyCountsFromShopcart(List<ShopCartBo> shopcartList, String specId) {
    for (ShopCartBo cart : shopcartList) {
      if (cart.getSpecId().equals(specId)) {
        return cart;
      }
    }
    return null;
  }
}
