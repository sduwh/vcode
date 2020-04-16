package com.vcode.entity;

import java.util.LinkedList;
import java.util.List;

/**
 * @author moyee
 * @version 1.0.0
 * @Description rank data sorted item
 * @Date
 */
public class RankUser {

  private String userAccount;

  private String username;

  private String contestName;

  private long totalErrorNum;

  /**
   * every problem only can increase 1
   */
  private long totalAcNum;

  private long totalTimeUsed;

  private List<RankProblemItem> problemSolvedInfo;

  public RankUser(Rank rank) {
    this.contestName = rank.getContestName();
    this.userAccount = rank.getUserAccount();
    this.username = rank.getUsername();
    this.totalAcNum = 0;
    this.totalErrorNum = 0;
    this.problemSolvedInfo = new LinkedList<>();
    this.totalTimeUsed = 0;
  }

  public String getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(String userAccount) {
    this.userAccount = userAccount;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getContestName() {
    return contestName;
  }

  public void setContestName(String contestName) {
    this.contestName = contestName;
  }

  public long getTotalErrorNum() {
    return totalErrorNum;
  }

  public void setTotalErrorNum(long totalErrorNum) {
    this.totalErrorNum = totalErrorNum;
  }

  public long getTotalAcNum() {
    return totalAcNum;
  }

  public void setTotalAcNum(long totalAcNum) {
    this.totalAcNum = totalAcNum;
  }

  public List<RankProblemItem> getProblemSolvedInfo() {
    return problemSolvedInfo;
  }

  public void pushItemToList(RankProblemItem problemItem) {
    this.problemSolvedInfo.add(problemItem);
  }

  public void setProblemSolvedInfo(List<RankProblemItem> problemSolvedInfo) {
    this.problemSolvedInfo = problemSolvedInfo;
  }

  public long getTotalTimeUsed() {
    return totalTimeUsed;
  }

  public void setTotalTimeUsed(long totalTimeUsed) {
    this.totalTimeUsed = totalTimeUsed;
  }
}
