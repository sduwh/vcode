package com.vcode.dao;

import com.vcode.entity.Group;

import java.util.List;

/**
 * 操作群组的实体类的接口
 *
 */

public interface GroupDao {

  /**
   * 保存group
   *
   * @param group group实例
   */
  void saveGroup(Group group);

  /**
   * 查询group
   *
   * @param name group's name
   * @return 查询的结果Group实例，不存在返回null
   */

  Group findGroupByName(String name);

  /**
   * 更新group
   *
   * @param group group实例
   */
  void updateGroup(Group group);

  /**
   * 根据name删除group
   *
   * @param name group's name
   */
  void deleteGroupByName(String name);

  /**
   * 判断对应的group是否存在
   *
   * @param group group实例
   * @return bool，true存在，false不存在
   */
  boolean isExist(Group group);

  /**
   * 查询所有的tag
   *
   * @return 返回所有的tag实例
   */
  List<Group> findAll();
}

