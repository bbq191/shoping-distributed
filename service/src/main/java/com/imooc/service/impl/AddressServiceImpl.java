package com.imooc.service.impl;

import com.imooc.enums.YesOrNo;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBo;
import com.imooc.service.AddressService;
import java.util.Date;
import java.util.List;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** @author afu */
@Service
public class AddressServiceImpl implements AddressService {

  @Autowired private UserAddressMapper userAddressMapper;
  @Autowired private Sid sid;

  @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
  @Override
  public List<UserAddress> queryAll(String userId) {
    UserAddress ua = new UserAddress();
    ua.setUserId(userId);

    return userAddressMapper.select(ua);
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  @Override
  public void addNewUserAddress(AddressBo addressBo) {
    // 1. 判断当前用户是否存在地址，如果没有，则新增为‘默认地址’
    int isDefault = 0;
    List<UserAddress> addressList = this.queryAll(addressBo.getUserId());
    if (addressList == null || addressList.isEmpty()) {
      isDefault = 1;
    }
    String addressId = sid.nextShort();
    // 2. 保存地址到数据库
    UserAddress newAddress = new UserAddress();
    BeanUtils.copyProperties(addressBo, newAddress);
    newAddress.setId(addressId);
    newAddress.setIsDefault(isDefault);
    newAddress.setCreatedTime(new Date());
    newAddress.setUpdatedTime(new Date());
    userAddressMapper.insert(newAddress);
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  @Override
  public void updateUserAddress(AddressBo addressBo) {
    String addressId = addressBo.getAddressId();
    UserAddress pendingAddress = new UserAddress();
    BeanUtils.copyProperties(addressBo, pendingAddress);
    pendingAddress.setId(addressId);
    pendingAddress.setUpdatedTime(new Date());
    userAddressMapper.updateByPrimaryKeySelective(pendingAddress);
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  @Override
  public void deleteUserAddress(String addressId, String userId) {
    UserAddress userAddress = new UserAddress();
    userAddress.setUserId(userId);
    userAddress.setId(addressId);
    userAddressMapper.delete(userAddress);
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  @Override
  public void updateUserAddressToDefualt(String addressId, String userId) {
    // 查出地址设置为非默认
    UserAddress userAddress = new UserAddress();
    userAddress.setUserId(userId);
    userAddress.setIsDefault(YesOrNo.YES.type);
    List<UserAddress> list = userAddressMapper.select(userAddress);
    for (UserAddress ua : list) {
      ua.setIsDefault(YesOrNo.NO.type);
      userAddressMapper.updateByPrimaryKeySelective(ua);
    }
    // 根据 id 修改为默认地址
    UserAddress defaultAddress = new UserAddress();
    defaultAddress.setId(addressId);
    defaultAddress.setUserId(userId);
    defaultAddress.setIsDefault(YesOrNo.YES.type);
    userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
  }

  @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
  @Override
  public UserAddress queryUserAddres(String addressId, String userId) {
    UserAddress singleAddress = new UserAddress();
    singleAddress.setId(addressId);
    singleAddress.setUserId(userId);

    return userAddressMapper.selectOne(singleAddress);
  }
}
