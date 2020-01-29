package com.vcode.controller;

import com.vcode.Impl.VUserDaoImpl;
import com.vcode.common.ResponseCodeConstants;
import com.vcode.entitiy.Response;
import com.vcode.entitiy.VUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class VUserController {
  
  @Autowired
  private VUserDaoImpl userDao;
  
  @GetMapping("/hello")
  public String hello() {
    return "hello";
  }
  
  @PostMapping("/sign_in")
  public Response signIn() {
    /*
    注册逻辑
    1. 检查输入参数的合法性
    2. 检查是否有用户已经使用改账户
    3. 注册账号，创建新的用户保存至数据库
     */
    String account = "";
    String password = "";
    System.out.println("hello" + password);
    System.out.println("hello" + password);
    Response res = new Response();
  
    res.setMsg("sign in this site success");
    return res;
  }
  
  @PostMapping("/login")
  public Response login(@RequestBody Map<String, Object> map) throws NoSuchAlgorithmException {
    /*
    登陆逻辑
    1. 检查输入参数的合法性
    2. 检查输入账号是否存在对应的用户
    3. 若存在，检查密码是否一致
     */
    Response res = new Response();
    System.out.println("hello" + map.get("account"));
    if (map.get("account") == null || map.get("password") == null) {
      res.setCode(ResponseCodeConstants.ERROR);
      res.setMsg("请输入完整参数");
      return res;
    }
    String account = map.get("account").toString();
    String password = map.get("password").toString();
    System.out.println("hello"+password);
    VUser user = userDao.findUserByUserAccount(account);
    System.out.println(user);
    return res;
  }
  
}
