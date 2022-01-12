package com.imooc.service.center;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBo;

/** @author afu */
public interface CenterUserService {
  /**
   * 根据用户id查询用户信息
   *
   * @param userId 用户id
   * @return 用户对象
   */
  Users queryUserInfo(String userId);

  /**
   * 修改用户信息
   *
   * @param userId 用户id
   * @param centerUserBo 用户信息参数
   * @return 更新后的用户对象
   */
  Users updateUserInfo(String userId, CenterUserBo centerUserBo);

  /**
   * 用户头像更新
   *
   * @param userId 用户id
   * @param faceUrl 头像url
   * @return 用户信息
   */
  Users updateUserFace(String userId, String faceUrl);
}
