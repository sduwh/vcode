package com.vcode.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcode.common.MongoCode;
import com.vcode.common.RedisCode;
import com.vcode.common.SubmissionResultCode;
import com.vcode.dao.SubmissionDao;
import com.vcode.entity.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component
public class SubmissionDaoImpl implements SubmissionDao {

  private final MongoTemplate mongoTemplate;
  private final UserDaoImpl vUserDao;
  private final ProblemDaoImpl problemDao;
  private final RedisTemplate<String, String> redisTemplate;
  private final RankDaoImpl rankDao;

  @Autowired
  public SubmissionDaoImpl(MongoTemplate mongoTemplate,
                           UserDaoImpl vUserDao,
                           ProblemDaoImpl problemDao,
                           RedisTemplate<String, String> redisTemplate,
                           RankDaoImpl rankDao) {
    this.mongoTemplate = mongoTemplate;
    this.vUserDao = vUserDao;
    this.problemDao = problemDao;
    this.redisTemplate = redisTemplate;
    this.rankDao = rankDao;
  }

  @Override
  public Submission saveSubmission(Submission submission) {
    // inc problem's submission number
    problemDao.incSubmissionNum(submission.getProblemOriginId());
    return mongoTemplate.save(submission);
  }

  @Override
  public Submission findById(ObjectId objectId) {
    Query query = new Query(Criteria.where("id").is(objectId));
    return mongoTemplate.findOne(query, Submission.class);
  }

  @Override
  public Submission findByIdHex(String hex) {
    ObjectId objectId = new ObjectId(hex);
    return findById(objectId);
  }

  @Override
  public void updateSubmission(Submission submission) throws InterruptedException {
    // submission need objectId
    if (submission.getResult() == 1) {
      // inc problem's accept number
      problemDao.incAcceptNum(submission.getProblemOriginId());
    }
    mongoTemplate.save(submission);
    rankDao.updateRank(submission);
  }

  @Override
  public void deleteSubmissionByHexId(String submissionHexId) {
    Query query = new Query(Criteria.where("id").is(new ObjectId(submissionHexId)));
    mongoTemplate.remove(query, Submission.class);
  }

  @Override
  public boolean isExist(Submission submission) {
    Submission s = getUserLastSubmissionBySameProblem(submission.getUserAccount(), submission.getProblemOriginId());
    if (s == null) {
      return false;
    }
    // if submission's status is padding
    if (s.getResult() == SubmissionResultCode.PADDING) {
      if (!s.getContestName().equals(submission.getContestName())) {
        return false;
      }
      if (!s.getLanguage().equals(submission.getLanguage())) {
        return false;
      }
      return s.getCode().equals(submission.getCode());
    }
    return false;
  }

  @Override
  public List<Submission> findSubmissions(int page, int size, String search) {
    Pageable pageableRequest = PageRequest.of(page, size);
    Query query = new Query(Criteria.where("contest_name").is(""));
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
  public List<Submission> findContestSubmission(String contestName, int page, int size, String search) {
    Query query = new Query(Criteria.where("contest_name").is(contestName));
    Pageable pageableRequest = PageRequest.of(page, size);
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("user_nickname").regex(".*" + search + ".*"));
    }
    query.with(pageableRequest);
    return mongoTemplate.find(query, Submission.class);
  }

  @Override
  public long count(String search) {
    Query query = new Query(Criteria.where("contest_name").is(""));
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("user_nickname").regex(".*" + search + ".*"));
    }
    return mongoTemplate.count(query, Submission.class);
  }

  @Override
  public long countSubmission(String problemOriginId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("problem_origin_id").is(problemOriginId).and("contest_name").is(null));
    return mongoTemplate.count(query, Submission.class);
  }

  @Override
  public long countContestSubmission(String contestName, String search) {
    Query query = new Query();
    query.addCriteria(Criteria.where("contest_name").is(contestName));
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("user_nickname").regex(".*" + search + ".*"));
    }
    return mongoTemplate.count(query, Submission.class);
  }

  @Override
  public Submission fillInfo(Submission submission) {
    User user = vUserDao.findUserByUserAccount(submission.getUserAccount());
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

  @Override
  public void sendToJudgeQueue(Submission submission) throws JsonProcessingException {
    if (submission.getProblemOriginId().startsWith(MongoCode.VCODE)) {
      JudgeTask judgeTask = new JudgeTask(submission);
      String task = judgeTask.toJsonString();
      redisTemplate.opsForList().leftPush(RedisCode.JUDGE_TASK_TOPIC, task);
    } else {
      RemoteJudgeTask remoteJudgeTask = new RemoteJudgeTask(submission);
      String task = remoteJudgeTask.toJsonString();
      redisTemplate.opsForList().leftPush(RedisCode.JUDGE_REMOTE_TASK_TOPIC, task);
    }
  }

  /**
   * 返回判题等待时间超过一分钟的Submission
   *
   * @return 返回SubmissionList
   */
  @Override
  public List<Submission> findJudgingSubmissionList() {
    // 获得一分钟之前的时间戳
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MINUTE, -1);
    long beforeTime = calendar.getTimeInMillis();
    Query query =
            new Query(Criteria.where("create_time").lt(beforeTime).and("result").is(SubmissionResultCode.PADDING));
    return mongoTemplate.find(query, Submission.class);
  }
}
