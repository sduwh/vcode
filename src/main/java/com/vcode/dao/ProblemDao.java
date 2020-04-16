package com.vcode.dao;

import com.vcode.entity.Problem;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.List;

/**
 * 问题实体的数据接口
 * @author moyee
 */
public interface ProblemDao {

  Long count(String search, boolean visible);

  void saveProblem(Problem problem);

  Problem findByOriginId(String originId);

  String updateProblem(Problem problem) throws IOException;

  void deleteProblemByOriginId(String originId);

  boolean isExist(Problem problem);

  boolean isExist(String originId);

  List<Problem> findProblems(int page, int size, String search, boolean visible);

  List<Problem> getAllProblems(List<ObjectId> problemIds);

  void updateProblemVisible(String originId, boolean visible);

  void incSubmissionNum(String originId);

  void incAcceptNum(String originId);

}
