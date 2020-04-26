package com.vcode.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcode.entity.Submission;
import com.vcode.impl.SubmissionDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author moyee
 * @version 1.0.0
 * @Description 对长时间为得到结果的submission尝试重新判题
 * @Date
 */
@Component
public class ParseJudgingSubmission {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final SubmissionDaoImpl submissionDao;

  @Autowired
  public ParseJudgingSubmission(SubmissionDaoImpl submissionDao) {
    this.submissionDao = submissionDao;
  }

  @Async
  @Scheduled(fixedRate = 10000)
  public void parseJudgingSubmission() {
    List<Submission> submissionList = submissionDao.findJudgingSubmissionList();
    for (Submission submission : submissionList) {
      try {
        submissionDao.sendToJudgeQueue(submission);
      } catch (JsonProcessingException e) {
        logger.error(String.format("Parse submission:%s error: %s", submission.getId().toHexString(), e.toString()));
      }
    }
  }
}
