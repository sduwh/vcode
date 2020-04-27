package com.vcode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Mongo;
import com.vcode.common.MongoCode;
import com.vcode.common.ProblemDifficultCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author moyee
 * @version 1.0.0
 * @Description 问题实体
 * @since 1.0.0
 */
@Document("problem")
public class Problem implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final String ORIGIN_ID_SPLIT_KEY = "-";

  @JsonIgnore
  @Id
  private ObjectId id;

  @Field("create_time")
  private long createTime;

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
  private String input;

  @NotEmpty(message = "output不能为空")
  @NotBlank(message = "output不能为空格字符串")
  @NotNull(message = "output不能为null")
  @Field("output")
  private String output;

  @NotNull(message = "sample_input不能为null")
  @Field("sample_input")
  private String[] sampleInput;

  @NotNull(message = "sample_output不能为null")
  @Field("sample_output")
  private String[] sampleOutput;

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

  @NotNull(message = "visible is required")
  @Field("visible")
  private boolean visible;

  @Field("languages")
  private String[] languages;

  @NotNull(message = "choiceLanguages is required")
  @Field("choice_languages")
  private String[] choiceLanguages;

  @Field("hint")
  private String hint;

  /**
   * 0 => low; 1 => mid; 2 => height;
   */
  @Field("difficulty")
  private int difficulty;

  @Field("submission_number")
  private long submissionNumber;

  @Field("accepted_number")
  private long acceptedNumber;

  @NotNull(message = "source is required")
  @Field("source")
  private String source;

  @NotNull(message = "testCaseId is required")
  @Field("test_case_id")
  private String testCaseId;

  public Problem() {
    this.origin = MongoCode.VCODE;
    this.originId = MongoCode.VCODE + "1000";
    this.title = "";
    this.description = "";
    this.input = "";
    this.output = "";
    this.sampleInput = new String[]{};
    this.sampleOutput = new String[]{};
    this.author = "admin";
    this.timeLimit = "";
    this.memoryLimit = "";
    this.visible = true;
    this.languages = new String[]{"C", "C++", "JAVA", "PYTHON3", "GO"};
    this.choiceLanguages = new String[]{};
    this.hint = "";
    this.difficulty = 0;
    this.submissionNumber = 0;
    this.acceptedNumber = 0;
    this.source = "";
    this.testCaseId = "";
    this.createTime = System.currentTimeMillis();
  }

  public void setId(ObjectId id) {
    this.id = id;
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
    this.originId = this.origin + '-' + originId;
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

  public String[] getSampleInput() {
    return sampleInput;
  }

  public void setSampleInput(String[] sampleInput) {
    this.sampleInput = sampleInput;
  }

  public String[] getSampleOutput() {
    return sampleOutput;
  }

  public void setSampleOutput(String[] sampleOutput) {
    this.sampleOutput = sampleOutput;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author == null ? "" : author;
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
    if (difficulty == ProblemDifficultCode.LOW || difficulty == ProblemDifficultCode.MID || difficulty == ProblemDifficultCode.HEIGHT) {
      this.difficulty = difficulty;
    } else {
      this.difficulty = 1;
    }
  }

  public long getCreateTime() {
    return createTime;
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

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public String[] getLanguages() {
    return languages;
  }

  public void setLanguages(String[] languages) {
    this.languages = languages;
  }

  public String getHint() {
    return hint;
  }

  public void setHint(String hint) {
    this.hint = hint == null ? "" : hint;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source == null ? "" : source;
  }

  public String getTestCaseId() {
    return testCaseId;
  }

  public void setTestCaseId(String testCaseId) {
    this.testCaseId = testCaseId;
  }


  public String[] getChoiceLanguages() {
    return choiceLanguages;
  }

  public void setChoiceLanguages(String[] choiceLanguages) {
    this.choiceLanguages = choiceLanguages;
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
            .set("memory_limit", this.getMemoryLimit())
            .set("hint", this.hint)
            .set("visible", this.visible)
            .set("source", this.source)
            .set("test_case_id", this.testCaseId)
            .set("choice_languages", this.choiceLanguages);
    return update;
  }

  public static Problem createProblemByJson(String jsonStr) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    HashMap map = objectMapper.readValue(jsonStr, HashMap.class);
    Problem problem = new Problem();
    problem.setOrigin((String) map.get("origin"));
    problem.setOriginId((String) map.get("origin_id"));
    problem.setTitle((String) map.get("title"));
    problem.setTimeLimit(String.valueOf(map.get("time_limit")));
    problem.setMemoryLimit((String.valueOf(map.get("memory_limit"))));
    problem.setDescription((String) map.get("description"));
    problem.setInput((String) map.get("input"));
    problem.setOutput((String) map.get("output"));
    problem.setSampleInput((new String[]{(String) map.get("sample_input")}));
    problem.setSampleOutput((new String[]{(String) map.get("sample_output")}));
    problem.setHint((String) map.get("hint"));
    problem.setSource((String) map.get("source"));
    problem.setVisible(true);
    problem.setAuthor((String) map.get("origin"));
    ArrayList<String> arrayList = (ArrayList<String>) map.get("language");
    problem.setLanguages(arrayList.toArray(new String[0]));
    problem.setChoiceLanguages(problem.getLanguages());
    return problem;
  }

  public void updateByProblem(Problem problem) {
    this.originId = problem.getOriginId();
    this.title = problem.getTitle();
    this.timeLimit = problem.getTimeLimit();
    this.memoryLimit = problem.getMemoryLimit();
    this.description = problem.getDescription();
    this.input = problem.getInput();
    this.output = problem.getOutput();
    this.sampleInput = problem.getSampleInput();
    this.sampleOutput = problem.getSampleOutput();
    this.hint = problem.getHint();
    this.source = problem.getSource();
    this.languages = problem.getLanguages();
  }
}