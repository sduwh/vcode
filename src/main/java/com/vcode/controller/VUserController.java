package com.vcode.controller;

import com.vcode.Impl.VUserDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Response;
import com.vcode.entity.VUser;
import com.vcode.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class VUserController {

  private VUserDaoImpl userDao;

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  public VUserController(VUserDaoImpl vUserDao) {
    this.userDao = vUserDao;
  }

  /**
   * @return 用户的全部资料
   * @Description 获取用户数据
   */
  @GetMapping()
  @RequiresAuthentication
  public Response getUserInfo() {
    Response response = new Response();
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JWTUtil.getAccount(token);
    VUser user = userDao.findUserByUserAccount(account);
    if (user == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("The user is not exist");
      logger.debug(response.getMessage());
      return response;
    }
    logger.info(String.format("user: %s has access full user info", account));
    HashMap<String, Object> resData = new HashMap<>();
    resData.put("user", user);
    response.setData(resData);
    return response;
  }

  /**
   * @param map 更新的数据
   * @return 更新用户的全部资料
   * @Description 修改用户数据
   */
  @PostMapping()
  @RequiresAuthentication
  public Response updateUserInfo(@RequestBody Map<String, String> map) {
    Response response = new Response();
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
    if (!nickname.equals("")) {
      user.setNickname(nickname);
    }
    if (!email.equals("")) {
      user.setEmail(email);
    }

    String err = userDao.updateUser(user);
    if (err != null) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage(err);
      logger.error(err);
      return response;
    }
    logger.info(String.format("user: %s has been updated", account));
    Map<String, Object> resData = new HashMap<>();
    resData.put("user", user);
    response.setData(resData);
    return response;
  }

  /**
   * @return 用户成功修改密码(包含token)
   * @Description 修改用户密码
   */
  @PostMapping("/password")
  @RequiresAuthentication
  public Response resetPassword(@RequestBody Map<String, String> map) throws NoSuchAlgorithmException {

    Response response = new Response();
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JWTUtil.getAccount(token);

    String oldPassword = map.get("oldPass");
    String newPassword = map.get("pass");
    String reNewPassword = map.get("checkPass");

    if (oldPassword == null || newPassword == null || reNewPassword == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("Please input all params");
      logger.debug(response.getMessage());
      return response;
    }

    VUser user = userDao.findUserByUserAccount(account);

    if (user.checkPassword(oldPassword)) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("The old password is wrong");
      logger.debug(String.format("user: %s, error: %s", account, response.getMessage()));
      return response;
    }

    if (newPassword.length() < 6) {
      response.setData(ResponseCode.FAIL);
      response.setMessage("The new password's length is required more than 6");
      logger.debug(String.format("user: %s, error: %s", account, response.getMessage()));
      return response;
    }

    if (!newPassword.equals(reNewPassword)) {
      response.setData(ResponseCode.FAIL);
      response.setMessage("The check password is wrong");
      logger.debug(String.format("user: %s, error: %s", account, response.getMessage()));
      return response;
    }

    user.setPassword(reNewPassword);
    userDao.saveUser(user);
    logger.info(String.format("user: %s has changed the password", account));

    HashMap<String, String> data = new HashMap<>();
    data.put("token", JWTUtil.sign(account, user.getPassword()));
    data.put("refreshToken", JWTUtil.signRefreshToken(account, user.getPassword()));
    response.setData(data);
    response.setMessage("Reset password success");
    return response;
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
    resData.put("refreshToken", JWTUtil.signRefreshToken(account, user.getPassword()));
    resData.put("nickname", user.getNickname());
    resData.put("account", user.getAccount());
    res.setData(resData);
    res.setMessage("注册成功");
    logger.info(String.format("user %s is sign.", user.getAccount()));
    return res;
  }


  /**
   * @param map 用户账号和密码
   * @return 登陆结果（包含token）
   * @Description 用户登陆api
   * @Date 2020/1/31 01:39
   */
  @PostMapping("/login")
  public Response login(@RequestBody Map<String, Object> map) throws NoSuchAlgorithmException {
    /*
      登陆逻辑
      1. 检查输入参数的合法性
      2. 检查输入账号是否存在对应的用户
      3. 若存在，检查密码是否一致
    */
    Response response = new Response();
    if (map.get("account") == null || map.get("password") == null) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("请输入完整参数");
      logger.debug(response.getMessage());
      return response;
    }
    String account = map.get("account").toString();
    String password = map.get("password").toString();
    VUser user = userDao.findUserByUserAccount(account);

    if (user == null) {
      response.setMessage("The account is not exist");
      response.setCode(ResponseCode.FAIL);
      logger.debug(response.getMessage());
      return response;
    }
    if (user.checkPassword(password)) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("The password is wrong");
      logger.debug(response.getMessage());
      return response;
    }
    HashMap<String, Object> resData = new HashMap<>();
    resData.put("token", JWTUtil.sign(user.getAccount(), user.getPassword()));
    resData.put("refreshToken", JWTUtil.signRefreshToken(account, user.getPassword()));
    resData.put("user", user);
    response.setData(resData);
    logger.info(String.format("user %s is login:", user.getNickname()));
    return response;
  }

  @PostMapping("/refresh-token")
  public Response refreshTokenHandle(@RequestBody Map<String, Object> map) {
    Response response = new Response();
    if (map.get("refreshToken") == null) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("refreshToken is required");
      logger.debug(response.getMessage());
      return response;
    }
    String refreshToken = (String) map.get("refreshToken");
    String account = JWTUtil.getAccount(refreshToken);
    VUser user = userDao.findUserByUserAccount(account);
    if (user == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("The user is not exist");
      logger.debug(response.getMessage());
      return response;
    }
    Map<String, Object> data = new HashMap<>();
    if (JWTUtil.verify(refreshToken, user.getAccount(), user.getPassword())) {
      String newToken = JWTUtil.sign(user.getAccount(), user.getPassword());
      String newRefreshToken = JWTUtil.signRefreshToken(user.getAccount(), user.getPassword());
      data.put("token", newToken);
      data.put("refreshToken", newRefreshToken);
      logger.info(String.format("user: %s refresh the token success, token: %s; refreshToken: %s", account, newToken,
              newRefreshToken));
      response.setData(data);
    } else {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("The token is not valid");
      logger.debug(response.getMessage());
    }
    return response;
  }
}
