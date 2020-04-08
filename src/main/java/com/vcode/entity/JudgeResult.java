package com.vcode.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */
public class JudgeResult {
  private String submitId;
  private String result;
  private String timeUsed;
  private String memoryUsed;
  private String info;

  public JudgeResult(String jsonInfo) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    HashMap map = objectMapper.readValue(jsonInfo, HashMap.class);
    this.submitId = (String) map.get("submit_id");
    this.result = (String) map.get("result");
    this.timeUsed = (String) map.get("time_used");
    this.memoryUsed = (String) map.get("memory_used");
    this.setInfo((String) map.get("info"));
  }

  public String getSubmitId() {
    return submitId;
  }

  public void setSubmitId(String submitId) {
    this.submitId = submitId;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getTimeUsed() {
    return timeUsed;
  }

  public void setTimeUsed(String timeUsed) {
    this.timeUsed = timeUsed;
  }

  public String getMemoryUsed() {
    return memoryUsed;
  }

  public void setMemoryUsed(String memoryUsed) {
    this.memoryUsed = memoryUsed;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = new String(Base64.getDecoder().decode(info), StandardCharsets.UTF_8);
  }
}
