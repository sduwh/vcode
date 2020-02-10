package com.vcode.entity;

import com.vcode.common.ResponseCode;

import java.io.Serializable;

public class Response implements Serializable {
  private int code;
  private String message;
  private Object data;
  private String error;

  public Response() {
    this.code = ResponseCode.SUCCESS;
    this.message = "success";
    this.data = null;
    this.error = null;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getError() {
    return error;
  }
}
