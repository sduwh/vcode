package com.vcode.Impl;

import com.vcode.dao.SubmissionDao;
import com.vcode.entity.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class SubmissionDaoImpl implements SubmissionDao {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public void saveSubmission(Submission submission) {
    mongoTemplate.save(submission);
  }

  @Override
  public Submission findById(String problemOriginId) {
    Query query = new Query(Criteria.where("id").is(problemOriginId));
    return mongoTemplate.findOne(query, Submission.class);
  }

  @Override
  public void updateSubmission(Submission submission) {
    Query query = new Query(Criteria.where("id").is(submission.getProblemOriginId()));
    Update update = submission.getUpdateData();
    mongoTemplate.updateFirst(query, update, Submission.class);
  }

  @Override
  public void deleteProblemByOriginId(String problemOriginId) {
    Query query = new Query(Criteria.where("problemOriginId").is(problemOriginId));
    mongoTemplate.remove(query, Submission.class);
  }

  @Override
  public boolean isExist(Submission submission) {
    Submission s = findById(submission.getProblemOriginId());
    return s != null;
  }
}
