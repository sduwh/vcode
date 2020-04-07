package com.vcode.service;

import com.vcode.Impl.ProblemDaoImpl;
import com.vcode.entity.Problem;
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

  private RedisTemplate<String, String> redisTemplate;

  private ProblemDaoImpl problemDao;

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
      Problem problem = Problem.createProblemByJson(problemJsonStr);
      if (problem != null)
        problemDao.saveProblem(problem);
    }
  }
}
