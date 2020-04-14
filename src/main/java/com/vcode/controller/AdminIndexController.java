package com.vcode.controller;

import com.vcode.Impl.JudgeServerImpl;
import com.vcode.Impl.ProblemDaoImpl;
import com.vcode.Impl.SubmissionDaoImpl;
import com.vcode.Impl.VUserDaoImpl;
import com.vcode.entity.Response;
import com.vcode.shiro.ShiroCommon;
import com.vcode.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */
@RestController
@RequestMapping("/admin")
public class AdminIndexController {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private ProblemDaoImpl problemDao;
  private JudgeServerImpl judgeServerDao;
  private SubmissionDaoImpl submissionDao;
  private VUserDaoImpl userDao;

  @Autowired
  public AdminIndexController(ProblemDaoImpl problemDao,
                              JudgeServerImpl judgeServerDao,
                              SubmissionDaoImpl submissionDao,
                              VUserDaoImpl userDao) {
    this.problemDao = problemDao;
    this.judgeServerDao = judgeServerDao;
    this.submissionDao = submissionDao;
    this.userDao = userDao;
  }

  /**
   * @return com.vcode.entity.Response
   * @Description some data for admin index like problem's total and user's total
   * @Date 2020/4/11 11:10
   */
  @GetMapping("/index")
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response getAdminIndex() {
    Response response = new Response();

    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JWTUtil.getAccount(token);

    logger.info(String.format("user: %s has login admin", account));

    Map<String, Object> problemForm = new HashMap<>();
    List<String> ojList = judgeServerDao.getOJSupport();
    Long problemTotal = problemDao.count("", false);
    Long submissionTotal = submissionDao.count("");
    problemForm.put("ojList", ojList);
    problemForm.put("problemTotal", problemTotal);
    problemForm.put("submissionTotal", submissionTotal);

    Map<String, Object> userForm = new HashMap<>();
    Long userTotal = userDao.count("");
    userForm.put("userTotal", userTotal);

    Map<String, Object> data = new HashMap<>();
    data.put("problemForm", problemForm);
    data.put("userForm", userForm);
    response.setData(data);
    return response;
  }
}
