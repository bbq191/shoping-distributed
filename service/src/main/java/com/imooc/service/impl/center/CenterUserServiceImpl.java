package com.imooc.service.impl.center;

import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBo;
import com.imooc.service.center.CenterUserService;
import java.util.Date;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** @author afu */
@Service
public class CenterUserServiceImpl implements CenterUserService {
  @Autowired private UsersMapper usersMapper;

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  @Override
  public Users queryUserInfo(String userId) {
    Users user = usersMapper.selectByPrimaryKey(userId);
    //    user.setPassword(null);
    return user;
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  @Override
  public Users updateUserInfo(String userId, CenterUserBo centerUserBo) {
    Users updateUser = new Users();
    BeanUtils.copyProperties(centerUserBo, updateUser);
    updateUser.setId(userId);
    updateUser.setUpdatedTime(new Date());
    usersMapper.updateByPrimaryKeySelective(updateUser);
    return queryUserInfo(userId);
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  @Override
  public Users updateUserFace(String userId, String faceUrl) {
    Users updateUser = new Users();
    updateUser.setId(userId);
    updateUser.setFace(faceUrl);
    updateUser.setUpdatedTime(new Date());

    usersMapper.updateByPrimaryKeySelective(updateUser);

    return queryUserInfo(userId);
  }
}
