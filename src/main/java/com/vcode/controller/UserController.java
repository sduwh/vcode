package com.vcode.controller;

import com.vcode.common.ResponseCode;
import com.vcode.entity.Response;
import com.vcode.entity.User;
import com.vcode.entity.UserSign;
import com.vcode.impl.UserDaoImpl;
import com.vcode.util.JwtUtil;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author moyee
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDaoImpl userDao;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserController(UserDaoImpl vUserDao) {
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
        String account = JwtUtil.getAccount(token);
        User user = userDao.findUserByUserAccount(account);
        if (user == null) {
            response.setCode(ResponseCode.FAIL);
            response.setMessage("The user is not exist");
            logger.debug(response.getMessage());
            return response;
        }
        logger.info(String.format("user: %s has access full user info", account));
        HashMap<String, Object> resData = new HashMap<>(1);
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
        String nicknameKey = "nickname";
        String emailKey = "email";
        Response response = new Response();
        Subject subject = SecurityUtils.getSubject();
        String token = (String) subject.getPrincipal();
        String account = JwtUtil.getAccount(token);
        String email = "";
        String nickname = "";

        if (map.get(nicknameKey) != null) {
            nickname = map.get(nicknameKey);
        }
        if (map.get(emailKey) != null) {
            email = map.get(emailKey);
        }

        User user = userDao.findUserByUserAccount(account);
        if (!"".equals(nickname)) {
            user.setNickname(nickname);
        }
        if (!"".equals(email)) {
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
        Map<String, Object> resData = new HashMap<>(1);
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
        String account = JwtUtil.getAccount(token);

        String oldPassword = map.get("oldPass");
        String newPassword = map.get("pass");
        String reNewPassword = map.get("checkPass");

        if (oldPassword == null || newPassword == null || reNewPassword == null) {
            response.setCode(ResponseCode.FAIL);
            response.setMessage("Please input all params");
            logger.debug(response.getMessage());
            return response;
        }

        User user = userDao.findUserByUserAccount(account);

        if (user.checkPassword(oldPassword)) {
            response.setCode(ResponseCode.FAIL);
            response.setMessage("The old password is wrong");
            logger.debug(String.format("user: %s, error: %s", account, response.getMessage()));
            return response;
        }

        if (newPassword.length() < User.PASSWORD_LENGTH) {
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

        HashMap<String, String> data = new HashMap<>(2);
        data.put("token", JwtUtil.sign(account, user.getPassword()));
        data.put("refreshToken", JwtUtil.signRefreshToken(account, user.getPassword()));
        response.setData(data);
        response.setMessage("Reset password success");
        return response;
    }

    /**
     * @return 注册结果（包含token）
     * @Param signInUser
     * @Param rePassword
     * @Param UUID
     * @Description 用户注册api
     * @Date 2020/1/31 01:40
     */
    @ApiOperation(value = "用户注册", notes = "用户注册的api", tags = "UserController",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "signInUser", value = "用户信息", paramType ="body",required = true, dataType = "UserSign"),
            @ApiImplicitParam(name = "UUID", value = "用户特征UUID", paramType ="query",required = true, dataType = "String"),
    })
    @ApiResponses({
            @ApiResponse(code=ResponseCode.SUCCESS,message="成功",response=Response.class),
            @ApiResponse(code=ResponseCode.FAIL,message="失败",response=Response.class),
            @ApiResponse(code=ResponseCode.ERROR,message="错误",response=Response.class)
    })
    @PostMapping("/sign-in")
    public Response signIn(@RequestBody @Valid UserSign signInUser,
                           @RequestParam(value = "UUID") String UUID) throws NoSuchAlgorithmException {
    /*
      注册逻辑
      1. 检查输入参数的合法性
      2. 检查是否有用户已经使用改账户
      3. 注册账号，创建新的用户保存至数据库
    */
    // TODO 创建了新的实体类接受不了参数，出一份这几个接口的文档 或者 配置一下swigger

        Response res = new Response();

        if (!userDao.checkEmailInRedis(signInUser.getEmail(), UUID)) {
            res.setCode(ResponseCode.FAIL);
            res.setMessage("email has not been checked");
            return res;
        }

        if (!signInUser.getPassword().equals(signInUser.getRePassword())) {
            res.setCode(ResponseCode.FAIL);
            res.setMessage("两次密码不一致");
            return res;
        }

        User user = userDao.findUserByUserAccount(signInUser.getAccount());
        if (user != null) {
            res.setCode(ResponseCode.FAIL);
            res.setMessage("该账号已存在");
            return res;
        }

        // 创建用户
        user = new User(signInUser.getAccount(), signInUser.getPassword());
        user.setEmail(signInUser.getEmail());
        user.setNickname(signInUser.getAccount());
        userDao.saveUser(user);

        HashMap<String, String> resData = new HashMap<>(4);
        resData.put("token", JwtUtil.sign(user.getAccount(), user.getPassword()));
        resData.put("refreshToken", JwtUtil.signRefreshToken(signInUser.getAccount(), user.getPassword()));
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
        String accountKey = "account";
        String passwordKey = "password";
        Response response = new Response();
        if (map.get(accountKey) == null || map.get(passwordKey) == null) {
            response.setCode(ResponseCode.ERROR);
            response.setMessage("请输入完整参数");
            logger.debug(response.getMessage());
            return response;
        }
        String account = map.get("account").toString();
        String password = map.get("password").toString();
        User user = userDao.findUserByUserAccount(account);

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
        HashMap<String, Object> resData = new HashMap<>(3);
        resData.put("token", JwtUtil.sign(user.getAccount(), user.getPassword()));
        resData.put("refreshToken", JwtUtil.signRefreshToken(account, user.getPassword()));
        resData.put("user", user);
        response.setData(resData);
        logger.info(String.format("user %s is login:", user.getNickname()));
        return response;
    }

    @PostMapping("/refresh-token")
    public Response refreshTokenHandle(@RequestBody Map<String, Object> map) {
        String refreshTokenKey = "refreshToken";
        Response response = new Response();
        if (map.get(refreshTokenKey) == null) {
            response.setCode(ResponseCode.ERROR);
            response.setMessage("refreshToken is required");
            logger.debug(response.getMessage());
            return response;
        }
        String refreshToken = (String) map.get("refreshToken");
        String account = JwtUtil.getAccount(refreshToken);
        User user = userDao.findUserByUserAccount(account);
        if (user == null) {
            response.setCode(ResponseCode.FAIL);
            response.setMessage("The user is not exist");
            logger.debug(response.getMessage());
            return response;
        }
        Map<String, Object> data = new HashMap<>(2);
        if (JwtUtil.verify(refreshToken, user.getAccount(), user.getPassword())) {
            String newToken = JwtUtil.sign(user.getAccount(), user.getPassword());
            String newRefreshToken = JwtUtil.signRefreshToken(user.getAccount(), user.getPassword());
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
