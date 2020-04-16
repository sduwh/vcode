package com.vcode.controller;

import com.vcode.impl.ContestDaoImpl;
import com.vcode.impl.ProblemDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Contest;
import com.vcode.entity.Problem;
import com.vcode.entity.Response;
import com.vcode.shiro.ShiroCommon;
import com.vcode.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author moyee
 */
@RestController
@RequestMapping("/contest")
public class ContestController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final ContestDaoImpl contestDao;
  private final ProblemDaoImpl problemDao;

  @Autowired
  public ContestController(ContestDaoImpl contestDao, ProblemDaoImpl problemDao) {
    this.contestDao = contestDao;
    this.problemDao = problemDao;
  }

  /**
   * @param page   页码
   * @param size   一页的容量
   * @param search 查询条件
   * @return com.vcode.entity.Response
   * @Description 获取contest列表
   * @Date 2020/2/11 15:33
   */
  @GetMapping("/list")
  public Response getContestsList(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                  @RequestParam(value = "search") String search) {
    Response response = new Response();
    if (page < 1 || size < 1) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("page or size must be greater than zero");
      logger.debug(response.getMessage());
      return response;
    }
    page--;
    HashMap<String, Object> resMap = new HashMap<>(2);
    List<Contest> contestList = contestDao.findContests(page, size, search);
    resMap.put("contestList", contestList);
    resMap.put("total", contestDao.count(search));
    response.setData(resMap);
    return response;
  }

  /**
   * @param contestName 1
   * @return com.vcode.entity.Response
   * @Description 获取Contest的详细信息
   * @Date 2020/2/11 16:13
   */
  @GetMapping("/detail")
  @RequiresAuthentication
  public Response getContestDetail(@RequestParam(value = "contestName") String contestName) {
    Response response = new Response();
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);
    if (contestName == null || contestName.length() == 0) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("name is required");
      logger.debug("the params name is required");
      return response;
    }
    Contest contest = contestDao.findByName(contestName);
    if (contest == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage(String.format("contest: %s is not exist", contestName));
      logger.debug(response.getMessage());
      return response;
    }
    logger.info(String.format("user: %s has access the Contest: %s", account, contestName));
    response.setData(contest);
    return response;
  }

  /**
   * @param contest contest的相关信息
   * @return com.vcode.entity.Response
   * @Description 创建一个新的contest
   * @Date 2020/2/11 16:24
   */
  @PostMapping("/create")
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response createContest(@RequestBody @Valid Contest contest) {
    Response response = new Response();
    if (contestDao.isExist(contest)) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage(String.format("The contest: %s is exist", contest.getName()));
      logger.debug(response.getMessage());
      return response;
    }
    contestDao.saveContest(contest);
    logger.info(String.format("The contest: %s is created", contest.getName()));
    return response;
  }

  /**
   * @param contest contest的相关信息
   * @return com.vcode.entity.Response
   * @Description 编辑contest的信息
   * @Date 2020/2/11 16:24
   */
  @PostMapping("/edit")
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response editContest(@RequestBody @Valid Contest contest) {
    Response response = new Response();
    if (contestDao.isExist(contest)) {
      contestDao.updateContest(contest);
      logger.info(String.format("The contest: %s is updated", contest.getUpdateData()));
      return response;
    }
    response.setCode(ResponseCode.FAIL);
    response.setMessage(String.format("The contest: %s is exist", contest.getName()));
    logger.debug(response.getMessage());
    return response;
  }

  /**
   * @param map 用于获取contestName
   * @return com.vcode.entity.Response
   * @Description 删除contest
   * @Date 2020/2/11 16:25
   */
  @PostMapping("/delete")
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response delContest(@RequestBody Map<String, String> map) {
    Response response = new Response();
    String contestName = map.get("name");
    if (contestName == null || contestName.length() == 0) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("name is required");
      logger.debug("the params name is required");
      return response;
    }
    contestDao.deleteContestByName(contestName);
    logger.warn(String.format("The contest: %s is deleted", contestName));
    return response;
  }

  /**
   * @param name contestName
   * @return com.vcode.entity.Response
   * @Description get problems of contest
   * @Date 2020/4/11 11:04
   */
  @GetMapping("/problems")
  @RequiresAuthentication
  public Response problems(@RequestParam(value = "contestTitle") String name) {
    Response response = new Response();
    Contest contest = contestDao.findByName(name);
    if (contest == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage(String.format("The contest: %s is not exist", name));
      logger.debug(response.getMessage());
      return response;
    }
    LinkedList<ObjectId> problemIds = contest.getProblems();
    List<Problem> problems = problemDao.getAllProblems(problemIds);
    HashMap<String, Object> map = new HashMap<>(1);
    map.put("problems", problems);
    response.setData(map);
    return response;
  }


  /**
   * @param map params
   * @return com.vcode.entity.Response
   * @Description add problem to contest
   * @Date 2020/4/11 11:04
   */
  @PostMapping("/problem")
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response addProblem(@RequestBody Map<String, String> map) {
    Response response = new Response();
    // get params
    String problemOriginId = map.get("problemOriginId");
    String contestTitle = map.get("contestTitle");
    if (contestTitle == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("contestTitle is required");
      logger.debug("the params contestTitle is required");
      return response;
    }
    if (problemOriginId == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("problemOriginId is required");
      logger.debug("the params problemOriginId is required");
      return response;
    }

    // find Object
    Contest contest = contestDao.findByName(contestTitle);
    if (contest == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage(String.format("The contest: %s is not exist", contestTitle));
      logger.debug(response.getMessage());
      return response;
    }

    Problem problem = problemDao.findByOriginId(problemOriginId);
    if (problem == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage(String.format("The problem: %s is not exist", problemOriginId));
      logger.debug(response.getMessage());
      return response;
    }

    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);

    contestDao.addProblem(contest, problem);
    logger.info(
            String.format("user: %s has added one problem: %s to contest: %s", account, problemOriginId, contestTitle));
    return response;
  }

  /**
   * @param problemOriginId problem origin id
   * @param contestTitle    contest'name
   * @return com.vcode.entity.Response
   * @Description delete problem form contest
   * @Date 2020/4/11 11:05
   */
  @DeleteMapping("/problem")
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response removeProblem(@RequestParam(value = "problemOriginId") String problemOriginId,
                                @RequestParam(value = "contestTitle") String contestTitle) {
    Response response = new Response();
    // find Object
    Contest contest = contestDao.findByName(contestTitle);
    if (contest == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage(String.format("The contest: %s is not exist", contestTitle));
      logger.debug(response.getMessage());
      return response;
    }
    Problem problem = problemDao.findByOriginId(problemOriginId);
    if (problem == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage(String.format("The problem: %s is not exist", problemOriginId));
      logger.debug(response.getMessage());
      return response;
    }

    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);

    contestDao.removeProblem(contest, problem);
    logger.warn(
            String.format("user: %s has deleted the problem: %s from contest: %s",
                    account,
                    problemOriginId,
                    contestTitle));
    return response;
  }

  /**
   * @param contestName contest' name
   * @param password    password that user input
   * @return com.vcode.entity.Response
   * @Description Check the password that the user input
   * @Date 2020/4/11 11:06
   */
  @GetMapping("/password")
  @RequiresAuthentication
  public Response checkPassword(@RequestParam(value = "contestName") String contestName,
                                @RequestParam(value = "password") String password) {
    Response response = new Response();
    Map<String, Object> data = new HashMap<>(1);
    data.put("result", contestDao.checkPassword(contestName, password));
    response.setData(data);
    return response;
  }
}
