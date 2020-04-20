package com.vcode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcode.common.TaskResultCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author moyee
 * @version 1.0.0
 * @Description crawl problem task
 * @Date
 */
@Document("crawl_problem_task")
public class CrawlProblemTask {

  @JsonIgnore
  @Id
  private ObjectId id;

  @Field("task_id")
  private String taskId;

  @NotNull(message = "oj is required")
  @NotBlank(message = "oj is required")
  @NotEmpty(message = "oj is required")
  @Field("oj")
  private String oj;

  @NotNull(message = "key is required")
  @NotBlank(message = "key is required")
  @NotEmpty(message = "key is required")
  @Field("key")
  private String key;

  @Field("result")
  private String result;

  @Field("create_time")
  private long createTime;

  @Field("author")
  private String author;

  @Field("message")
  private String message;

  public CrawlProblemTask() {
    this.setTaskId();
    this.oj = "";
    this.key = "";
    this.result = TaskResultCode.CRAWLING;
    this.createTime = System.currentTimeMillis();
    this.author = "admin";
    this.message = "";
  }

  public String getHex() {
    return this.id.toHexString();
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public void setTaskId() {
    this.taskId = UUID.randomUUID().toString();
  }

  public String getOj() {
    return oj;
  }

  public void setOj(String oj) {
    this.oj = oj;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String toJsonString() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> map = new HashMap<>(3);
    map.put("oj", this.oj);
    map.put("key", this.key);
    map.put("task_id", this.taskId);
    return objectMapper.writeValueAsString(map);
  }
}
