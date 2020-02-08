package com.vcode.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
  @Id
  private ObjectId id;

  @Field("create_time")
  private Date createTime;

  @Field("user_account")
  private String user_account;

  @Field("user_nickname")
  private String nickname;

  @Field("problem_title")
  private String problemTitle;

  @Field("problem_origin_id")
  private String problemOriginId;

  @Field("time")
  private String time;

  @Field("memory")
  private String memory;

  @Field("language")
  private String language;

  @Field("code")
  private String code;
}
