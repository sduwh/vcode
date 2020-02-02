package com.vcode.controller;

import com.vcode.Impl.VUserDaoImpl;
import com.vcode.common.ResponseCodeConstants;
import com.vcode.entitiy.Response;
import com.vcode.entitiy.VUser;
import com.vcode.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


@RestController
@RequestMapping("/user")
public class VUserController {
  
  @Autowired
  private VUserDaoImpl userDao;
  
  private Logger log = Logger.getLogger("UserController");
  
  /*
 注册逻辑
 1. 检查输入参数的合法性
 2. 检查是否有用户已经使用改账户
 3. 注册账号，创建新的用户保存至数据库
  */
  @PostMapping("/sign_in")
  public Response signIn(@RequestBody Map<String, Object> map) throws NoSuchAlgorithmException {
   /**
    * @Description 用户注册api
    * @Date 2020/1/31 01:40
    * @param account 用户账号
    * @param password 密码
    * @param rePassword 重复密码
    * @return 注册结果（包含token）
    */
    Response res = new Response();
    if (map.get("account") == null || map.get("password") == null || map.get("rePassword") == null) {
      res.setCode(ResponseCodeConstants.ERROR);
      res.setMessage("请输入完整参数");
      return res;
    }
    // 获取参数
    String account = map.get("account").toString();
    String password = map.get("password").toString();
    String rePassword = map.get("rePassword").toString();
    
    if (!password.equals(rePassword)) {
      res.setCode(ResponseCodeConstants.FAIL);
      res.setMessage("两次密码不一致");
      return res;
    }
    
    VUser user = userDao.findUserByUserAccount(account);
    if (user != null) {
      res.setCode(ResponseCodeConstants.FAIL);
      res.setMessage("该账号已存在");
      return res;
    }
    // 创建用户
    user = new VUser(account, password);
    userDao.saveUser(user);
    
    HashMap<String, String> resData = new HashMap<>();
    resData.put("token", JWTUtil.sign(user.getAccount(), user.getPassword()));
    resData.put("nickname", user.getNickname());
    resData.put("account", user.getAccount());
    res.setData(resData);
    res.setMessage("注册成功");
    log.info(String.format("user %s is sign.", user.getAccount()));
    return res;
  }
  
  /*
  登陆逻辑
  1. 检查输入参数的合法性
  2. 检查输入账号是否存在对应的用户
  3. 若存在，检查密码是否一致
   */
  @PostMapping("/login")
  public Response login(@RequestBody Map<String, Object> map) throws NoSuchAlgorithmException {
    /**
     * @Description 用户登陆api
     * @Date 2020/1/31 01:39
     * @param account 用户账号
     * @param password 用户密码
     * @return 登陆结果（包含token）
     */
    Response res = new Response();
    if (map.get("account") == null || map.get("password") == null) {
      res.setCode(ResponseCodeConstants.ERROR);
      res.setMessage("请输入完整参数");
      return res;
    }
    String account = map.get("account").toString();
    String password = map.get("password").toString();
    VUser user = userDao.findUserByUserAccount(account);
    if (user == null) {
      res.setMessage("账号不存在");
      res.setCode(ResponseCodeConstants.FAIL);
      return res;
    }
    if (!user.checkPassword(password)) {
      res.setCode(ResponseCodeConstants.FAIL);
      res.setMessage("密码错误");
      return res;
    }
    HashMap<String, String> resData = new HashMap<>();
    resData.put("token", JWTUtil.sign(user.getAccount(), user.getPassword()));
    resData.put("nickname", user.getNickname());
    resData.put("account", user.getAccount());
    res.setData(resData);
    log.info(String.format("user %s is login:", user.getNickname()));
    return res;
  }

  @GetMapping("/user-info")
  public Response userInfo(@RequestParam("account") String account){
    /**
     * @Description 获取用户数据
     * @param account 用户账号
     * @return 用户的全部资料
     */
    Response res=new Response();
//    if(account.length()<10){
//      res.setCode(ResponseCodeConstants.FAIL);
//      res.setMessage("账号长度小于10");
//      return res;
//    }
    VUser vUser=userDao.findUserByUserAccount(account);
    if(vUser==null){
      res.setCode(ResponseCodeConstants.FAIL);
      res.setMessage("该用户不存在");
      return res;
    }
    HashMap<String,String> resData=new HashMap<>();
    resData.put("account", vUser.getAccount());
    resData.put("nickname",vUser.getNickname());
    resData.put("email",vUser.getEmail());
    res.setData(resData);
    return res;
  }

}
