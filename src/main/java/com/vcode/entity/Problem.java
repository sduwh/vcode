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
  @JsonIgnore
  @Id
  private ObjectId id;

  @Field("create_time")
  private long create_time;

  @NotEmpty(message = "origin不能为空")
  @NotBlank(message = "origin不能为空格字符串")
  @NotNull(message = "origin不能为null")
  @Field("origin")
  private String origin;

  @NotEmpty(message = "origin_id不能为空")
  @NotBlank(message = "origin_id不能为空格字符串")
  @NotNull(message = "origin_id不能为null")
  @Field("origin_id")
  private String originId;

  @NotEmpty(message = "title不能为空")
  @NotBlank(message = "title不能为空格字符串")
  @NotNull(message = "title不能为null")
  @Field("title")
  private String title;

  @NotEmpty(message = "description不能为空")
  @NotBlank(message = "description不能为空格字符串")
  @NotNull(message = "description不能为null")
  @Field("description")
  private String description;

  @NotEmpty(message = "input不能为空")
  @NotBlank(message = "input不能为空格字符串")
  @NotNull(message = "input不能为null")
  @Field("input")
  private String input; // 输入描述

  @NotEmpty(message = "output不能为空")
  @NotBlank(message = "output不能为空格字符串")
  @NotNull(message = "output不能为null")
  @Field("output")
  private String output; // 输出描述

  @NotEmpty(message = "sample_input不能为空")
  @NotBlank(message = "sample_input不能为空格字符串")
  @NotNull(message = "sample_input不能为null")
  @Field("sample_input")
  private String sampleInput;

  @NotEmpty(message = "sample_output不能为空")
  @NotBlank(message = "sample_output不能为空格字符串")
  @NotNull(message = "sample_output不能为null")
  @Field("sample_output")
  private String sampleOutput;

  @NotEmpty(message = "author不能为空")
  @NotBlank(message = "author不能为空格字符串")
  @NotNull(message = "author不能为null")
  @Field("author")
  private String author;

  @NotEmpty(message = "time_limit不能为空")
  @NotBlank(message = "time_limit不能为空格字符串")
  @NotNull(message = "time_limit不能为null")
  @Field("time_limit")
  private String timeLimit;

  @NotEmpty(message = "memory_limit不能为空")
  @NotBlank(message = "memory_limit不能为空格字符串")
  @NotNull(message = "memory_limit不能为null")
  @Field("memory_limit")
  private String memoryLimit;

  @Field("difficulty")
  private int difficulty;  // 0 => low; 1 => mid; 2 => height;

  @Field("submission_number")
  private long submissionNumber;

  @Field("accepted_number")
  private long acceptedNumber;

  public Problem(String origin, String originId, String title, String description, String input, String output, String sampleInput, String sampleOutput, String author, String timeLimit, String memoryLimit, int difficulty) {
    this.origin = origin;
    this.originId = originId;
    this.title = title;
    this.description = description;
    this.input = input;
    this.output = output;
    this.sampleInput = sampleInput;
    this.sampleOutput = sampleOutput;
    this.author = author;
    this.timeLimit = timeLimit;
    this.memoryLimit = memoryLimit;
    this.difficulty = difficulty;

    this.create_time = System.currentTimeMillis();
  }

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
    this.originId = this.origin + '-' +  originId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public long getCreate_time() {
    return create_time;
  }

  public long getSubmissionNumber() {
    return submissionNumber;
  }

  public void setSubmissionNumber(long submissionNumber) {
    this.submissionNumber = submissionNumber;
  }

  public long getAcceptedNumber() {
    return acceptedNumber;
  }

  public void setAcceptedNumber(long acceptedNumber) {
    this.acceptedNumber = acceptedNumber;
  }

  @JsonIgnore
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