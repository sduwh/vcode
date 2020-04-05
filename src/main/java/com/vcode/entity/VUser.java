package com.vcode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Document("v_user")
public class VUser implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonIgnore
  @Id
  private ObjectId id;

  @NotNull(message = "account is required")
  @NotEmpty(message = "account can't be empty")
  @NotBlank(message = "account can't be blank")
  @Size(min = 8)
  @Field("account")
  private String account;

  @NotNull(message = "nickname is required")
  @NotEmpty(message = "nickname can't be empty")
  @NotBlank(message = "nickname can't be blank")
  @Field("nickname")
  private String nickname;

  @Field("email")
  private String email;

  @JsonIgnore
  @Field("password")
  private String password;

  @Field("createTime")
  private Date createTime;

  @NotNull(message = "role is required")
  @NotEmpty(message = "role can't be empty")
  @NotBlank(message = "role can't be blank")
  @Field("role")
  private String role;

  @NotNull(message = "permission is required")
  @NotEmpty(message = "permission can't be empty")
  @NotBlank(message = "permission can't be blank")
  @Field("permission")
  private String permission;

  public VUser() {
    this.setCreateTime();
    this.setEmail(null);
    this.setNickname("");
  }

  public VUser(String account, String password) throws NoSuchAlgorithmException {
    this.setCreateTime();
    this.setEmail("");
    this.setAccount(account);
    this.setPassword(password);
    this.setNickname(account);
    this.setRole("user");
    this.setPermission("");
  }

  public ObjectId getId() {
    return this.id;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getAccount() {
    return this.account;
  }

  public void setNickname(String newNickname) {
    this.nickname = newNickname;
  }

  public String getNickname() {
    return nickname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String newEmail) {
    this.email = newEmail;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String newPassword) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(newPassword.getBytes());
    this.password = DatatypeConverter.printHexBinary(md.digest());
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime() {
    this.createTime = new Date();
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  public boolean checkPassword(String inputPassword) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(inputPassword.getBytes());
    return this.password.equals(DatatypeConverter.printHexBinary(md.digest()));
  }
}
