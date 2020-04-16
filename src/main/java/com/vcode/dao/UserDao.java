package com.vcode.dao;

import com.vcode.entity.User;

import java.util.List;

/**
 * @author moyee
 */
public interface UserDao {

  void saveUser(User user);

  void deleteUserByAccount(String account);

  User findUserByUserAccount(String account);

  String updateUser(User user);

  List<User> findUsers(int page, int size, String search);

  List<User> findAdmins(int page, int size, String search);

  long count(String search);

  long countAdmins(String search);
  boolean isNicknameExist(User user);

  boolean isEmailExist(User user);

}
