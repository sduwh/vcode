package com.vcode.Impl;

import com.vcode.dao.SubmissionDao;
import com.vcode.entity.Problem;
import com.vcode.entity.Submission;
import com.vcode.entity.VUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubmissionDaoImpl implements SubmissionDao {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private VUserDaoImpl vUserDao;

  @Autowired
  private ProblemDaoImpl problemDao;

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
    Submission s = getUserLastSubmissionBySameProblem(submission.getUserAccount(), submission.getProblemOriginId());
    if (s == null) return false;
    // if submission's status is padding
    if (s.getResult() == 5) {
      System.out.println(s.getLanguage());
      System.out.println(submission.getLanguage());
      if (!s.getLanguage().equals(submission.getLanguage())) return false;


      return s.getCode().equals(submission.getCode());
    }

    return false;
  }

  @Override
  public List<Submission> findSubmissions(int page, int size, String search) {
    Pageable pageableRequest = PageRequest.of(page, size);
    Query query = new Query();
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("user_nickname").regex(".*" + search + ".*"));
    }
    query.with(pageableRequest);
    return mongoTemplate.find(query, Submission.class);
  }

  @Override
  public List<Submission> findProblemSubmissions(int page, int size, String originId) {
    Pageable pageableRequest = PageRequest.of(page, size);
    Query query = new Query();
    query.addCriteria(Criteria.where("problem_origin_id").is(originId));
    query.with(pageableRequest);
    return mongoTemplate.find(query, Submission.class);
  }

  @Override
  public List<Submission> findSubmissions(String originId, String account) {
    Query query = new Query();
    query.addCriteria(Criteria.where("problem_origin_id").is(originId).and("user_account").is(account));
    return mongoTemplate.find(query, Submission.class);
  }

  @Override
  public long count(String search) {
    Query query = new Query();
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("user_nickname").regex(".*" + search + ".*"));
    }
    return mongoTemplate.count(query, Submission.class);
  }

  @Override
  public long countSubmission(String problemOriginId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("problem_origin_id").is(problemOriginId));
    return mongoTemplate.count(query, Submission.class);
  }

  @Override
  public Submission fillInfo(Submission submission) {
    VUser user = vUserDao.findUserByUserAccount(submission.getUserAccount());
    submission.setNickname(user.getNickname());
    Problem problem = problemDao.findByOriginId(submission.getProblemOriginId());
    if (problem == null) {
      submission.setProblemTitle(null);
    } else {
      submission.setProblemTitle(problem.getTitle());
    }
    submission.setMemory("");
    submission.setTime("");
    return submission;
  }

  @Override
  public Submission getUserLastSubmissionBySameProblem(String account, String problemOriginId) {
    Query query = new Query(
            Criteria
                    .where("user_account").is(account)
                    .and("problem_origin_id").is(problemOriginId)
    ).with(Sort.by(Sort.Direction.DESC, "create_time"));
    return mongoTemplate.findOne(query, Submission.class);
  }
}
