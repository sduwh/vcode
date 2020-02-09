package com.vcode.Impl;

import com.vcode.dao.SubmissionDao;
import com.vcode.entity.Submission;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class SubmissionDaoImpl implements SubmissionDao {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public void saveSubmission(Submission submission) {
    mongoTemplate.save(submission);
  }

  @Override
  public Submission findById(ObjectId Id) {
    Query query = new Query(Criteria.where("id").is(Id));
    return mongoTemplate.findOne(query, Submission.class);
  }

  @Override
  public void updateSubmission(Submission submission) {
    Query query = new Query(Criteria.where("id").is(submission.getId()));
    Update update = new Update().push("$set", submission.getUpdateData());
    mongoTemplate.updateFirst(query, update, Submission.class);
  }
}
