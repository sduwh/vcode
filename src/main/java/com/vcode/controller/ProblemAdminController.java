package com.vcode.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcode.handler.TestCaseHandler;
import com.vcode.impl.ProblemDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Problem;
import com.vcode.entity.Response;
import com.vcode.shiro.ShiroCommon;
import com.vcode.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

/**
 * @author moyee
 */
@RestController
@RequestMapping("/admin/problem")
public class ProblemAdminController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final ProblemDaoImpl problemDao;
  private final TestCaseHandler testCaseHandler;

  @Autowired
  public ProblemAdminController(ProblemDaoImpl problemDao, TestCaseHandler testCaseHandler) {
    this.problemDao = problemDao;
    this.testCaseHandler = testCaseHandler;
  }

  /**
   * @param problem problem的字段
   * @return com.vcode.entity.Response
   * @Description 创建problem的信息
   * @Date 2020/2/11 15:36
   */
  @PostMapping("/create")
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response createProblem(@RequestBody @Valid Problem problem) {
    Response response = new Response();
    // check is this problem exist
    if (problemDao.isExist(problem)) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("ID or Title is repeat");
      logger.debug(response.getMessage());
      return response;
    }
    String testCaseId = problem.getTestCaseId();
    boolean checkResult;
    try {
      checkResult = testCaseHandler.caseCheckHandler(testCaseId, "");
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      response.setCode(ResponseCode.ERROR);
      response.setError(e.toString());
      response.setMessage(String.format("check the case file fail: %s", e.getMessage()));
      logger.error(response.getMessage());
      return response;
    }
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);

    if (checkResult) {
      problemDao.saveProblem(problem);
      logger.info(String.format("user: %s create problem: origin_id: %s, title: %s", account, problem.getOrigin(),
              problem.getTitle()));
      response.setData(problem);
    } else {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("Please retry upload case file");
    }
    return response;
  }

  /**
   * @param problem problem的字段
   * @return com.vcode.entity.Response
   * @Description 编辑problem的信息
   * @Date 2020/2/11 15:35
   */
  @PostMapping("/edit")
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response editProblem(@RequestBody @Valid Problem problem) {
    Response res = new Response();
    if (!problem.getOriginId().startsWith(problem.getOrigin() + Problem.ORIGIN_ID_SPLIT_KEY)) {
      problem.setOriginId(problem.getOriginId());
    }
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);
    try {
      String err = problemDao.updateProblem(problem);
      if (err == null) {
        logger.info(String.format("user: %s update the problem: %s", account, problem.getOriginId()));
        return res;
      }
    } catch (IOException e) {
      logger.error(e.toString());
    }
    res.setCode(ResponseCode.ERROR);
    res.setMessage("update error");
    logger.debug(res.getMessage());
    return res;

  }

  /**
   * @param problemOriginId problemOriginId
   * @return com.vcode.entity.Response
   * @Description 根据OriginId删除problem
   * @Date 2020/2/11 15:36
   */
  @DeleteMapping("/delete")
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response deleteProblemByOriginId(@RequestParam(value = "originId") String problemOriginId) {
    Response res = new Response();
    if (problemOriginId == null) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("The params originId is required");
      logger.debug(res.getMessage());
      return res;
    }
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);
    problemDao.deleteProblemByOriginId(problemOriginId);
    logger.warn(String.format("user: %s has deleted the problem: %s", account, problemOriginId));
    return res;
  }

  /**
   * @param map get originId and visible by map
   * @return com.vcode.entity.Response
   * @Description update problem's visible
   * @Date 2020/2/25 14:57
   */
  @PostMapping("/visible")
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response changeVisible(@RequestBody Map<String, String> map) {
    Response res = new Response();
    String problemOriginId = map.get("originId");
    String visibleStr = map.get("visible");
    if (problemOriginId == null || visibleStr == null) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("The params originId and visible are required");
      logger.debug(res.getMessage());
      return res;
    }
    if (!problemDao.isExist(problemOriginId)) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("The problem is not exist");
      logger.debug(res.getMessage());
      return res;
    }
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);
    boolean visible = Boolean.parseBoolean(visibleStr);
    problemDao.updateProblemVisible(problemOriginId, visible);
    logger.info(String.format("user: %s has changed the visible of problem: %s", account, problemOriginId));
    return res;
  }
}
