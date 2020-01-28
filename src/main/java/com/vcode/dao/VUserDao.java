package com.vcode.dao;

import com.vcode.entitiy.VUserEntity;

public interface VUserDao {
  void saveVUser(VUserEntity vuserEntity);
  void removeVUser(Long Id);
  void updateVUser(VUserEntity vUserEntity);
  boolean isExitVUser(String account);
  VUserEntity findById(Long Id);
  VUserEntity findByAccount(String account);
}
