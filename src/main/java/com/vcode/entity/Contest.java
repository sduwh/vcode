package com.vcode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedList;

/**
 * @author moyee
 * @version 1.0.0
 * @Description 标签实体
 * @return Tag实例
 * @since 1.0.0
 */
@Document("contest")
public class Contest {

  @JsonIgnore
  @Id
  private ObjectId id;

  @NotEmpty(message = "name不能为空")
  @NotBlank(message = "name不能为空格字符串")
  @NotNull(message = "name不能为null")
  @Field("name")
  private String name;

  @NotNull(message = "always不能为null")
  @Field("always")
  private boolean always; // true => 永久开放；false => 限时开放；

  @NotNull(message = "start_time不能为null")
  @Field("start_time")
  private Date startTime;

  @NotNull(message = "end_time不能为null")
  @Field("end_time")
  private Date endTime;

  @NotEmpty(message = "owner_account不能为空")
  @NotBlank(message = "owner_account不能为空格字符串")
  @NotNull(message = "owner_account不能为null")
  @Field("owner_account")
  private String ownerAccount;

  @NotEmpty(message = "contestType不能为空")
  @NotBlank(message = "contestType不能为空格字符串")
  @NotNull(message = "contestType不能为null")
  @Field("contest_type")
  private String contestType; // nomal, password


  @Field("password")
  private String password;

  @Field("problems")
  private LinkedList<ObjectId> problems;  // 存放所有此tag下的问题ObjectId

  public Contest(String name,
                 boolean always,
                 Date startTime,
                 Date endTime,
                 String ownerAccount,
                 String contestType,
                 String password) {
    this.name = name;
    this.always = always;
    this.startTime = startTime;
    this.endTime = endTime;
    this.ownerAccount = ownerAccount;
    this.problems = new LinkedList<>();
    this.contestType = contestType;
    this.password = password;
  }

  public ObjectId getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isAlways() {
    return always;
  }

  public void setAlways(boolean always) {
    this.always = always;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public String getOwnerAccount() {
    return ownerAccount;
  }

  public void setOwnerAccount(String ownerAccount) {
    this.ownerAccount = ownerAccount;
  }

  public boolean checkOwner(String owner_account) {
    return this.ownerAccount.equals(owner_account);
  }

  public LinkedList<ObjectId> getProblems() {
    return problems;
  }

  public String getContestType() {
    return contestType;
  }

  public void setContestType(String contestType) {
    this.contestType = contestType;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Update getUpdateData() {
    Update update = new Update();
    update.set("name", this.getName())
            .set("always", this.isAlways())
            .set("startTime", this.getStartTime())
            .set("endTime", this.getEndTime())
            .set("contest_type", this.getContestType())
            .set("password", this.getPassword());
    return update;
  }
}
