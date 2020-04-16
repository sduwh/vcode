package com.vcode.dao;

import java.util.List;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */
public interface JudgeServerDao {
  /**
   * 获取爬虫所支持的OJ
   *
   * @return Oj列表
   */
  List<String> getOjSupport();
}
