package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.vo.UsersVo;
import com.imooc.service.UserService;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import com.imooc.utils.RedisOperator;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/** @author afu */
@Controller
public class SsoController {
  @Autowired private UserService userService;
  @Autowired private RedisOperator redisOperator;
  private static final String REDIS_USER_TOKEN = "redis_user_token";
  public static final String REDIS_USER_TICKET = "redis_user_ticket";
  public static final String REDIS_TMP_TICKET = "redis_tmp_ticket";
  public static final String COOKIE_USER_TICKET = "cookie_user_ticket";

  @GetMapping("/login")
  public String login(
      String returnUrl, Model model, HttpServletRequest request, HttpServletResponse response) {
    model.addAttribute("returnUrl", returnUrl);
    // 返回 CAS 统一登陆验证
    return "login";
  }

  /**
   * CAS的统一登录接口
   *
   * <p>目的：
   *
   * <p>1. 登录后创建用户的全局会话 -> uniqueToken
   *
   * <p>2. 创建用户全局门票，用以表示在CAS端是否登录 -> userTicket
   *
   * <p>3. 创建用户的临时票据，用于回跳回传 -> tmpTicket
   *
   * @param username 用户名
   * @param password 密码
   * @param returnUrl 回跳地址
   * @param model model
   * @param request 请求
   * @param response 响应
   * @return 报文
   */
  @PostMapping("/doLogin")
  public String doLogin(
      String username,
      String password,
      String returnUrl,
      Model model,
      HttpServletRequest request,
      HttpServletResponse response) {
    model.addAttribute("returnUrl", returnUrl);
    if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
      model.addAttribute("errmsg", "用户名密码不能为空");
      return "login";
    }
    Users userResult = null;
    try {
      userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (userResult == null) {
      model.addAttribute("errmsg", "用户名密码不正确");
      return "login";
    }
    // 实现用户的redis会话，对应时序图的第 7，8 步
    String uniqueToken = UUID.randomUUID().toString().trim();
    UsersVo usersVO = new UsersVo();
    BeanUtils.copyProperties(userResult, usersVO);
    usersVO.setUserUniqueToken(uniqueToken);
    redisOperator.set(REDIS_USER_TOKEN + ":" + userResult.getId(), JsonUtils.objectToJson(usersVO));

    // 3. 生成ticket门票，全局门票，代表用户在CAS端登录过，对应时序图8，9步
    String userTicket = UUID.randomUUID().toString().trim();
    // 3.1 用户全局门票需要放入CAS端的cookie中
    setCookie(userTicket, response);
    // 4. userTicket关联用户id，并且放入到redis中，代表这个用户有门票了，可以在各个景区游玩
    redisOperator.set(REDIS_USER_TICKET + ":" + userTicket, userResult.getId());
    // 5. 生成临时票据，回跳到调用端网站，是由CAS端所签发的一个一次性的临时ticket
    String tmpTicket = createTmpTicket();
    /*
     * userTicket: 用于表示用户在CAS端的一个登录状态：已经登录
     * tmpTicket: 用于颁发给用户进行一次性的验证的票据，有时效性
     */
    /*
     * 举例：
     *      我们去动物园玩耍，大门口买了一张统一的门票，这个就是CAS系统的全局门票和用户全局会话。
     *      动物园里有一些小的景点，需要凭你的门票去领取一次性的票据，有了这张票据以后就能去一些小的景点游玩了。
     *      这样的一个个的小景点其实就是我们这里所对应的一个个的站点。
     *      当我们使用完毕这张临时票据以后，就需要销毁。
     */
    return "login";
    //    return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
  }
  /**
   * 创建临时票据
   *
   * @return 临时票据
   */
  private String createTmpTicket() {
    String tmpTicket = UUID.randomUUID().toString().trim();
    try {
      redisOperator.set(REDIS_TMP_TICKET + ":" + tmpTicket, MD5Utils.getMD5Str(tmpTicket), 600);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tmpTicket;
  }

  /**
   * 设置全局门票到cookie
   *
   * @param val 票据值
   * @param response 响应
   */
  private void setCookie(String val, HttpServletResponse response) {
    Cookie cookie = new Cookie(COOKIE_USER_TICKET, val);
    cookie.setDomain("");
    cookie.setPath("/");
    response.addCookie(cookie);
  }
}
