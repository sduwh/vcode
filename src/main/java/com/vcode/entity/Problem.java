package com.vcode.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;

/**
 * @author moyee
 * @version 1.0.0
 * @Description 问题实体
 * @return 新的问题对象
 * @since 1.0.0
 */
@Document("problem")
public class Problem implements Serializable {
  @Id
  private ObjectId id;

  @Field("origin")
  private String origin;

  @Field("origin_id")
  private String originId;

  @Field("title")
  private String title;

  @Field("description")
  private String Description;

  @Field("input")
  private String input; // 输入描述

  @Field("output")
  private String output; // 输出描述

  @Field("sample_input")
  private String sampleInput;

  @Field("sample_output")
  private String sampleOutput;

  @Field("author")
  private String author;

  @Field("time_limit")
  private String timeLimit;

  @Field("memory_limit")
  private String memoryLimit;

  @Field("difficulty")
  private int difficulty;  // 0 => low; 1 => mid; 2 => height;

  public ObjectId getId() {
    return id;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String getOriginId() {
    return originId;
  }

  public void setOriginId(String originId) {
    this.originId = this.origin + originId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return Description;
  }

  public void setDescription(String description) {
    Description = description;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public String getOutput() {
    return output;
  }

  public void setOutput(String output) {
    this.output = output;
  }

  public String getSampleInput() {
    return sampleInput;
  }

  public void setSampleInput(String sampleInput) {
    this.sampleInput = sampleInput;
  }

  public String getSampleOutput() {
    return sampleOutput;
  }

  public void setSampleOutput(String sampleOutput) {
    this.sampleOutput = sampleOutput;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    if (author == null) {
      author = "";
    }
    this.author = author;
  }

  public String getTimeLimit() {
    return timeLimit;
  }

  public void setTimeLimit(String timeLimit) {
    this.timeLimit = timeLimit;
  }

  public String getMemoryLimit() {
    return memoryLimit;
  }

  public void setMemoryLimit(String memoryLimit) {
    this.memoryLimit = memoryLimit;
  }

  public int getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(int difficulty) {
    if (difficulty == 0 || difficulty == 1 || difficulty == 2) {
      this.difficulty = difficulty;
    } else {
      this.difficulty = 1;
    }
  }

  public Update getUpdateData() {
    Update update = new Update();
    update.set("title", this.getTitle())
            .set("Description", this.getDescription())
            .set("input", this.getInput())
            .set("output", this.getOutput())
            .set("sample_input", this.getSampleInput())
            .set("sample_output", this.getSampleOutput())
            .set("author", this.getOrigin())
            .set("time_limit", this.getTimeLimit())
            .set("memory_limit", this.getMemoryLimit());
    return update;
  }
}