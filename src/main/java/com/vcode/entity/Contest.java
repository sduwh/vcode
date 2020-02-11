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

  @NotEmpty(message = "always不能为空")
  @NotBlank(message = "always不能为空格字符串")
  @NotNull(message = "always不能为null")
  @Field("always")
  private boolean always; // true => 永久开放；false => 限时开放；

  @NotEmpty(message = "start_time不能为空")
  @NotBlank(message = "start_time不能为空格字符串")
  @Field("start_time")
  private Date startTime;

  @NotEmpty(message = "end_time不能为空")
  @NotBlank(message = "end_time不能为空格字符串")
  @Field("end_time")
  private Date endTime;

  @NotEmpty(message = "owner_account不能为空")
  @NotBlank(message = "owner_account不能为空格字符串")
  @NotNull(message = "owner_account不能为null")
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
