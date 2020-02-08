package com.vcode.dao;


import com.vcode.entity.VUser;

public interface VUserDao {

  void saveUser(VUser user);

  VUser findUserByUserAccount(String account);

  // 传入最新数据的实体即可
  void updateUser(VUser user);

  void deleteUserByAccount(String account);

}
