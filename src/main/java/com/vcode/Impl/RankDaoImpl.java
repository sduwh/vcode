package com.vcode.Impl;

import com.vcode.common.RedisCode;
import com.vcode.dao.RankDao;
import com.vcode.entity.Rank;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */
@Component
public class RankDaoImpl implements RankDao {

  private MongoTemplate mongoTemplate;

  private RedisLockRegistry redisLockRegistry;

  @Autowired
  public RankDaoImpl(MongoTemplate mongoTemplate, RedisLockRegistry redisLockRegistry) {
    this.mongoTemplate = mongoTemplate;
    this.redisLockRegistry = redisLockRegistry;
  }

  @Override
  public List<Rank> getContestRankData(String contestName) {
    Query query = new Query(Criteria.where("contest_name").is(contestName));
    return mongoTemplate.find(query, Rank.class);
  }

  @Override
  public List<Rank> getGlobalRankData() {
    Query query = new Query(Criteria.where("contest_name").is(null));
    return mongoTemplate.find(query, Rank.class);
  }

  @Override
  public void saveRank(Rank rank) throws InterruptedException {
    Query query;
    if (rank.getContestName() == null || rank.getContestName().equals("")) {
      query = new Query(Criteria.where("problem_origin_id").is(rank.getProblemOriginId()));
    } else {
      query = new Query(Criteria.where("contest_name").is(rank.getContestName()).and("problem_origin_id").is(rank.getProblemOriginId()));
    }
    Lock lock = redisLockRegistry.obtain(RedisCode.RankLock);

    lock.tryLock(500, TimeUnit.MILLISECONDS);
    List<Rank> rankList = mongoTemplate.find(query, Rank.class);
    boolean flag = true;
    for (Rank value : rankList) {
      if (value.getAcNum() > rank.getAcNum() || (value.getAcNum() == rank.getAcNum() && value.getUsedTime() > rank.getUsedTime())) {
        value.setEarliest(false);
        mongoTemplate.save(value);
        flag = false;
        break;
      }
    }
    if (flag) rank.setEarliest(true);
    mongoTemplate.save(rank);
    lock.unlock();
  }

  public void save(Rank rank) {
    mongoTemplate.save(rank);
  }
}
