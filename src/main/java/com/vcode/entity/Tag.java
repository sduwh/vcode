package com.vcode.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * @author moyee
 * @version 1.0.0
 * @Description 标签实体
 * @return Tag实例
 * @since 1.0.0
 */
@Document("tag")
public class Tag implements Serializable {
  @Id
  private ObjectId id;

  @Field("name")
  private String name;

  @Field("problems")
  private LinkedList<ObjectId> problems;  // 存放所有此tag下的问题ObjectId

  public Tag() {
    this.setName("");
    this.problems = new LinkedList<>();
  }

  public Tag(String name) {
    this.name = name;
    this.problems = new LinkedList<>();
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

  public LinkedList<ObjectId> getProblems() {
    return problems;
  }
}
