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

  /**
   * true => 永久开放；false => 限时开放；
   */
  @NotNull(message = "always不能为null")
  @Field("always")
  private boolean always;

  @Field("start_time")
  private Date startTime;

  @Field("end_time")
  private Date endTime;

  @NotEmpty(message = "owner_account不能为空")
  @NotBlank(message = "owner_account不能为空格字符串")
  @NotNull(message = "owner_account不能为null")
  @Field("owner_account")
  private String ownerAccount;

  /**
   * true => lock
   */
  @NotNull(message = "contestType不能为null")
  @Field("is_lock")
  private Boolean isLock;


  @Field("password")
  private String password;

  /**
   * // 存放所有此tag下的问题ObjectId
   */
  @JsonIgnore
  @Field("problems")
  private LinkedList<ObjectId> problems;

  public Contest(String name,
                 boolean always,
                 Date startTime,
                 Date endTime,
                 String ownerAccount,
                 boolean isLock,
                 String password) {
    this.name = name;
    this.always = always;
    this.startTime = startTime;
    this.endTime = endTime;
    this.ownerAccount = ownerAccount;
    this.problems = new LinkedList<>();
    this.isLock = isLock;
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

  public boolean checkOwner(String ownerAccount) {
    return this.ownerAccount.equals(ownerAccount);
  }

  public LinkedList<ObjectId> getProblems() {
    return problems;
  }

  public void setProblems(LinkedList<ObjectId> problems) {
    this.problems = problems;
  }

  public Boolean getLock() {
    return isLock;
  }

  public void setLock(Boolean lock) {
    isLock = lock;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean checkPassword(String password) {
    return this.password.equals(password);
  }

  @JsonIgnore
  public Update getUpdateData() {
    Update update = new Update();
    update.set("name", this.getName())
            .set("always", this.isAlways())
            .set("start_time", this.getStartTime())
            .set("end_time", this.getEndTime())
            .set("is_lock", this.getLock())
            .set("password", this.password);
    return update;
  }
}
