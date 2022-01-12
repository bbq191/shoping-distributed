package com.imooc.service;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;

/** @author afu */
public interface UserService {

  /**
   * 判断是否存在相同用户名
   *
   * @param username 用户名
   * @return boolean
   */
  public boolean queryUsernameIsExist(String username);

  /**
   * 新建用户，注册用户
   *
   * @param userBO 前端传入的业务对象
   * @return 注册后的用户脱敏信息
   */
  public Users createUser(UserBO userBO);

  /**
   * 检索用户名和密码是否匹配，用于登陆
   *
   * @param username 用户名
   * @param password 密码
   * @return 登陆成功后的用户对象，需要脱敏
   */
  public Users queryUserForLogin(String username, String password);
}
