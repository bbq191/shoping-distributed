package com.imooc.controller;

import com.imooc.pojo.Orders;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.IMOOCJSONResult;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/** @author afu */
@Controller
public class BaseController {
  @Autowired public MyOrdersService myOrdersService;
  public static final String FOODIE_SHOPCART = "shopcart";
  public static final Integer COMMON_PAGE_SIZE = 10;
  public static final Integer PAGE_SIZE = 20;
  /** 微信支付成功 -> 支付中心 -> shopping平台 // |-> 回调通知 URL */
  String payReturnUrl = "http://localhost:8088//orders/notifyMerchantOrderPaid";
  /** 支付中心的调用地址 */
  String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
  /** 用户上传头像的位置 */
  public static final String IMAGE_USER_FACE_LOCATION =
      File.separator
          + "Users"
          + File.separator
          + "afu"
          + File.separator
          + "Workspace"
          + File.separator
          + "images"
          + File.separator
          + "foodie"
          + File.separator
          + "faces";

  /**
   * 用于验证用户和订单是否有关联关系，避免非法用户调用
   *
   * @return 检查结果
   */
  public IMOOCJSONResult checkUserOrder(String userId, String orderId) {
    Orders order = myOrdersService.queryMyOrder(userId, orderId);
    if (order == null) {
      return IMOOCJSONResult.errorMsg("订单不存在！");
    }
    return IMOOCJSONResult.ok(order);
  }
}
