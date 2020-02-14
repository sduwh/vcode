package com.vcode.Impl;

import com.vcode.dao.ContestDao;
import com.vcode.entity.Contest;
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
    Query query = new Query(Criteria.where("id").is(contest.getId()));
    Update update = contest.getUpdateData();
    mongoTemplate.updateFirst(query, update, Contest.class);
  }

  @Override
  public void deleteContestByName(String Name) {
    Query query = new Query(Criteria.where("name").is(Name));
    mongoTemplate.remove(query, Contest.class);
  }

  @Override
  public List<Contest> findContestsByPageAndSize(int page, int size) {
    Pageable pageableRequest = PageRequest.of(page, size);
    Query query = new Query();
    query.with(pageableRequest);
    return mongoTemplate.find(query, Contest.class);
  }

  @Override
  public boolean isExist(String contestName) {
    Query query = new Query(Criteria.where("name").is(contestName));
    Contest contest = mongoTemplate.findOne(query, Contest.class);
    return contest != null;
  }

  @Override
  public Long count() {
    return mongoTemplate.count(new Query(), Contest.class);
  }
}
