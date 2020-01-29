package com.vcode.entitiy;

import com.vcode.common.ResponseCodeConstants;

import java.io.Serializable;

public class Response implements Serializable {
  private int code;
  private String msg;
  private Object data;
  
  public Response() {
    this.code = ResponseCodeConstants.SUCCESS;
    this.msg = "success";
    this.data = null;
  }
  
  public Object getData() {
    return data;
  }
  
  public void setData(Object data) {
    this.data = data;
  }
  
  public String getMsg() {
    return msg;
  }
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  public int getCode() {
    return code;
  }
  
  public void setCode(int code) {
    this.code = code;
  }
}
