package com.vcode.dao;

import com.vcode.entity.Tag;

import java.util.List;

/**
 * 标签实体的数据接口
 *
 * @author moyee
 */
public interface TagDao {

  /**
   * 保存tag
   *
   * @param tag tag实例
   */
  void saveTag(Tag tag);

  /**
   * 查询tag
   *
   * @param name tag's name
   * @return 查询的结果tag实例，不存在返回null
   */
  Tag findTagByName(String name);

  /**
   * 更新tag
   *
   * @param tag tag实例
   */
  void updateTag(Tag tag);

  /**
   * 根据name删除tag
   *
   * @param name tag's name
   */
  void deleteTagByName(String name);

  /**
   * 判断对应的tag是否存在
   *
   * @param tag tag实例
   * @return bool，true存在，false不存在
   */
  boolean isExist(Tag tag);

  /**
   * 查询所有的tag
   *
   * @return 返回所有的tag实例
   */
  List<Tag> findAll();
}
