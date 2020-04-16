package com.vcode.common;

/**
 * @author moyee
 */
public class ProblemDifficultCode {
  static final public int LOW = 0;
  static final public int MID = 1;
  static final public int HEIGHT = 2;

  static public int difficultStrToInt(String difficult){
    int result;
    switch (difficult.toLowerCase()) {
      case "low":
        result = LOW;
        break;
      case "mid":
        result = MID;
        break;
      default:
        result = HEIGHT;
    }
    return result;
  }
}
