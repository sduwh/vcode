package com.vcode.controller;

import com.vcode.common.ResponseCode;
import com.vcode.entity.Response;
import com.vcode.handler.TestCaseHandler;
import com.vcode.shiro.ShiroCommon;
import com.vcode.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author moyee
 */
@RestController
@RequestMapping("/test_case")
public class TestCaseController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final TestCaseHandler testCaseHandler;

  @Autowired
  public TestCaseController(TestCaseHandler testCaseHandler) {
    this.testCaseHandler = testCaseHandler;
  }

  /**
   * @param file testcase.zip
   * @return testCaseId(filename)
   * @Description upload test-case file (zip)
   * @Date 2020/2/24 12:37
   */
  @PostMapping("/upload")
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response uploadTestCase(@RequestParam("file") MultipartFile file) {
    Response response = new Response();
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);

    // check file is exist
    if (file.isEmpty()) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("please select one file to upload");
      logger.debug(response.getMessage());
      return response;
    }
    // send case file to vcode judge
    boolean uploadSuccess;
    String testCaseId = new ObjectId().toHexString();
    try {
      uploadSuccess = testCaseHandler.caseReadyHandler(file, testCaseId);
    } catch (IOException e) {
      e.printStackTrace();
      response.setCode(ResponseCode.ERROR);
      response.setError(e.toString());
      response.setMessage(String.format("up load file error: %s", e.getMessage()));
      logger.error(response.getMessage());
      return response;
    }
    if (uploadSuccess) {
      HashMap<String, Object> resData = new HashMap<>(1);
      resData.put("testCaseId", testCaseId);
      response.setMessage("upload success");
      response.setData(resData);
      logger.info(String.format("user: %s upload case file: %s success", account, testCaseId));
    } else {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("upload fail");
      logger.info(String.format("user: %s upload case file: %s fail", account, testCaseId));
    }
    return response;
  }
}
