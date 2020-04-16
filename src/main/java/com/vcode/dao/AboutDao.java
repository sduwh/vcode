package com.vcode.dao;

import com.vcode.entity.About;

/**
 * @author moyee
 */
public interface AboutDao {

  /**
   * 更新about
   *
   * @param about about实例对象
   */
  void updateAbout(About about);

  /**
   * 获取数据库中的about
   *
   * @return About 实例对象
   */
  About getAbout();


  /**
   * 返回数据库中是否存在about数据
   *
   * @return 布尔值，true存在，false不存在
   */
  boolean isExist();
}
