package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.vo.UsersVo;
import com.imooc.utils.RedisOperator;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/** @author afu */
@Controller
public class BaseController {
  @Autowired private RedisOperator redisOperator;
  public static final String REDIS_USER_TOKEN = "redis_user_token";

  /**
   * 将用户数据转换为 redis 可用的 vo 对象
   *
   * @param user 用户数据
   * @return redis 需要的 vo
   */
  public UsersVo conventUsersVo(Users user) {
    // 实现用户的redis会话
    String uniqueToken = UUID.randomUUID().toString().trim();
    redisOperator.set(REDIS_USER_TOKEN + ":" + user.getId(), uniqueToken);

    UsersVo usersVO = new UsersVo();
    BeanUtils.copyProperties(user, usersVO);
    usersVO.setUserUniqueToken(uniqueToken);
    return usersVO;
  }
}
