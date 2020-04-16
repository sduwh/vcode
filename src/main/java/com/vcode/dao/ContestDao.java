package com.vcode.dao;

import com.vcode.entity.Contest;
import com.vcode.entity.Problem;

import java.util.List;

/**
 * 操作题组的实体类的接口
 *
 * @author moyee
 */
public interface ContestDao {

  /**
   * 保存contest信息
   *
   * @param contest contest实例对象
   */
  void saveContest(Contest contest);

  /**
   * 通过name值查询contest
   *
   * @param name contest的name
   * @return 对应的contest，不存在返回null
   */
  Contest findByName(String name);

  /**
   * 跟新contest数据
   *
   * @param contest 含有新信息的contest实例
   */
  void updateContest(Contest contest);

  /**
   * 通过contest's name 删除contest
   *
   * @param name contest's name
   */
  void deleteContestByName(String name);

  /**
   * 查询contest列表
   *
   * @param page   页码
   * @param size   一页含量
   * @param search 查询条件
   * @return 对应查询条件的contest列表
   */
  List<Contest> findContests(int page, int size, String search);

  /**
   * 判断contest是否存在
   *
   * @param contest contest实例
   * @return bool true存在，false不存在
   */
  boolean isExist(Contest contest);

  /**
   * 判断contest是否存在
   *
   * @param contestName contest's name
   * @return bool true存在，false不存在
   */
  boolean isExist(String contestName);

  /**
   * 统计查询条件下的contest的数量
   *
   * @param search 查询条件
   * @return 对应的contest的数量
   */
  Long count(String search);

  /**
   * 添加problem至contest
   *
   * @param contest 要添加的目标contest实例，
   * @param problem 要添加的problem
   */
  void addProblem(Contest contest, Problem problem);

  /**
   * 从contest中移除problem
   *
   * @param contest 目标contest
   * @param problem 目标problem
   */
  void removeProblem(Contest contest, Problem problem);

  /**
   * 检查密码是否正确
   *
   * @param contestName contest's name
   * @param password    密码
   * @return boot true密码正确，false不正确
   */
  boolean checkPassword(String contestName, String password);
}
