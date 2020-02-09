package com.vcode.entity;

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
@Document("contest")
public class Contest {
  @Id
  private ObjectId id;

  @Field("name")
  private String name;

  @Field("always")
  private boolean always; // true => 永久开放；false => 限时开放；

  @Field("start_time")
  private Date startTime;

  @Field("end_time")
  private Date endTime;

  @Field("owner_account")
  private String owner_account;

  public Contest(String name, boolean always, Date startTime, Date endTime, String owner_account) {
    this.name = name;
    this.always = always;
    this.startTime = startTime;
    this.endTime = endTime;
    this.owner_account = owner_account;
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

  public String getOwner_account() {
    return owner_account;
  }

  public void setOwner_account(String owner_account) {
    this.owner_account = owner_account;
  }

  public boolean checkOwner(String owner_account) {
    return this.owner_account.equals(owner_account);
  }

  public Update getUpdateData() {
    Update update = new Update();
    update.set("name", this.getName())
            .set("always", this.isAlways())
            .set("startTime", this.getStartTime())
            .set("endTime", this.getEndTime());
    return update;
  }
}
