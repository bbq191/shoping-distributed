package com.imooc.service;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBo;
import java.util.List;

/** @author afu */
public interface AddressService {

  /**
   * 根据用户 id 查询用户地址列表
   *
   * @param userId 用户 id
   * @return 地址列表
   */
  List<UserAddress> queryAll(String userId);

  /**
   * 用户新增地址
   *
   * @param addressBo 地址业务参数
   */
  void addNewUserAddress(AddressBo addressBo);

  /**
   * 修改用户地址
   *
   * @param addressBo 地址业务参数
   */
  void updateUserAddress(AddressBo addressBo);

  /**
   * 根据传入的地址及用户 id 删除对应信息
   *
   * @param addressId 地址 id
   * @param userId 用户 id
   */
  void deleteUserAddress(String addressId, String userId);

  /**
   * 根据传入的地址及用户 id 修改默认地址
   *
   * @param addressId 地址 id
   * @param userId 用户 id
   */
  void updateUserAddressToDefualt(String addressId, String userId);

  /**
   * 根据地址及用户 id 获取地址信息
   *
   * @param addressId 地址 id
   * @param userId 用户 id
   * @return 地址信息
   */
  UserAddress queryUserAddres(String addressId, String userId);
}
