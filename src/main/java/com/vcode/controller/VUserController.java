package com.vcode.controller;

import com.vcode.common.ResponseCodeConstants;
import com.vcode.entitiy.ResponseEntity;
import com.vcode.entitiy.VUserEntity;
import org.springframework.web.bind.annotation.*;
import com.vcode.domain.*;

import javax.validation.constraints.Size;
import java.security.NoSuchAlgorithmException;


@RestController
@RequestMapping("/user")
public class VUserController {
  
  @GetMapping("/hello")
  public String hello() {
    return "hello";
  }
  
  @PostMapping("/sign_in")
  public ResponseEntity signIn() {
    /*
    注册逻辑
    1. 检查输入参数的合法性
    2. 检查是否有用户已经使用改账户
    3. 注册账号，创建新的用户保存至数据库
     */
    String account = "";
    String password = "";
    System.out.println("hello"+ password);
    System.out.println("hello"+ password);
    ResponseEntity res = new ResponseEntity();
    VUserEntity user = new VUserEntity();
    VUserDaoImpl vUserDao = new VUserDaoImpl();
    if (vUserDao.isExitVUser(account)) {
      res.setCode(ResponseCodeConstants.FAIL);
      res.setMsg("account is exit");
      return res;
    }
    try {
      user.setAccount(account);
      user.setPassword(password);
      user.setNickname(account);
      vUserDao.saveVUser(user);
    } catch (Exception e) {
      res.setCode(ResponseCodeConstants.ERROR);
      res.setMsg("create user error");
      return res;
    }
    res.setMsg("sign in this site success");
    return res;
  }
  
  @PostMapping("/login")
  public ResponseEntity login() throws NoSuchAlgorithmException {
    /*
    登陆逻辑
    1. 检查输入参数的合法性
    2. 检查输入账号是否存在对应的用户
    3. 若存在，检查密码是否一致
     */
    String account = "";
    String password = "";
    System.out.println("hello"+ password);
    System.out.println("hello"+ password);
    ResponseEntity res = new ResponseEntity();
//    VUserDaoImpl vUserDao = new VUserDaoImpl();
//    VUserEntity vUser = vUserDao.findByAccount(account);
//    if (vUser == null) {
//      res.setCode(ResponseCodeConstants.ERROR);
//      res.setMsg("The account is not exit.");
//      return res;
//    }
//    if (vUser.checkPassword(password)) {
//      res.setCode(ResponseCodeConstants.SUCCESS);
//      res.setMsg("login success.");
//      res.setData(vUser);
//    } else {
//      res.setCode(ResponseCodeConstants.FAIL);
//      res.setMsg("password is wrong.");
//    }
    return res;
  }
  
}
