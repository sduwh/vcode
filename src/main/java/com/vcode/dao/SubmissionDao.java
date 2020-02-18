package com.vcode.dao;

import com.vcode.entity.Problem;
import com.vcode.entity.Submission;
import org.bson.types.ObjectId;

public interface SubmissionDao {

  void saveSubmission(Submission submission);

  Submission findById(String problemOriginId);

  // 传入最新数据的实体即可
  void updateSubmission(Submission submission);

  void deleteProblemByOriginId(String problemOriginId);

  boolean isExist(Submission submission);
}
