package com.vcode.dao;

import com.vcode.entity.Tag;

/**
 * 标签实体的数据接口
 */

public interface TagDao {

  void saveTag(Tag tag);

  Tag findUserByUserName(String name);

  // 传入最新数据的实体即可
  void updateTag(Tag tag);

  void deleteTagByName(String name);

}
