package com.vcode.dao;

import com.vcode.entity.Problem;

import java.util.List;

/**
 * 问题实体的数据接口
 */
public interface ProblemDao {

  void saveProblem(Problem problem);

  Problem findByOriginId(String originId);

  // 传入最新数据的实体即可
  void updateProblem(Problem problem);

  void deleteProblemByOriginId(String originId);

  boolean isExist(Problem problem);

  List<Problem> findProblemsByPageAndSize(int page, int size);

  Long count();

}
