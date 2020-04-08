package com.vcode.Impl;

import com.vcode.common.RedisCode;
import com.vcode.dao.RankDao;
import com.vcode.entity.Contest;
import com.vcode.entity.Rank;
import com.vcode.entity.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
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
  private ContestDaoImpl contestDao;
  private RedisLockRegistry redisLockRegistry;

  @Autowired
  public RankDaoImpl(MongoTemplate mongoTemplate,
                     RedisLockRegistry redisLockRegistry,
                     ContestDaoImpl contestDao) {
    this.mongoTemplate = mongoTemplate;
    this.redisLockRegistry = redisLockRegistry;
    this.contestDao = contestDao;
  }

  @Override
  public List<Rank> getContestRankData(String contestName) {
    Query query = new Query(Criteria.where("contest_name").is(contestName));
    return mongoTemplate.find(query, Rank.class);
  }

  @Override
  public List<HashMap> getGlobalRankData() {
    Criteria criteria = Criteria.where("contest_name").is("");
    List<Sort.Order> sortOrderList = new LinkedList<>();
    sortOrderList.add(new Sort.Order(Sort.Direction.DESC, "acNum"));
    sortOrderList.add(new Sort.Order(Sort.Direction.ASC, "wrongNum"));
    Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(criteria),
            Aggregation.group("userAccount", "username").sum("acNum").as("acNum").sum("wrongNum").as("wrongNum"),
            Aggregation.sort(Sort.by(sortOrderList)));
    AggregationResults<HashMap> results = mongoTemplate.aggregate(aggregation, Rank.class, HashMap.class);
    return results.getMappedResults();
  }

  @Override
  public void saveRank(Rank rank) throws InterruptedException {
    Query query;
    if (rank.getContestName() == null || rank.getContestName().equals("")) {
      query = new Query(Criteria.where("problem_origin_id").is(rank.getProblemOriginId()));
    } else {
      query = new Query(Criteria.where("contest_name").is(rank.getContestName()).and("problem_origin_id")
              .is(rank.getProblemOriginId()));
    }
    Lock lock = redisLockRegistry.obtain(RedisCode.RANK_LOCK);

    lock.tryLock(500, TimeUnit.MILLISECONDS);
    List<Rank> rankList = mongoTemplate.find(query, Rank.class);
    boolean flag = true;
    for (Rank value : rankList) {
      if (value.getAcNum() > rank.getAcNum()
              || (value.getAcNum() == rank.getAcNum() && value.getUsedTime() > rank.getUsedTime())) {
        value.setEarliest(false);
        mongoTemplate.save(value);
        flag = false;
        break;
      }
    }
    if (flag)
      rank.setEarliest(true);
    mongoTemplate.save(rank);
    lock.unlock();
  }

  @Override
  public Rank find(String userAccount, String contestName, String problemOriginId) {
    Query query = new Query(Criteria
            .where("user_account").is(userAccount)
            .and("contest_name").is(contestName)
            .and("problem_origin_id").is(problemOriginId)
    );
    return mongoTemplate.findOne(query, Rank.class);
  }

  @Override
  public void updateRank(Submission submission) throws InterruptedException {
    Lock lock = redisLockRegistry.obtain(RedisCode.RANK_LOCK);
    lock.tryLock(500, TimeUnit.MILLISECONDS);
    Rank rank = find(submission.getUserAccount(), submission.getContestName(), submission.getProblemOriginId());
    if (rank == null) {
      if (submission.getContestName() == null || submission.getContestName().equals("")) {
        rank = new Rank(
                submission.getUserAccount(),
                submission.getNickname(),
                submission.getContestName(),
                0,
                submission.getProblemOriginId()
        );
      } else {
        Contest contest = contestDao.findByName(submission.getContestName());
        rank = new Rank(
                submission.getUserAccount(),
                submission.getNickname(),
                submission.getContestName(),
                contest.getStartTime().getTime(),
                submission.getProblemOriginId()
        );
      }
    }
    if (submission.getResult() == 1) {
      rank.setAcNum(rank.getAcNum() + 1);
      if (submission.getContestName() != null && !submission.getContestName().equals("") && rank.getAcNum() == 0) {
        rank.setUsedTime(rank.getUsedTime() + (System.currentTimeMillis() - rank.getStartTime()));
      }
    } else {
      rank.setWrongNum(rank.getWrongNum() + 1);
      if (submission.getContestName() != null && !submission.getContestName().equals("")) {
        // Time penalty, 20 minute
        rank.setUsedTime(rank.getUsedTime() + 20 * 60 * 1000);
      }
    }
    saveRank(rank);
    lock.unlock();
  }
}
