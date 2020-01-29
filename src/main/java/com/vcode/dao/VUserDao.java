package com.vcode.dao;


import com.vcode.entitiy.VUser;

public interface VUserDao {
  public void saveUser(VUser user);
  
  public VUser findUserByUserAccount(String account);
  
  public long updateUser(VUser user);
  
  public void deleteUserById(Long id);
}
