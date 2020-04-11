package com.vcode.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcode.Impl.ContestDaoImpl;
import com.vcode.Impl.SubmissionDaoImpl;
import com.vcode.Impl.VUserDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Contest;
import com.vcode.entity.Response;
import com.vcode.entity.Submission;
import com.vcode.entity.VUser;
import com.vcode.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/submission")
public class SubmissionController {

  private Logger log = Logger.getLogger("SubmissionController");

  private SubmissionDaoImpl submissionDao;


  private ContestDaoImpl contestDao;

  private VUserDaoImpl userDao;

  @Autowired
  public SubmissionController(SubmissionDaoImpl submissionDao,
                              ContestDaoImpl contestDao,
                              VUserDaoImpl userDao) {
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
      return response;
    }
    page--;
    try {
      List<Submission> submissionList = submissionDao.findSubmissions(page, size, search);
      HashMap<String, Object> resMap = new HashMap<>();
      resMap.put("submissionList", submissionList);
      resMap.put("total", submissionDao.count(search));
      response.setData(resMap);
      return response;
    } catch (Exception e) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("find error");
      response.setError(e.getMessage());
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
    Response res = new Response();
    if (submissionIdHex == null) {
      res.setCode(ResponseCode.ERROR);
      res.setMessage("originId is required");
      return res;
    }
    Submission submission = submissionDao.findByIdHex(submissionIdHex);
    if (submission == null) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("Submission is not exit");
      return res;
    }

    Map<String, Object> data = new HashMap<>();
    data.put("submission", submission);
    res.setData(data);
    return res;
  }

  /**
   * @param submission 的submission字段
   * @return com.vcode.entity.Response
   * @Description 创建一个新的Submission
   */
  @PostMapping()
  @RequiresAuthentication
  public Response uploadSubmission(@RequestBody @Valid Submission submission) {
    Response res = new Response();
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JWTUtil.getAccount(token);
    if ((submission.getContestName() != null && !submission.getContestName().equals("")) && !contestDao.isExist(submission.getContestName())) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("The contest is not exist");
      return res;
    }

    VUser user = userDao.findUserByUserAccount(account);
    submission.setUserAccount(account);
    submission.setNickname(user.getNickname());
    if (submissionDao.isExist(submission)) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("The submission is repeat");
      return res;
    }

    submission = submissionDao.fillInfo(submission);
    if (submission.getProblemTitle() == null) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("Problem is not exist");
      return res;
    }

    Submission savedSubmission = submissionDao.saveSubmission(submission);
    try {
      submissionDao.sendToJudgeQueue(savedSubmission);
    } catch (JsonProcessingException e) {
      res.setCode(ResponseCode.ERROR);
      res.setMessage("Parse submission error");
      return res;
    }

    Map<String, Object> data = new HashMap<>();
    data.put("submissionId", savedSubmission.getId().toHexString());
    res.setData(data);
    return res;
  }

  /**
   * @param map 用于获取problemOriginId
   * @return com.vcode.entity.Response
   * @Description 删除一个Submission
   */
  @DeleteMapping("/delete")
  public Response deleteSubmissionByOriginId(@RequestBody Map<String, String> map) {
    Response res = new Response();
    String id = map.get("problemOriginId");
    if (id == null) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("OriginId is required");
      return res;
    }
    submissionDao.deleteProblemByOriginId(id);
    return res;
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
    String account = JWTUtil.getAccount(token);

    List<Submission> submissionList = submissionDao.findSubmissions(problemOriginId, account);
    Map<String, Object> dataMap = new HashMap<>();
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
      response.setMessage("Contest is not exist");
      return response;
    }
    page--;
    List<Submission> submissionList = submissionDao.findContestSubmission(contest.getName(), page, size, search);
    Map<String, Object> dataMap = new HashMap<>();
    dataMap.put("submissionList", submissionList);
    dataMap.put("total", submissionDao.countContestSubmission(contest.getName(), search));
    response.setData(dataMap);
    return response;
  }
}
