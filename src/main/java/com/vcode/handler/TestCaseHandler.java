package com.vcode.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcode.common.ResponseCode;
import com.vcode.config.JudgeConfig;
import com.vcode.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


/**
 * @author yaorui
 */
@Component
public class TestCaseHandler {
  private final JudgeConfig judgeConfig;
  private final RestTemplate restTemplate;

  @Autowired
  public TestCaseHandler(JudgeConfig judgeConfig, RestTemplate restTemplate) {
    this.judgeConfig = judgeConfig;
    this.restTemplate = restTemplate;
  }

  public boolean caseReadyHandler(MultipartFile file, String testCaseId) throws IOException {
    File uploadFile = new File("/tmp/" + StringUtil.generateRandomStr() + ".zip");
    // create zip file in /tmp
    FileUtils.copyInputStreamToFile(file.getInputStream(), uploadFile);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
    MultiValueMap<String, Object> uploadData = new LinkedMultiValueMap<>();
    uploadData.add("testCaseId", testCaseId);
    uploadData.add("file", new FileSystemResource(uploadFile));
    HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(uploadData, httpHeaders);
    ResponseEntity<String> responseEntity =
            restTemplate.postForEntity(
                    judgeConfig.caseReadyApi(),
                    request,
                    String.class);
    ObjectMapper mapper = new ObjectMapper();
    HashMap resMap = mapper.readValue(responseEntity.getBody(), HashMap.class);
    int code = (int) resMap.get("code");
    return code == 1;
  }

  public boolean caseCheckHandler(String testCaseId, String oldTestCaseId) throws JsonProcessingException {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, String> httpData = new LinkedMultiValueMap<>();
    httpData.add("testCaseId", testCaseId);
    httpData.add("oldTestCaseId", oldTestCaseId);
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(httpData, httpHeaders);
    ResponseEntity<String> responseEntity =
            restTemplate.postForEntity(
                    judgeConfig.caseCheckApi(),
                    request,
                    String.class);
    ObjectMapper mapper = new ObjectMapper();
    HashMap resMap = mapper.readValue(responseEntity.getBody(), HashMap.class);
    int checkResult = (int) resMap.get("code");
    return checkResult == 1;
  }
}
