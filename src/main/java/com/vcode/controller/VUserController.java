package com.vcode.controller;

import com.vcode.Impl.VUserDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Response;
import com.vcode.entity.VUser;
import com.vcode.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


@RestController
@RequestMapping("/user")
public class VUserController {

  private VUserDaoImpl userDao;

  private Logger log = Logger.getLogger("UserController");

  @Autowired
  public VUserController(VUserDaoImpl vUserDao) {
    this.userDao = vUserDao;
  }

  @GetMapping()
  @RequiresAuthentication
  public Response getUserInfo() {
    /**
     * @Description 获取用户数据
     * @param account 用户账号
     * @return 用户的全部资料
     */
    Response res = new Response();
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JWTUtil.getAccount(token);
    VUser user = userDao.findUserByUserAccount(account);
    if (user == null) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("该用户不存在");
      return res;
    }
    HashMap<String, Object> resData = new HashMap<>();
    resData.put("user", user);
    res.setData(resData);
    return res;
  }

  @PostMapping()
  @RequiresAuthentication
  public Response updateUserInfo(@RequestBody Map<String, String> map) {
    /**
     * @Description 修改用户数据
     * @param account 用户账号
     * @return 更新用户的全部资料
     */
    Response res = new Response();
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JWTUtil.getAccount(token);
    String email = "";
    String nickname = "";

    if (map.get("nickname") != null) {
      nickname = map.get("nickname");
    }
    if (map.get("email") != null) {
      email = map.get("email");
    }

    VUser user = userDao.findUserByUserAccount(account);
    if (user == null) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("该用户不存在");
      return res;
    }

    if (!nickname.equals("")) {
      user.setNickname(nickname);
    }
    if (!email.equals("")) {
      user.setEmail(email);
    }

    String err = userDao.updateUser(user);
    if (err != null) {
      res.setCode(ResponseCode.ERROR);
      res.setMessage(err);
      return res;
    }

    Map<String, Object> resData = new HashMap<>();
    resData.put("user", user);
    res.setData(resData);
    return res;
  }

  /**
   * @return 用户成功修改密码(包含token)
   * @Description 修改用户密码
   */
  @PostMapping("/password")
  @RequiresAuthentication
  public Response resetPassword(@RequestBody Map<String, String> map) throws NoSuchAlgorithmException {

    Response res = new Response();
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JWTUtil.getAccount(token);

    String oldPassword = map.get("oldPassword");
    String newPassword = map.get("newPassword");
    String reNewPassword = map.get("reNewPassword");

    VUser user = userDao.findUserByUserAccount(account);

    if (user == null) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("该用户不存在");
      return res;
    }
    if (user.checkPassword(oldPassword)) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("密码输入错误");
      return res;
    }

    if (newPassword.length() < 6) {
      res.setData(ResponseCode.FAIL);
      res.setMessage("请确保新密码长度大于或等于6");
      return res;
    }

    if (!newPassword.equals(reNewPassword)) {
      res.setData(ResponseCode.FAIL);
      res.setMessage("请确保两次新密码输入一致");
      return res;
    }

    user.setPassword(reNewPassword);
    userDao.saveUser(user);

    HashMap<String, String> resData = new HashMap<>();
    resData.put("token", JWTUtil.sign(user.getAccount(), user.getPassword()));
    res.setData(resData);
    res.setMessage("修改密码成功");
    return res;
  }

  /*
注册逻辑
1. 检查输入参数的合法性
2. 检查是否有用户已经使用改账户
3. 注册账号，创建新的用户保存至数据库
*/
  @PostMapping("/sign-in")
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
      res.setCode(ResponseCode.ERROR);
      res.setMessage("请输入完整参数");
      return res;
    }
    // 获取参数
    String account = map.get("account").toString();
    String password = map.get("password").toString();
    String rePassword = map.get("rePassword").toString();

    if (account.length() < 8) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("account's length must be more than 8");
      return res;
    }

    if (password.length() < 6) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("password's length must be more than 6");
      return res;
    }

    if (!password.equals(rePassword)) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("两次密码不一致");
      return res;
    }

    VUser user = userDao.findUserByUserAccount(account);
    if (user != null) {
      res.setCode(ResponseCode.FAIL);
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
      res.setCode(ResponseCode.ERROR);
      res.setMessage("请输入完整参数");
      return res;
    }
    String account = map.get("account").toString();
    String password = map.get("password").toString();
    VUser user = userDao.findUserByUserAccount(account);

    if (user == null) {
      res.setMessage("账号不存在");
      res.setCode(ResponseCode.FAIL);
      return res;
    }
    if (!user.checkPassword(password)) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("密码错误");
      return res;
    }
    HashMap<String, Object> resData = new HashMap<>();
    resData.put("token", JWTUtil.sign(user.getAccount(), user.getPassword()));
    resData.put("user", user);
    res.setData(resData);
    log.info(String.format("user %s is login:", user.getNickname()));
    return res;
  }
}
