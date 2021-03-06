package com.imooc.enums;

/** @author afu */
public enum PayMethod {
  /** 支付方式 枚举 */
  WEIXIN(1, "微信"),
  ALIPAY(2, "支付宝");

  public final Integer type;
  public final String value;

  PayMethod(Integer type, String value) {
    this.type = type;
    this.value = value;
  }
}
