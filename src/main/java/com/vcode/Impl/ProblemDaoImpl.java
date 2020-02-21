package com.vcode.Impl;

import com.vcode.dao.ProblemDao;
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

import java.util.List;

@Component
public class ProblemDaoImpl implements ProblemDao {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public void saveProblem(Problem problem) {
    mongoTemplate.save(problem);
  }

  @Override
  public Problem findByOriginId(String originId) {
    Query query = new Query(Criteria.where("origin_id").is(originId));
    return mongoTemplate.findOne(query, Problem.class);
  }

  @Override
  public void updateProblem(Problem problem) {
    // 只能编辑非爬虫获取的的题目
    if (problem.getOrigin().equals("vcode")) {
      Query query = new Query(Criteria.where("id").is(problem.getId()));
      Update update =problem.getUpdateData();
      mongoTemplate.updateFirst(query, update, Problem.class);
    }
  }

  @Override
  public void deleteProblemByOriginId(String originId) {
    Query query = new Query(Criteria.where("origin_id").is(originId));
    mongoTemplate.remove(query, Problem.class);
  }

  @Override
  public boolean isExist(Problem problem) {
    Problem p = findByOriginId(problem.getOriginId());
    return p != null;
  }

  @Override
  public List<Problem> findProblems(int page, int size, String search) {
    Pageable pageableRequest = PageRequest.of(page, size);
    Query query = new Query();
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("title").regex(".*" + search + ".*"));
    }
    query.with(pageableRequest);
    return mongoTemplate.find(query, Problem.class);
  }

  @Override
  public Long count(String search) {
    Query query = new Query();
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("title").regex(".*" + search + ".*"));
    }
    return mongoTemplate.count(query, Problem.class);
  }

  @Override
  public List<Problem> getAllProblems(List<ObjectId> problemIds) {
    Query query = new Query(Criteria.where("id").in(problemIds));
    return mongoTemplate.find(query, Problem.class);
  }
}
