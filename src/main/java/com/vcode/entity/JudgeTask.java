package com.vcode.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author moyee
 * @version 1.0.0
 * @Description 本地判题任务
 * @Date
 */
public class JudgeTask {
  private String origin;
  private String key;
  private String code;
  private String submitId;
  private String language;

  public JudgeTask() {
    this.origin = "";
    this.key = "";
    this.code = "";
    this.submitId = "";
    this.language = "";
  }

  public JudgeTask(Submission submission) {
    String problemOriginId = submission.getProblemOriginId();
    this.origin = problemOriginId.substring(0, problemOriginId.indexOf("-"));
    this.key = problemOriginId.substring(problemOriginId.indexOf("-") + 1);
    this.setCode(submission.getCode());
    this.language = submission.getLanguage();
    this.submitId = submission.getId().toHexString();
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getCode() {
    return new String(Base64.getDecoder().decode(this.code), StandardCharsets.UTF_8);
  }

  public void setCode(String code) {
    this.code = Base64.getEncoder().encodeToString(code.getBytes(StandardCharsets.UTF_8));
  }

  public String getSubmitId() {
    return submitId;
  }

  public void setSubmitId(String submitId) {
    this.submitId = submitId;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String toJsonString() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, String> map = new HashMap<>(5);
    map.put("origin", this.origin);
    map.put("key", this.key);
    map.put("code", this.code);
    map.put("submit_id", this.submitId);
    map.put("language", this.language);
    return objectMapper.writeValueAsString(map);
  }
}
