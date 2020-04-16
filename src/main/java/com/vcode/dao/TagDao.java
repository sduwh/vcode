package com.vcode.dao;

import com.vcode.entity.Tag;

import java.util.List;

/**
 * 标签实体的数据接口
 * @author moyee
 */
public interface TagDao {

  void saveTag(Tag tag);

  Tag findTagByName(String name);

  // 传入最新数据的实体即可
  void updateTag(Tag tag);

  void deleteTagByName(String name);

  boolean isExist(Tag tag);

  List<Tag> findAll();


}
