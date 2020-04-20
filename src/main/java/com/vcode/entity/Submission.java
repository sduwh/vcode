package com.vcode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vcode.common.SubmissionResultCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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

  /**
   * 提交时间戳
   */
  @Field("create_time")
  private long createTime;

  @Field("user_account")
  private String userAccount;

  @Field("user_nickname")
  private String nickname;

  @Field("problem_title")
  private String problemTitle;

  @Field("problem_origin_id")
  @NotNull(message = "problemOriginId is required")
  @NotBlank(message = "problemOriginId is required")
  @NotEmpty(message = "problemOriginId is required")
  private String problemOriginId;

  /**
   * 代码耗时
   */
  @Field("time")
  private String time;

  @Field("memory")
  private String memory;

  @Field("language")
  @NotNull(message = "language is required")
  @NotBlank(message = "language is required")
  @NotEmpty(message = "language is required")
  private String language;

  @Field("code")
  @NotNull(message = "code is required")
  @NotBlank(message = "code is required")
  @NotEmpty(message = "code is required")
  private String code;

  @NotNull(message = "contest_name is required")
  @Field("contest_name")
  private String contestName;

  /**
   * 0 => wrong; 1 => success; 2 => time_out; 3 => memory_out; 4 => unknown_error; 5 => padding; 6 => compile error
   */
  @Field("result")
  private int result;

  @Field("result_message")
  private String resultMessage;

  public Submission() {
    this.userAccount = "";
    this.nickname = "";
    this.problemTitle = "";
    this.problemOriginId = "";
    this.language = "";
    this.code = "";

    this.createTime = System.currentTimeMillis();
    this.result = SubmissionResultCode.PADDING;
    this.time = "";
    this.memory = "";
    this.contestName = "";
    this.resultMessage = "";
  }

  public Submission(String userAccount,
                    String nickname,
                    String problemTitle,
                    String problemOriginId,
                    String language,
                    String code,
                    String resultMessage) {
    this.userAccount = userAccount;
    this.nickname = nickname;
    this.problemTitle = problemTitle;
    this.problemOriginId = problemOriginId;
    this.language = language;
    this.code = code;
    this.resultMessage = resultMessage;

    this.createTime = System.currentTimeMillis();
    this.result = SubmissionResultCode.PADDING;
    this.time = "";
    this.memory = "";
    this.contestName = "";
  }

  public ObjectId getId() {
    return id;
  }

  public long getCreateTime() {
    return createTime;
  }

  public String getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(String userAccount) {
    this.userAccount = userAccount;
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
    this.result = SubmissionResultCode.resultStrToInt(result);
  }

  public String getContestName() {
    return contestName;
  }

  public void setContestName(String contestName) {
    this.contestName = contestName;
  }

  public String getHex() {
    return this.id.toHexString();
  }

  public String getResultMessage() {
    return resultMessage;
  }

  public void setResultMessage(String resultMessage) {
    this.resultMessage = resultMessage;
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
