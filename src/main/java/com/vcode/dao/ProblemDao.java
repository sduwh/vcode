package com.vcode.dao;

import com.vcode.entity.Problem;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * 问题实体的数据接口
 */
public interface ProblemDao {

  Long count(String search);

  void saveProblem(Problem problem);

  Problem findByOriginId(String originId);

  // 传入最新数据的实体即可
  String updateProblem(Problem problem);

  void deleteProblemByOriginId(String originId);

  boolean isExist(Problem problem);

  boolean isExist(String originId);

  List<Problem> findProblems(int page, int size, String search, boolean visible);

  List<Problem> getAllProblems(List<ObjectId> problemIds);

  void updateProblemVisible(String originId, boolean visible);

  void incSubmissionNum(String originId);

  void incAcceptNum(String originId);

}
