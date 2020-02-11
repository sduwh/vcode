package com.vcode.dao;

import com.vcode.entity.Contest;

import java.util.List;

/**
 * 操作题组的实体类的接口
 */
public interface ContestDao {

  void saveContest(Contest contest);

  Contest findByName(String Name);

  // 传入最新数据的实体即可
  void updateContest(Contest contest);

  void deleteContestByName(String Name);

  List<Contest> findContestsByPageAndSize(int page, int size);

  boolean isExist(String contestName);
}
