package com.vcode.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcode.common.SubmissionResultCode;

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
    this.submitId = (String) map.get("task_id");
    this.result = (String) map.get("status");
    this.timeUsed = Integer.toString((Integer) map.get("time_used"));
    this.memoryUsed = Integer.toString((Integer) map.get("memory_used"));
    if (SubmissionResultCode.resultStrToInt(this.result) == SubmissionResultCode.COMPILE_ERROR) {
      this.setInfo((String) map.get("compile_error"));
    } else {
      this.info = this.result;
    }
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
    this.info = info;
  }
}
