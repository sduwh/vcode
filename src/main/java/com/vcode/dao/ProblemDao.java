package com.vcode.dao;


import com.vcode.entity.Problem;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.List;

/**
 * 问题实体的数据接口
 *
 * @author moyee
 */
public interface ProblemDao {

  /**
   * 统计当前条件下的题目数量
   *
   * @param search     查询条件
   * @param visible    题目是否可见
   * @param originType 题目来源 1：本地出题，2：爬虫获取，其他：所有来源
   * @return 题目数量
   */
  Long count(String search, boolean visible, int originType);

  /**
   * 保存含有新信息的problem
   *
   * @param problem problem实例
   */
  void saveProblem(Problem problem);

  /**
   * 根据problem的originId查询problem
   *
   * @param originId problem的originId
   * @return 存在返回problem否则返回null
   */
  Problem findByOriginId(String originId);

  /**
   * 保存含有新信息的problem
   *
   * @param problem problem实例
   * @return 错误信息，正常返回null
   * @throws IOException 测试用例操作出错
   */
  String updateProblem(Problem problem) throws IOException;

  /**
   * 根据originId删除problem
   *
   * @param originId problem的originId
   */
  void deleteProblemByOriginId(String originId);

  /**
   * problem是否存在
   *
   * @param problem problem实例
   * @return bool true存在，false不存在
   */
  boolean isExist(Problem problem);

  /**
   * problem是否存在
   *
   * @param originId problem的originId
   * @return bool true存在，false不存在
   */
  boolean isExist(String originId);


  /**
   * 查询当前下的所有题目
   *
   * @param page       页数
   * @param size       一页的容量
   * @param search     查询条件
   * @param visible    题目是否可见
   * @param originType 题目来源 1：本地出题，2：爬虫获取，其他：所有来源
   * @return 题目列表
   */
  List<Problem> findProblems(int page, int size, String search, boolean visible, int originType);

  /**
   * 查询多个problem Id
   *
   * @param problemIds 要查询的problem的所有id
   * @return 对应的problem列表
   */
  List<Problem> getAllProblems(List<ObjectId> problemIds);

  /**
   * 更新problem的可见状态
   *
   * @param originId problem的originId
   * @param visible  要更新的可见状态 true 可见，false不可见
   */
  void updateProblemVisible(String originId, boolean visible);

  /**
   * 增加problem的提交数
   *
   * @param originId problem的originId
   */
  void incSubmissionNum(String originId);

  /**
   * 增加problem的ac数
   *
   * @param originId problem的originId
   */
  void incAcceptNum(String originId);

}
