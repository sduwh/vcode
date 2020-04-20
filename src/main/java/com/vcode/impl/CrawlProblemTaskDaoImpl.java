package com.vcode.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcode.common.RedisCode;
import com.vcode.dao.CrawlProblemTaskDao;
import com.vcode.entity.CrawlProblemTask;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author moyee
 * @version 1.0.0
 * @Description CrawlProblemTaskDao 接口实现
 * @Date
 */
@Component
public class CrawlProblemTaskDaoImpl implements CrawlProblemTaskDao {

  private final MongoTemplate mongoTemplate;
  private final RedisTemplate<String, String> redisTemplate;

  @Autowired
  public CrawlProblemTaskDaoImpl(MongoTemplate mongoTemplate, RedisTemplate<String, String> redisTemplate) {
    this.mongoTemplate = mongoTemplate;
    this.redisTemplate = redisTemplate;
  }

  /**
   * 创建一个新的任务
   *
   * @param task task实例
   * @return 保存至mongo中的task数据
   * @throws JsonProcessingException json处理错误
   */
  @Override
  public CrawlProblemTask createTask(CrawlProblemTask task) throws JsonProcessingException {
    CrawlProblemTask savedTask = mongoTemplate.save(task);
    redisTemplate.opsForList().leftPush(RedisCode.TARGET_CRAWL_TOPIC, savedTask.toJsonString());
    return savedTask;
  }

  /**
   * 通过id的hex值删除任务
   *
   * @param idHex objectId hex
   */
  @Override
  public void deleteTask(String idHex) {
    Query query = new Query(Criteria.where("id").is(new ObjectId(idHex)));
    mongoTemplate.remove(query, CrawlProblemTask.class);
  }


  /**
   * 根据ObjectId的hex值查找task
   *
   * @param taskId objectId hex
   * @return task 实例
   */
  @Override
  public CrawlProblemTask findTaskByOriginTaskId(String taskId) {
    Query query = new Query(Criteria.where("task_id").is(taskId));
    return mongoTemplate.findOne(query, CrawlProblemTask.class);
  }

  /**
   * task 分页查询
   *
   * @param page   页码
   * @param size   页的大小
   * @param search 查询条件
   * @return 查询结果
   */
  @Override
  public List<CrawlProblemTask> findTaskList(int page, int size, String search) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "create_time"));
    Query query = new Query();
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("oj").regex(".*" + search + ".*"));
    }
    query.with(pageable);
    return mongoTemplate.find(query, CrawlProblemTask.class);
  }

  /**
   * 统计符合查询条件的task数量
   *
   * @param search 查询条件
   * @return 统计结果
   */
  @Override
  public long count(String search) {
    Query query = new Query();
    // 添加查询条件
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("oj").regex(".*" + search + ".*"));
    }
    return mongoTemplate.count(query, CrawlProblemTask.class);
  }

  /**
   * 根据task_id更新task结果
   *
   * @param crawlProblemTask 最新的task实例
   */
  @Override
  public void updateResult(CrawlProblemTask crawlProblemTask) {
    Update update = new Update()
            .set("result", crawlProblemTask.getResult())
            .set("message", crawlProblemTask.getMessage());
    Query query = new Query(Criteria.where("task_id").is(crawlProblemTask.getTaskId()));
    mongoTemplate.updateFirst(query, update, CrawlProblemTask.class);
  }
}
