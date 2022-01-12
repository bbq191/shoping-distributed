package com.imooc.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/** @author afu */
@ApiModel(value = "商品对象 VO", description = "数据组织封装在此 entity 中")
public class ShopCartVo {
  @ApiModelProperty(value = "商品 id", name = "itemId", example = "imooc", required = true)
  private String itemId;

  @ApiModelProperty(value = "商品图片", name = "itemUrl", example = "imooc", required = true)
  private String itemUrl;

  @ApiModelProperty(value = "商品名称", name = "itemName", example = "imooc", required = true)
  private String itemName;

  @ApiModelProperty(value = "规格 id", name = "specId", example = "imooc", required = true)
  private String specId;

  @ApiModelProperty(value = "规格名称", name = "specName", example = "imooc", required = true)
  private String specName;

  @ApiModelProperty(value = "折扣价", name = "priceDiscount", example = "imooc", required = true)
  private String priceDiscount;

  @ApiModelProperty(value = "正价", name = "priceNormal", example = "imooc", required = true)
  private String priceNormal;

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public String getItemUrl() {
    return itemUrl;
  }

  public void setItemUrl(String itemUrl) {
    this.itemUrl = itemUrl;
  }

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public String getSpecId() {
    return specId;
  }

  public void setSpecId(String specId) {
    this.specId = specId;
  }

  public String getSpecName() {
    return specName;
  }

  public void setSpecName(String specName) {
    this.specName = specName;
  }

  public String getPriceDiscount() {
    return priceDiscount;
  }

  public void setPriceDiscount(String priceDiscount) {
    this.priceDiscount = priceDiscount;
  }

  public String getPriceNormal() {
    return priceNormal;
  }

  public void setPriceNormal(String priceNormal) {
    this.priceNormal = priceNormal;
  }
}
