package com.vcode.dao;

import com.vcode.entity.Problem;

/**
 * 问题实体的数据接口
 */
public interface ProblemDao {

  void saveProblem(Problem problem);

  Problem findByOriginId(String originId);

  // 传入最新数据的实体即可
  void updateProblem(Problem problem);

  void DeleteProblemByOriginId(String originId);

}
