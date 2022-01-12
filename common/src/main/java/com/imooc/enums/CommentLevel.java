package com.imooc.enums;

/** @author afu */
public enum CommentLevel {
  /** 枚举值 评价类型 */
  GOOD(1, "好评"),
  NORMAL(2, "中评"),
  BAD(3, "差评");

  public final Integer type;
  public final String value;

  CommentLevel(Integer type, String value) {
    this.type = type;
    this.value = value;
  }
}
