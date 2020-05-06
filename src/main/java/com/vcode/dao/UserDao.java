package com.vcode.dao;

import com.vcode.entity.User;

import java.util.List;

/**
 * @author moyee
 */
public interface UserDao {

  /**
   * 保存user
   *
   * @param user user实例
   */
  void saveUser(User user);

  /**
   * 根据用户账号删除用户
   *
   * @param account 用户账号
   */
  void deleteUserByAccount(String account);

  /**
   * 查询用户
   *
   * @param account 用户账号
   * @return 查询结果，不存在返回null
   */
  User findUserByUserAccount(String account);

  /**
   * 更新用户数据
   *
   * @param user user实例
   * @return 返回错误信息
   */
  String updateUser(User user);

  /**
   * 查询用户列表
   *
   * @param page   页码
   * @param size   一页的容量
   * @param search 查询条件
   * @return 对应条件下的用户列表
   */
  List<User> findUsers(int page, int size, String search);

  /**
   * 查询管理员权限的用户
   *
   * @param page   页码
   * @param size   一页的容量
   * @param search 查询条件
   * @return 对应条件下的用户列表
   */
  List<User> findAdmins(int page, int size, String search);

  /**
   * 统计对应条件下的用户数量
   *
   * @param search 查询条件
   * @return 查询数量
   */
  long count(String search);

  /**
   * 统计对应条件下的管理员数量
   *
   * @param search 查询条件
   * @return 查询数量
   */
  long countAdmins(String search);

  /**
   * 判断nickname是否重复
   *
   * @param user user实例
   * @return true 重复 false不重复
   */
  boolean isNicknameExist(User user);

  /**
   * 判断email是否已被使用
   *
   * @param user user实例
   * @return true 重复 false不重复
   */
  boolean isEmailExist(User user);

  /**
   * (注册时)暂时存储注册时应验证的邮箱信息
   *
   * @param email
   * @param UUID
   * @return
   */
  void storeEmailInRedis(String email,String UUID);

  /**
   * (注册时)根据UUID查询对应邮箱是否为同一人申请和确认
   *
   * @param email
   * @param UUID
   * @return
   */
  boolean checkEmailInRedis(String email,String UUID);
}
