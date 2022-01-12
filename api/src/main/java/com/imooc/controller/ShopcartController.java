package com.imooc.controller;

import com.imooc.pojo.bo.ShopCartBo;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** @author afu */
@Api(
    value = "购物车接口",
    tags = {"用于购物车的相关接口"})
@RestController
@RequestMapping("shopcart")
public class ShopcartController extends BaseController {
  @Autowired private RedisOperator redisOperator;

  @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
  @PostMapping("/add")
  public IMOOCJSONResult add(
      @ApiParam(name = "userId", value = "用户 id", required = true) @RequestParam String userId,
      @ApiParam(name = "shopCartBo", value = "购物车 BO", required = true) @RequestBody
          ShopCartBo shopCartBo,
      HttpServletRequest request,
      HttpServletResponse response) {
    if (StringUtils.isBlank(userId)) {
      return IMOOCJSONResult.errorMsg("用户 id 为空");
    }
    System.out.println(shopCartBo);
    // 前端用户在登陆到情况下添加商品到购物车会同步添加到 redis
    // 需要判断当前购物车中包含已经存在的商品，如果存在则累加购买数量
    String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
    List<ShopCartBo> shopcartList = null;
    if (StringUtils.isNotBlank(shopcartJson)) {
      // redis中已经有购物车了
      shopcartList = JsonUtils.jsonToList(shopcartJson, ShopCartBo.class);
      // 判断购物车中是否存在已有商品，如果有的话counts累加
      boolean isHaving = false;
      assert shopcartList != null;
      for (ShopCartBo sc : shopcartList) {
        String tmpSpecId = sc.getSpecId();
        if (tmpSpecId.equals(shopCartBo.getSpecId())) {
          sc.setBuyCounts(sc.getBuyCounts() + shopCartBo.getBuyCounts());
          isHaving = true;
        }
      }
      if (!isHaving) {
        shopcartList.add(shopCartBo);
      }
    } else {
      // redis中没有购物车
      shopcartList = new ArrayList<>();
      // 直接添加到购物车中
      shopcartList.add(shopCartBo);
    }
    // 覆盖现有redis中的购物车
    redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartList));
    return IMOOCJSONResult.ok();
  }

  @ApiOperation(value = "从购物车删除商品", notes = "从购物车删除商品", httpMethod = "POST")
  @PostMapping("/del")
  public IMOOCJSONResult del(
      @ApiParam(name = "userId", value = "用户 id", required = true) @RequestParam String userId,
      @ApiParam(name = "itemSpecId", value = "商品规格 id", required = true) @RequestParam
          String itemSpecId,
      HttpServletRequest request,
      HttpServletResponse response) {
    if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
      return IMOOCJSONResult.errorMsg("参数为空");
    }
    // 用户删除页面购物车中商品，用户在登陆到情况下会删除购物车中商品并会同步删除 redis 信息
    String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
    if (StringUtils.isNotBlank(shopcartJson)) {
      // redis中已经有购物车了
      List<ShopCartBo> shopcartList = JsonUtils.jsonToList(shopcartJson, ShopCartBo.class);
      // 判断购物车中是否存在已有商品，如果有的话则删除
      assert shopcartList != null : "shopcartList 不能为 null";
      for (ShopCartBo sc : shopcartList) {
        String tmpSpecId = sc.getSpecId();
        if (tmpSpecId.equals(itemSpecId)) {
          shopcartList.remove(sc);
          break;
        }
      }
      // 覆盖现有redis中的购物车
      redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartList));
    }
    return IMOOCJSONResult.ok();
  }
}
