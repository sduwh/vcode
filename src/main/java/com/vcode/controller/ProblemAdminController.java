package com.vcode.controller;

import com.vcode.handler.TestCaseHandler;
import com.vcode.impl.ProblemDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.config.TestCaseConfig;
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
  private final TestCaseConfig testCaseConfig;

  @Autowired
  public ProblemAdminController(ProblemDaoImpl problemDao, TestCaseConfig testCaseConfig) {
    this.problemDao = problemDao;
    this.testCaseConfig = testCaseConfig;
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
    Response res = new Response();
    // check is this problem exist
    if (problemDao.isExist(problem)) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("ID or Title is repeat");
      logger.debug(res.getMessage());
      return res;
    }
    // check testCaseId
    String fileFullPath = "/tmp/" + problem.getTestCaseId() + ".zip";
    String targetDir = problem.getTestCaseId();
    if (!TestCaseHandler.isZipExist(fileFullPath)) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("Test Case is need to upload");
      logger.debug(res.getMessage());
      return res;
    }
    try {
      // process testCaseFile: mv the file to target dir
      TestCaseHandler.moveTestCaseFiles(testCaseConfig.getPath(), targetDir);
    } catch (IOException e) {
      logger.error(e.toString());
      res.setCode(ResponseCode.ERROR);
      return res;
    }
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);

    problemDao.saveProblem(problem);
    logger.info(String.format("user: %s create problem: origin_id: %s, title: %s", account, problem.getOrigin(),
            problem.getTitle()));
    res.setData(problem);
    return res;
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
    if (!problem.getOriginId().startsWith(problem.getOrigin() + "-")) {
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
