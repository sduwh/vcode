package com.vcode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * @author moyee
 * @version 1.0.0
 * @Description submission rank table
 * @Date 03/29/2020
 */
@Document("rank")
public class Rank {
  @JsonIgnore
  @Id
  private ObjectId id;

  @Field(value = "create_time")
  private long createTime;

  @Field(value = "user_account")
  private String userAccount;

  @Field(value = "username")
  private String username;

  @Field(value = "contest_name")
  private String contestName;

  @Field(value = "start_time")
  private long startTime;

  @Field(value = "ac_num")
  private int acNum;

  @Field(value = "wrong_num")
  private int wrongNum;

  @Field(value = "problem_origin_id")
  private String problemOriginId;

  @Field(value = "used_time")
  private int usedTime;

  @Field(value = "is_earliest")
  private boolean isEarliest;

  public Rank() {
  }

  public Rank(String userAccount, String username, String contestName, long startTime, String problemOriginId) {
    this.createTime = System.currentTimeMillis();
    this.userAccount = userAccount;
    this.username = username;
    this.contestName = contestName;
    this.startTime = startTime;
    this.problemOriginId = problemOriginId;
    this.acNum = 0;
    this.wrongNum = 0;
    this.usedTime = 0;
    this.isEarliest = false;
  }

  public long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }

  public String getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(String userAccount) {
    this.userAccount = userAccount;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getContestName() {
    return contestName;
  }

  public void setContestName(String contestName) {
    this.contestName = contestName;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public int getAcNum() {
    return acNum;
  }

  public void setAcNum(int acNum) {
    this.acNum = acNum;
  }

  public int getWrongNum() {
    return wrongNum;
  }

  public void setWrongNum(int wrongNum) {
    this.wrongNum = wrongNum;
  }

  public String getProblemOriginId() {
    return problemOriginId;
  }

  public void setProblemOriginId(String problemOriginId) {
    this.problemOriginId = problemOriginId;
  }

  public int getUsedTime() {
    return usedTime;
  }

  public void setUsedTime(int usedTime) {
    this.usedTime = usedTime;
  }

  public boolean isEarliest() {
    return isEarliest;
  }

  public void setEarliest(boolean earliest) {
    isEarliest = earliest;
  }
}
