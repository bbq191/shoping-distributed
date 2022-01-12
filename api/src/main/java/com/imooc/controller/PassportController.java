package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.ShopCartBo;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** @author afu */
@Api(
    value = "注册登录",
    tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController extends BaseController {
  @Autowired private UserService userService;
  @Autowired private RedisOperator redisOperator;

  @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
  @GetMapping("/usernameIsExist")
  public IMOOCJSONResult usernameIsExist(@RequestParam String username) {
    // 判断传入参数是否为空
    if (StringUtils.isBlank(username)) {
      return IMOOCJSONResult.errorMsg("用户名不能为空");
    }
    // 查找用户是否存在
    boolean isExist = userService.queryUsernameIsExist(username);
    if (isExist) {
      return IMOOCJSONResult.errorMsg("用户名已存在");
    }
    // 不重复，返回成功
    return IMOOCJSONResult.ok();
  }

  @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
  @PostMapping("/regist")
  public IMOOCJSONResult regist(
      @RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
    String username = userBO.getUsername();
    String password = userBO.getPassword();
    String confirmPwd = userBO.getConfirmPassword();
    if (StringUtils.isBlank(username)
        || StringUtils.isBlank(password)
        || StringUtils.isBlank(confirmPwd)) {
      return IMOOCJSONResult.errorMsg("用户名密码不能为空");
    }
    // 查找用户是否存在
    boolean isExist = userService.queryUsernameIsExist(username);
    if (isExist) {
      return IMOOCJSONResult.errorMsg("用户名已存在");
    }
    if (password.length() < 6) {
      return IMOOCJSONResult.errorMsg("密码不能小于 6 位");
    }
    if (!password.equals(confirmPwd)) {
      return IMOOCJSONResult.errorMsg("两次密码必须一致");
    }
    Users userResult = userService.createUser(userBO);
    setNullPropertirs(userResult);
    // 设置 cookie 值
    CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), true);
    // 不重复，返回成功
    return IMOOCJSONResult.ok();
  }

  @ApiOperation(value = "用户登陆", notes = "用户登陆", httpMethod = "POST")
  @PostMapping("/login")
  public IMOOCJSONResult login(
      @RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
    String username = userBO.getUsername();
    String password = userBO.getPassword();
    if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
      return IMOOCJSONResult.errorMsg("用户名密码不能为空");
    }
    Users userResult = null;
    try {
      userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (userResult == null) {
      return IMOOCJSONResult.errorMsg("用户名密码不正确");
    }
    setNullPropertirs(userResult);
    // 设置 cookie 值
    CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), true);
    // TODO: 1/5/22 生成用户 token，存入 redis
    // 同步购物车数据
    synchShopcartData(userResult.getId(), request, response);
    return IMOOCJSONResult.ok(userResult);
  }

  @ApiOperation(value = "用户退出", notes = "用户退出并清理 cookie", httpMethod = "POST")
  @PostMapping("/logout")
  public IMOOCJSONResult logout(
      @RequestParam String userId, HttpServletRequest request, HttpServletResponse response) {
    // 清楚用户 cookie
    CookieUtils.deleteCookie(request, response, "user");
    CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);
    // todo 分布式会话中需要清除用户数据
    return IMOOCJSONResult.ok();
  }

  /**
   * 注册登录成功后，同步cookie和redis中的购物车数据
   *
   * @param userId 用户id
   * @param request 请求
   * @param response 响应
   */
  private void synchShopcartData(
      String userId, HttpServletRequest request, HttpServletResponse response) {
    /*
     * 1. redis中无数据，如果cookie中的购物车为空，那么这个时候不做任何处理
     *                 如果cookie中的购物车不为空，此时直接放入redis中
     * 2. redis中有数据，如果cookie中的购物车为空，那么直接把redis的购物车覆盖本地cookie
     *                 如果cookie中的购物车不为空，如果cookie中的某个商品在redis中存在，
     *                 则以cookie为主，删除redis中的，把cookie中的商品直接覆盖redis中（参考京东）
     * 3. 同步到redis中去了以后，覆盖本地cookie购物车的数据，保证本地购物车的数据是同步最新的
     */
    // 从redis中获取购物车
    String shopcartJsonRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
    // 从cookie中获取购物车
    String shopcartStrCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);
    if (StringUtils.isBlank(shopcartJsonRedis)) {
      // redis为空，cookie不为空，直接把cookie中的数据放入redis
      if (StringUtils.isNotBlank(shopcartStrCookie)) {
        redisOperator.set(FOODIE_SHOPCART + ":" + userId, shopcartStrCookie);
      }
    } else {
      // redis不为空，cookie不为空，合并cookie和redis中购物车的商品数据（同一商品则覆盖redis）
      if (StringUtils.isNotBlank(shopcartStrCookie)) {
        /*
         * 1. 已经存在的，把cookie中对应的数量，覆盖redis（参考京东）
         * 2. 该项商品标记为待删除，统一放入一个待删除的list
         * 3. 从cookie中清理所有的待删除list
         * 4. 合并redis和cookie中的数据
         * 5. 更新到redis和cookie中
         */
        List<ShopCartBo> shopcartListRedis =
            JsonUtils.jsonToList(shopcartJsonRedis, ShopCartBo.class);
        List<ShopCartBo> shopcartListCookie =
            JsonUtils.jsonToList(shopcartStrCookie, ShopCartBo.class);
        // 定义一个待删除list
        List<ShopCartBo> pendingDeleteList = new ArrayList<>();
        assert shopcartListRedis != null;
        for (ShopCartBo redisShopcart : shopcartListRedis) {
          String redisSpecId = redisShopcart.getSpecId();
          assert shopcartListCookie != null;
          for (ShopCartBo cookieShopcart : shopcartListCookie) {
            String cookieSpecId = cookieShopcart.getSpecId();
            if (redisSpecId.equals(cookieSpecId)) {
              // 覆盖购买数量，不累加，参考京东
              redisShopcart.setBuyCounts(cookieShopcart.getBuyCounts());
              // 把cookieShopcart放入待删除列表，用于最后的删除与合并
              pendingDeleteList.add(cookieShopcart);
            }
          }
        }
        // 从现有cookie中删除对应的覆盖过的商品数据
        assert shopcartListCookie != null;
        shopcartListCookie.removeAll(pendingDeleteList);
        // 合并两个list
        shopcartListRedis.addAll(shopcartListCookie);
        // 更新到redis和cookie
        CookieUtils.setCookie(
            request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartListRedis), true);
        redisOperator.set(
            FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartListRedis));
      } else {
        // redis不为空，cookie为空，直接把redis覆盖cookie
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, shopcartJsonRedis, true);
      }
    }
  }

  /**
   * 过滤敏感信息
   *
   * @param userResult 用户结果集
   */
  private void setNullPropertirs(Users userResult) {
    userResult.setPassword(null);
    userResult.setMobile(null);
    userResult.setEmail(null);
    userResult.setBirthday(null);
    userResult.setUpdatedTime(null);
    userResult.setCreatedTime(null);
  }
}
