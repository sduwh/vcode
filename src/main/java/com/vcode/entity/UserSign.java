package com.vcode.entity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author moyee
 * @version 1.0.0
 * @Description 用户注册实体
 * @Date
 */
public class UserSign {

  @NotBlank
  @NotNull
  @NotEmpty
  @Length(min = 8, max = 12)
  private String account;

  @NotBlank
  @NotNull
  @NotEmpty
  @Length(min = 8, max = 32)
  private String password;

  @NotBlank
  @NotNull
  @NotEmpty
  @Length(min = 8, max = 32)
  private String rePassword;

  @NotNull
  @Email
  private String email;

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRePassword() {
    return rePassword;
  }

  public void setRePassword(String rePassword) {
    this.rePassword = rePassword;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
