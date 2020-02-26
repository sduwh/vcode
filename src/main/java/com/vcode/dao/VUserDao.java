package com.vcode.dao;


import com.vcode.entity.VUser;

import java.util.List;

public interface VUserDao {

  void saveUser(VUser user);

  void deleteUserByAccount(String account);

  VUser findUserByUserAccount(String account);

  String updateUser(VUser user);

  List<VUser> findUsers(int page, int size, String search);

  List<VUser> finAdmins(int page, int size, String search);

  long count(String search);

  boolean isNicknameExist(VUser user);

  boolean isEmailExist(VUser user);

}
