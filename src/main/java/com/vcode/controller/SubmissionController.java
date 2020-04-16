package com.vcode.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcode.impl.ContestDaoImpl;
import com.vcode.impl.SubmissionDaoImpl;
import com.vcode.impl.UserDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Contest;
import com.vcode.entity.Response;
import com.vcode.entity.Submission;
import com.vcode.entity.User;
import com.vcode.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author moyee
 */
@RestController
@RequestMapping("/submission")
public class SubmissionController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final SubmissionDaoImpl submissionDao;
  private final ContestDaoImpl contestDao;
  private final UserDaoImpl userDao;

  @Autowired
  public SubmissionController(SubmissionDaoImpl submissionDao,
                              ContestDaoImpl contestDao,
                              UserDaoImpl userDao) {
    this.submissionDao = submissionDao;
    this.contestDao = contestDao;
    this.userDao = userDao;
  }

  /**
   * @param page   page number
   * @param size   page size
   * @param search search key words
   * @return com.vcode.entity.Response
   * @Description get submission list
   * @Date 2020/3/19 14:30
   */
  @GetMapping()
  public Response getSubmissions(@RequestParam(value = "page") int page,
                                 @RequestParam(value = "size") int size,
                                 @RequestParam(value = "search") String search) {
    Response response = new Response();
    if (page < 1 || size < 1) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("page or size must be greater than zero");
      logger.debug(response.getMessage());
      return response;
    }
    page--;
    try {
      List<Submission> submissionList = submissionDao.findSubmissions(page, size, search);
      HashMap<String, Object> resMap = new HashMap<>(2);
      resMap.put("submissionList", submissionList);
      resMap.put("total", submissionDao.count(search));
      response.setData(resMap);
      return response;
    } catch (Exception e) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("Find error");
      response.setError(e.toString());
      logger.error(String.format("page: %d, size: %d, search: %s, error: %s", page, size, search, e.toString()));
      return response;
    }
  }

  /**
   * @param submissionIdHex submission的唯一标示
   * @return com.vcode.entity.Response
   * @Description 获取Submission的详情
   */
  @GetMapping("/detail")
  @RequiresAuthentication
  public Response getSubmissionDetail(@RequestParam(value = "submissionIdHex") String submissionIdHex) {
    Response response = new Response();
    if (submissionIdHex == null) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("The params originId is required");
      logger.debug(response.getMessage());
      return response;
    }
    Submission submission = submissionDao.findByIdHex(submissionIdHex);
    if (submission == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage(String.format("The submission: %s is not exist", submissionIdHex));
      logger.debug(response.getMessage());
      return response;
    }

    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);

    logger.info(String.format("user: %s has accessed the submission: %s", account, submissionIdHex));

    Map<String, Object> data = new HashMap<>(1);
    data.put("submission", submission);
    response.setData(data);
    return response;
  }

  /**
   * @param submission 的submission字段
   * @return com.vcode.entity.Response
   * @Description 创建一个新的Submission
   */
  @PostMapping()
  @RequiresAuthentication
  public Response uploadSubmission(@RequestBody @Valid Submission submission) {
    Response response = new Response();
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);
    if ((submission.getContestName() == null || !contestDao.isExist(submission.getContestName()))) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage(String.format("The contest: %s is not exist", submission.getContestName()));
      logger.debug(response.getMessage());
      return response;
    }

    User user = userDao.findUserByUserAccount(account);
    submission.setUserAccount(account);
    submission.setNickname(user.getNickname());
    if (submissionDao.isExist(submission)) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("The submission is repeat");
      logger.debug(response.getMessage());
      return response;
    }

    submission = submissionDao.fillInfo(submission);
    if (submission.getProblemTitle() == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("Problem is not exist");
      logger.debug(response.getMessage());
      return response;
    }

    Submission savedSubmission = submissionDao.saveSubmission(submission);
    try {
      submissionDao.sendToJudgeQueue(savedSubmission);
    } catch (JsonProcessingException e) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("Parse submission error");
      response.setError(e.toString());
      logger.error(String.format("Parse submission error: %s", e.toString()));
      return response;
    }
    logger.info(String.format("user: %s upload submission: %s success", account,
            savedSubmission.getId().toHexString()));
    Map<String, Object> data = new HashMap<>(1);
    data.put("submissionId", savedSubmission.getId().toHexString());
    response.setData(data);
    return response;
  }

  /**
   * @param submissionHexId 用于获取submission
   * @return com.vcode.entity.Response
   * @Description 删除一个Submission
   */
  @DeleteMapping("/delete")
  @RequiresAuthentication
  public Response deleteSubmissionByHexId(@RequestParam(value = "submissionHexId") String submissionHexId) {
    Response response = new Response();
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);
    if (submissionHexId == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("The params originId is required");
      logger.debug(String.format("user: %s, error: %s", account, response.getMessage()));
      return response;
    }
    logger.warn(String.format("user: %s has delete the submission: %s", account, submissionHexId));
    submissionDao.deleteSubmissionByHexId(submissionHexId);
    return response;
  }

  /**
   * @param problemOriginId problem id
   * @return com.vcode.entity.Response
   * @Description get user's submission of one problem
   * @Date 2020/3/27 17:35
   */
  @GetMapping("/problem/user")
  @RequiresAuthentication
  public Response getUserSubmissionForProblem(@RequestParam(value = "originId") String problemOriginId) {
    Response res = new Response();
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);

    List<Submission> submissionList = submissionDao.findSubmissions(problemOriginId, account);
    logger.info(String.format("user: %s has access the submission list of problem: %s", account, problemOriginId));
    Map<String, Object> dataMap = new HashMap<>(1);
    dataMap.put("submissionList", submissionList);
    res.setData(dataMap);
    return res;
  }

  /**
   * @param contestTitle contestTitle String
   * @param page         page int
   * @param size         size int
   * @param search       search data String
   * @return com.vcode.entity.Response
   * @Description
   * @Date 2020/3/27 15:54
   */
  @GetMapping("/contest")
  @RequiresAuthentication
  public Response getSubmissionForContest(@RequestParam(value = "contestTitle") String contestTitle,
                                          @RequestParam(value = "page") int page,
                                          @RequestParam(value = "size") int size,
                                          @RequestParam(value = "search") String search) {
    Response response = new Response();
    Contest contest = contestDao.findByName(contestTitle);
    if (contest == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage(String.format("Contest: %s is not exist", contestTitle));
      logger.debug(response.getMessage());
      return response;
    }
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);
    page--;
    List<Submission> submissionList = submissionDao.findContestSubmission(contest.getName(), page, size, search);
    logger.info(String.format("user: %s has access the submission list of contest: %s, page: %d, size: %d, search: " +
            "%s", account, contestTitle, page, size, search));
    Map<String, Object> dataMap = new HashMap<>(2);
    dataMap.put("submissionList", submissionList);
    dataMap.put("total", submissionDao.countContestSubmission(contest.getName(), search));
    response.setData(dataMap);
    return response;
  }
}
