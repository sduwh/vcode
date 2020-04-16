package com.vcode.controller;

import com.vcode.handler.TestCaseHandler;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Response;
import com.vcode.shiro.ShiroCommon;
import com.vcode.util.JwtUtil;
import com.vcode.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author moyee
 */
@RestController
@RequestMapping("/test_case")
public class TestCaseController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
    // get test_case_id
    String testCaseId = StringUtil.generateRandomStr();
    String zipFileFullPath = "/tmp/" + testCaseId + ".zip";
    logger.info(String.format("testCaseId: %s, zipFileFullPath: %s", testCaseId, zipFileFullPath));
    // write file data
    try {
      byte[] bytes = file.getBytes();
      Path path = Paths.get(zipFileFullPath);
      Files.write(path, bytes);
    } catch (IOException e) {
      logger.warn("write test case data error");
      response.setCode(ResponseCode.ERROR);
      response.setError(e.toString());
      logger.error(String.format("user: %s upload test case: %s fail, write file fail: %s", account, testCaseId,
              e.toString()));
      return response;
    }
    String unzipResult = TestCaseHandler.processZipFile(zipFileFullPath, testCaseId, "/tmp");
    if (unzipResult == null) {
      HashMap<String, Object> resData = new HashMap<>(1);
      resData.put("testCaseId", testCaseId);
      response.setData(resData);
      logger.error(String.format("user: %s upload test case: %s success", account, testCaseId));
    } else {
      response.setCode(ResponseCode.FAIL);
      response.setMessage(unzipResult);
      logger.error(String.format("user: %s upload test case: %s fail, error: %s", account, testCaseId, unzipResult));
    }
    return response;
  }
}
