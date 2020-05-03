package com.vcode.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;

/**
 * @version 1.0.0
 * @Description group实体
 * @since 1.0.0
 */
@Document("group")
public class Group {

  private static final long serialVersionUID = 1L;

  @Id
  private ObjectId id;

  @Field("createTime")
  private long createTime;

  @NotEmpty(message = "group_name不能为空")
  @NotNull(message = "group_name不能为null")
  @Field("group_name")
  private String group_name;

  @NotNull(message = "visible is required")
  @Field("visible")
  private boolean visible;

  @NotEmpty(message = "join_policy不能为空")
  @NotBlank(message = "join_policy不能为空格字符串")
  @NotNull(message = "join_policy不能为null")
  @Field("join_policy")
  private String join_policy;

  @NotEmpty(message = "owner不能为空")
  @NotBlank(message = "owner不能为空格字符串")
  @NotNull(message = "owner不能为null")
  @Field("owner")
  private String owner;

  @Field("members")
  private final LinkedList<ObjectId> members;

  @Field("contests")
  private final LinkedList<ObjectId> contests;

  public Group(){
    this.group_name="";
    this.join_policy="";
    this.visible=true;
    this.owner="";
    this.contests = new LinkedList<>();
    this.members = new LinkedList<>();
    this.createTime = System.currentTimeMillis();
  }

  public Group(String group_name){
    this.group_name=group_name;
    this.join_policy=join_policy;
    this.visible=visible;
    this.owner=owner;
    this.contests = new LinkedList<>();
    this.members = new LinkedList<>();
    this.createTime = System.currentTimeMillis();
  }

  public ObjectId getId() {
    return id;
  }

  public void setGroup_name(){this.group_name=group_name;}

  public String getGroup_name(){return group_name;}

  public void setVisible(){this.visible=visible;}

  public boolean isVisible(){return visible;}

  public void setJoin_policy(){this.join_policy=join_policy;}

  public String getJoin_policy(){return join_policy;}

  public void setOwner(){this.owner=owner;}

  public String getOwner(){return owner;}

  public LinkedList<ObjectId> getMembers(){return members;}

  public LinkedList<ObjectId> getContests(){return contests;}
}

