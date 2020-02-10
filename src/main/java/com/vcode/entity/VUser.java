package com.vcode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


@Document("v_user")
public class VUser implements Serializable {

  @JsonIgnore
  @Id
  private ObjectId id;
  
  @Field("account")
  private String account;
  
  @Field("nickname")
  private String nickname;
  
  @Field("email")
  private String email;
  
  @Field("password")
  private String password;
  
  @Field("createTime")
  private Date createTime;
  
  public VUser() {
    this.setCreateTime();
    this.setEmail(null);
    this.setNickname("");
  }
  
  public VUser(String account, String password) throws NoSuchAlgorithmException {
    this.setCreateTime();;
    this.setEmail(null);
    this.setAccount(account);
    this.setPassword(password);
    this.setNickname(account);
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
  
  public boolean checkPassword(String inputPassword) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(inputPassword.getBytes());
    return this.password.equals(DatatypeConverter.printHexBinary(md.digest()));
  }
}
