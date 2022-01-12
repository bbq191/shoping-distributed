package com.imooc.enums;

/** @author afu */
public enum YesOrNo {
  /** 枚举值 是否 */
  NO(0, "否"),
  YES(1, "是");

  public final Integer type;
  public final String value;

  YesOrNo(Integer type, String value) {
    this.type = type;
    this.value = value;
  }
}
