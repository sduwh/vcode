package com.vcode.dao;

import com.vcode.entity.Contest;

/**
 * 操作题组的实体类的接口
 */
public interface ContestDao {

  void saveContest(Contest contest);

  Contest findByName(String Name);

  // 传入最新数据的实体即可
  void updateContest(Contest contest);

  void DeleteContestByName(String Name);

}
