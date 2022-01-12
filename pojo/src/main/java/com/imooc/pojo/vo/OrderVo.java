package com.imooc.pojo.vo;

import com.imooc.pojo.bo.ShopCartBo;
import java.util.List;

/** @author afu */
public class OrderVo {
  private String orderId;
  private MerchantOrdersVo merchantOrdersVo;
  private List<ShopCartBo> toBeRemovedShopcatdList;

  public String getOrderId() {
    return orderId;
  }

  public MerchantOrdersVo getMerchantOrdersVo() {
    return merchantOrdersVo;
  }

  public void setMerchantOrdersVo(MerchantOrdersVo merchantOrdersVo) {
    this.merchantOrdersVo = merchantOrdersVo;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public List<ShopCartBo> getToBeRemovedShopcatdList() {
    return toBeRemovedShopcatdList;
  }

  public void setToBeRemovedShopcatdList(List<ShopCartBo> toBeRemovedShopcatdList) {
    this.toBeRemovedShopcatdList = toBeRemovedShopcatdList;
  }
}
