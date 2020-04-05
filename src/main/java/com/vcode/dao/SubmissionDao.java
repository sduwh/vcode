package com.vcode.dao;

import com.vcode.entity.Submission;

import java.util.List;

public interface SubmissionDao {

  void saveSubmission(Submission submission);

  Submission findById(String problemOriginId);

  // 传入最新数据的实体即可
  void updateSubmission(Submission submission);

  void deleteProblemByOriginId(String problemOriginId);

  boolean isExist(Submission submission);

  List<Submission> findSubmissions(int page, int size, String search);

  List<Submission> findSubmissions(String originId, String account);

  List<Submission> findProblemSubmissions(int page, int size, String originId);

  List<Submission> findContestSubmission(String contestName, int page, int size, String search);

  long count(String search);

  long countSubmission(String problemOriginId);

  long countContestSubmission(String contestName, String search);

  Submission fillInfo(Submission submission);

  Submission getUserLastSubmissionBySameProblem(String account, String problemOriginId);
}
