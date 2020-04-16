package com.vcode.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcode.impl.ProblemDaoImpl;
import com.vcode.entity.Problem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.vcode.common.RedisCode.PROBLEM_TOPIC;

/**
 * @author moyee
 * @version 1.0.0
 * @Description save problem in message queue which create by spider
 * @Date 2020/4/6
 */
@Component
public class ParseSpiderProblem {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final RedisTemplate<String, String> redisTemplate;

  private final ProblemDaoImpl problemDao;

  @Autowired
  public ParseSpiderProblem(RedisTemplate<String, String> redisTemplate, ProblemDaoImpl problemDao) {
    this.redisTemplate = redisTemplate;
    this.problemDao = problemDao;
  }

  /**
   * @Description 0.5s执行一次
   * @Date 2020/4/6 23:50
   */
  @Async("getSpiderProblemExecutor")
  @Scheduled(fixedRate = 500)
  public void parseProblemFromRedis() {
    String problemJsonStr = redisTemplate.opsForList().rightPop(PROBLEM_TOPIC);
    if (problemJsonStr != null) {
      try {
        Problem problem = Problem.createProblemByJson(problemJsonStr);
        problemDao.saveProblem(problem);
      } catch (JsonProcessingException e) {
        logger.error(e.toString());
      }
    }
  }
}
