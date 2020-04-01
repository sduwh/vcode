package com.vcode.Impl;

import com.vcode.dao.RankDao;
import com.vcode.entity.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */
@Component
public class RankDaoImpl implements RankDao {

  private MongoTemplate mongoTemplate;

  @Autowired
  public RankDaoImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public List<Rank> getContestRankData(String contestName) {
    Query query = new Query(Criteria.where("contestName").is(contestName));
    return mongoTemplate.find(query, Rank.class);
  }

  @Override
  public List<Rank> getGlobalRankData() {
    Query query = new Query(Criteria.where("contestName").is(null));
    return mongoTemplate.find(query, Rank.class);
  }

  @Override
  public void saveRank(Rank rank) {

  }

  public void save(Rank rank) {
    mongoTemplate.save(rank);
  }
}
