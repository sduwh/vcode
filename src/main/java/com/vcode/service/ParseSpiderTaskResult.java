package com.vcode.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcode.common.RedisCode;
import com.vcode.common.TaskResultCode;
import com.vcode.entity.CrawlProblemTask;
import com.vcode.impl.CrawlProblemTaskDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */
@Component
public class ParseSpiderTaskResult {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final CrawlProblemTaskDaoImpl crawlProblemTaskDao;
  private final RedisTemplate<String, String> redisTemplate;

  @Autowired
  public ParseSpiderTaskResult(CrawlProblemTaskDaoImpl crawlProblemTaskDao,
                               RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.crawlProblemTaskDao = crawlProblemTaskDao;
  }

  @Async("getSpiderProblemTaskResultExecutor")
  @Scheduled(fixedRate = 500)
  public void parseProblemTaskResult() {
    String taskResultJson = redisTemplate.opsForList().rightPop(RedisCode.TARGET_CRAWL_RESULT_TOPIC);
    if (taskResultJson != null) {
      ObjectMapper mapper = new ObjectMapper();
      HashMap map;
      try {
        map = mapper.readValue(taskResultJson, HashMap.class);
      } catch (JsonProcessingException e) {
        logger.error(String.format("parse json info from redis error: %s", e.getMessage()));
        return;
      }
      String result = (String) map.get("result");
      String taskId = (String) map.get("task_id");
      String message = (String) map.get("message");
      CrawlProblemTask task = crawlProblemTaskDao.findTaskByOriginTaskId(taskId);
      if (task != null) {
        if (TaskResultCode.SUCCESS.equals(result)) {
          task.setResult(TaskResultCode.SUCCESS);
        } else {
          task.setResult(TaskResultCode.FAIL);
        }
        task.setMessage(message);
        crawlProblemTaskDao.updateResult(task);
        logger.info(String.format("Crawl target problem task: %s finish, result: %s, message: %s",
                task.getTaskId(),
                task.getResult(),
                task.getMessage()));
      }
    }
  }
}
