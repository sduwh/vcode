package com.vcode.dao;

import com.vcode.entity.Contest;
import com.vcode.entity.Problem;

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

  List<Contest> findContests(int page, int size, String search);

  boolean isExist(Contest contest);

  boolean isExist(String contestName);

  Long count(String search);

  void addProblem(Contest contest, Problem problem);

  void removeProblem(Contest contest, Problem problem);

  boolean checkPassword(String contestName, String password);
}
