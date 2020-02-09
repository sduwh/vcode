package com.vcode.dao;

import com.vcode.entity.Submission;
import org.bson.types.ObjectId;

public interface SubmissionDao {

  void saveSubmission(Submission submission);

  Submission findById(ObjectId Id);

  // 传入最新数据的实体即可
  void updateSubmission(Submission submission);

}
