package com.vcode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vcode.common.SubmissionResultCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;

/**
 * @author moyee
 * @version 1.0.0
 * @Description 标签实体
 * @return Tag实例
 * @since 1.0.0
 */
@Document("submission")
public class Submission {
  @JsonIgnore
  @Id
  private ObjectId id;

  @Field("create_time")
  private long createTime; // 提交时间戳

  @Field("user_account")
  private String user_account;

  @Field("user_nickname")
  private String nickname;

  @Field("problem_title")
  private String problemTitle;

  @Field("problem_origin_id")
  private String problemOriginId;

  @Field("time")
  private String time; // 代码耗时

  @Field("memory")
  private String memory;

  @Field("language")
  private String language;

  @Field("code")
  private String code;

  @Field("result")
  private int result; // 0 => wrong; 1 => success; 2 => time_out; 3 => memory_out; 4 => unknown_error; 5 => padding

  public Submission() {
    this.createTime = System.currentTimeMillis();
    this.result = SubmissionResultCode.PADDING;
    this.time = null;
    this.memory = null;
  }

  public Submission(String user_account,
                    String nickname,
                    String problemTitle,
                    String problemOriginId,
                    String language,
                    String code) {
    this.user_account = user_account;
    this.nickname = nickname;
    this.problemTitle = problemTitle;
    this.problemOriginId = problemOriginId;
    this.language = language;
    this.code = code;

    this.createTime = System.currentTimeMillis();
    this.result = SubmissionResultCode.PADDING;
    this.time = null;
    this.memory = null;
  }

  public ObjectId getId() {
    return id;
  }

  public long getCreateTime() {
    return createTime;
  }

  public String getUser_account() {
    return user_account;
  }

  public void setUser_account(String user_account) {
    this.user_account = user_account;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getProblemTitle() {
    return problemTitle;
  }

  public void setProblemTitle(String problemTitle) {
    this.problemTitle = problemTitle;
  }

  public String getProblemOriginId() {
    return problemOriginId;
  }

  public void setProblemOriginId(String problemOriginId) {
    this.problemOriginId = problemOriginId;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getMemory() {
    return memory;
  }

  public void setMemory(String memory) {
    this.memory = memory;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public int getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = SubmissionResultCode.ResultStrToInt(result);
  }

  @JsonIgnore
  public Update getUpdateData() {
    Update update = new Update();
    update.set("time", this.getTime())
            .set("memory", this.getMemory())
            .set("result", this.getResult());
    return update;
  }
}