package com.vcode.common;

/**
 * @author moyee
 */
public class SubmissionResultCode {
  static final public int WRONG = 0;
  static final public int SUCCESS = 1;
  static final public int TIME_OUT = 2;
  static final public int MEMORY_OUT = 3;
  static final public int UNKNOWN_ERROR = 4;
  static final public int PADDING = 5;
  static final public int COMPILE_ERROR = 6;

  static public int resultStrToInt(String result) {
    int res;
    switch (result.toLowerCase()) {
      case "wrong":
        res = WRONG;
        break;
      case "success":
      case "accepted":
      case "accept":
        res = SUCCESS;
        break;
      case "time_out":
      case "timeout":
        res = TIME_OUT;
        break;
      case "memory_out":
      case "memory out":
        res = MEMORY_OUT;
        break;
      case "padding":
        res = PADDING;
        break;
      case "compile_error":
      case "compile error":
        res = COMPILE_ERROR;
        break;
      default:
        res = UNKNOWN_ERROR;
    }
    return res;
  }
}
