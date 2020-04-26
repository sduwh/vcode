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
  static final public int RUNTIME_ERROR = 7;
  static final public int OUTPUT_LIMIT = 8;
  static final public int PRESENTATION_ERROR = 9;

  static public int resultStrToInt(String result) {
    int res;
    switch (result.toLowerCase()) {
      case "wrong":
      case "wrong answer":
        res = WRONG;
        break;
      case "success":
      case "accepted":
      case "accept":
        res = SUCCESS;
        break;
      case "time_out":
      case "timeout":
      case "time limit exceeded":
        res = TIME_OUT;
        break;
      case "memory_out":
      case "memory out":
      case "memory limit exceeded":
        res = MEMORY_OUT;
        break;
      case "padding":
      case "judging":
        res = PADDING;
        break;
      case "compile_error":
      case "compile error":
        res = COMPILE_ERROR;
        break;
      case "runtime error":
        res = RUNTIME_ERROR;
        break;
      case "output limit exceeded":
        res = OUTPUT_LIMIT;
        break;
      case "presentation error":
        res = PRESENTATION_ERROR;
        break;
      default:
        res = UNKNOWN_ERROR;
    }
    return res;
  }
}
