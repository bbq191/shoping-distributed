package com.imooc.controller.interceptor;

import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/** @author afu */
public class UserTokenInterceptor implements HandlerInterceptor {

  @Autowired private RedisOperator redisOperator;

  public static final String REDIS_USER_TOKEN = "redis_user_token";

  /**
   * 拦截请求，在访问controller调用之前
   *
   * @param request 请求
   * @param response 响应
   * @param handler 拦截处理器
   * @return T/F
   * @throws Exception 异常处理
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String userId = request.getHeader("headerUserId");
    String userToken = request.getHeader("headerUserToken");

    if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)) {
      String uniqueToken = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
      if (StringUtils.isBlank(uniqueToken)) {
        returnErrorResponse(response, IMOOCJSONResult.errorMsg("请登录..."));
        return false;
      } else {
        if (!uniqueToken.equals(userToken)) {
          returnErrorResponse(response, IMOOCJSONResult.errorMsg("账号在异地登录..."));
          return false;
        }
      }
    } else {
      returnErrorResponse(response, IMOOCJSONResult.errorMsg("请登录..."));
      return false;
    }
    /* false: 请求被拦截，被驳回，验证出现问题
    true: 请求在经过验证校验以后，是OK的，是可以放行的 */
    return true;
  }

  public void returnErrorResponse(HttpServletResponse response, IMOOCJSONResult result) {
    OutputStream out = null;
    try {
      response.setCharacterEncoding("utf-8");
      response.setContentType("text/json");
      out = response.getOutputStream();
      out.write(
          Objects.requireNonNull(JsonUtils.objectToJson(result)).getBytes(StandardCharsets.UTF_8));
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 请求访问controller之后，渲染视图之前
   *
   * @param request
   * @param response
   * @param handler
   * @param modelAndView
   * @throws Exception
   */
  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {}

  /**
   * 请求访问controller之后，渲染视图之后
   *
   * @param request
   * @param response
   * @param handler
   * @param ex
   * @throws Exception
   */
  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {}
}
