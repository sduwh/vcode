package com.vcode.entity;

/**
 * @author moyee
 * @version 1.0.0
 * @Description used in rank data item's list
 * @Date
 */
public class RankProblemItem {

  private String problemOriginId;

  private boolean isEarliest;

  private long errorNum;

  private boolean isAc;

  public RankProblemItem(Rank rank) {
    this.problemOriginId = rank.getProblemOriginId();
    this.isAc = rank.getAcNum() > 0;
    this.isEarliest = rank.isEarliest();
    this.errorNum = rank.getWrongNum();
  }

  public String getProblemOriginId() {
    return problemOriginId;
  }

  public void setProblemOriginId(String problemOriginId) {
    this.problemOriginId = problemOriginId;
  }

  public boolean isEarliest() {
    return isEarliest;
  }

  public void setEarliest(boolean earliest) {
    isEarliest = earliest;
  }

  public long getErrorNum() {
    return errorNum;
  }

  public void setErrorNum(long errorNum) {
    this.errorNum = errorNum;
  }

  public boolean isAc() {
    return isAc;
  }

  public void setAc(boolean ac) {
    isAc = ac;
  }
}
