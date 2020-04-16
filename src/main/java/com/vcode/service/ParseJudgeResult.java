package com.vcode.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcode.impl.SubmissionDaoImpl;
import com.vcode.common.RedisCode;
import com.vcode.entity.JudgeResult;
import com.vcode.entity.Submission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */
@Component
public class ParseJudgeResult {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final RedisTemplate<String, String> redisTemplate;
  private final SubmissionDaoImpl submissionDao;

  @Autowired
  public ParseJudgeResult(RedisTemplate<String, String> redisTemplate, SubmissionDaoImpl submissionDao) {
    this.redisTemplate = redisTemplate;
    this.submissionDao = submissionDao;
  }

  /**
   * @Description 0.5s执行一次
   * @Date 2020/4/5 18:05
   */
  @Async("getJudgeResultExecutor")
  @Scheduled(fixedRate = 500)
  public void parseProblemFromRedis() {
    String judgeResult = redisTemplate.opsForList().rightPop(RedisCode.JUDGE_RESULT_TOPIC);
    if (judgeResult != null) {
      try {
        JudgeResult result = new JudgeResult(judgeResult);
        Submission submission = submissionDao.findByIdHex(result.getSubmitId());
        if (submission != null) {
          submission.setResult(result.getResult());
          submission.setTime(result.getTimeUsed());
          submission.setMemory(result.getMemoryUsed());
          submission.setResultMessage(result.getInfo());
          submissionDao.updateSubmission(submission);
          logger.info(String.format("update submission: %s success", submission.getId().toHexString()));
        }
      } catch (JsonProcessingException | InterruptedException e) {
        logger.error(e.toString());
      }
    }
  }
}
