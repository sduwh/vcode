package com.vcode.Impl;

import com.vcode.dao.ContestDao;
import com.vcode.entity.Contest;
import com.vcode.entity.Problem;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class ContestDaoImpl implements ContestDao {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public void saveContest(Contest contest) {
    mongoTemplate.save(contest);
  }

  @Override
  public Contest findByName(String Name) {
    Query query = new Query(Criteria.where("name").is(Name));
    return mongoTemplate.findOne(query, Contest.class);
  }

  @Override
  public void updateContest(Contest contest) {
    Query query = new Query(Criteria.where("name").is(contest.getName()));
    Update update = contest.getUpdateData();
    mongoTemplate.updateFirst(query, update, Contest.class);
  }

  @Override
  public void deleteContestByName(String Name) {
    Query query = new Query(Criteria.where("name").is(Name));
    mongoTemplate.remove(query, Contest.class);
  }

  @Override
  public List<Contest> findContests(int page, int size, String search) {
    Pageable pageableRequest = PageRequest.of(page, size);
    Query query = new Query();
    // 添加查询条件
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("name").regex(".*" + search + ".*"));
    }
    query.with(pageableRequest);
    return mongoTemplate.find(query, Contest.class);
  }

  @Override
  public boolean isExist(Contest contest) {
    Query query = new Query(Criteria.where("name").is(contest.getName()));
    Contest _contest = mongoTemplate.findOne(query, Contest.class);
    return _contest != null;
  }

  @Override
  public boolean isExist(String contestName) {
    Query query = new Query(Criteria.where("name").is(contestName));
    Contest _contest = mongoTemplate.findOne(query, Contest.class);
    return _contest != null;
  }

  @Override
  public Long count(String search) {
    Query query = new Query();
    // 添加查询条件
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("name").regex(".*" + search + ".*"));
    }
    return mongoTemplate.count(query, Contest.class);
  }


  @Override
  public void addProblem(Contest contest, Problem problem) {
    LinkedList<ObjectId> problemIds = contest.getProblems();
    if (!problemIds.contains(problem.getId())) {
      problemIds.add(problem.getId());
      Query query = new Query(Criteria.where("name").is(contest.getName()));
      Update update = new Update().set("problems", problemIds);
      mongoTemplate.updateFirst(query, update, Contest.class);
    }
  }

  @Override
  public void removeProblem(Contest contest, Problem problem) {
    LinkedList<ObjectId> problemIds = contest.getProblems();
    if (problemIds.contains(problem.getId())) {
      problemIds.remove(problem.getId());
      Query query = new Query(Criteria.where("name").is(contest.getName()));
      Update update = new Update().set("problems", problemIds);
      mongoTemplate.updateFirst(query, update, Contest.class);
    }
  }

  @Override
  public boolean checkPassword(String contestName, String password) {
    Query query = new Query(Criteria.where("name").is(contestName));
    Contest contest = mongoTemplate.findOne(query, Contest.class);
    if (contest == null) return false;
    return contest.checkPassword(password);
  }
}
