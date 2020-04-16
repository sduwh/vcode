package com.vcode.controller;

import com.vcode.impl.UserDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Response;
import com.vcode.entity.User;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author moyee
 * @version 1.0.0
 * @Description vuser admin controller
 * @Date 2020.02.26
 */
@RestController
@RequestMapping("/admin/user")
public class UserAdminController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final UserDaoImpl userDao;

  @Autowired
  public UserAdminController(UserDaoImpl vUserDao) {
    this.userDao = vUserDao;
  }

  /**
   * @param page   page's num
   * @param size   page's size
   * @param search filter data
   * @return com.vcode.entity.Response
   * @Description get user list in admin
   * @Date 2020/4/11 11:15
   */
  @GetMapping("/list")
  @RequiresRoles("admin")
  public Response getUserList(@RequestParam(value = "page") int page,
                              @RequestParam(value = "size") int size,
                              @RequestParam(value = "search") String search) {
    Response response = new Response();
    if (page < 1 || size < 1) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("page or size must be greater than zero");
      logger.debug(response.getMessage());
      return response;
    }
    page--;
    List<User> userList = userDao.findUsers(page, size, search);
    Map<String, Object> data = new HashMap<>();
    data.put("userList", userList);
    data.put("total", userDao.count(search));
    response.setData(data);
    return response;
  }

  /**
   * @param page   page's num
   * @param size   page's size
   * @param search filter data
   * @return com.vcode.entity.Response
   * @Description get admin list in admin
   * @Date 2020/4/11 11:16
   */
  @GetMapping("/admin-list")
  @RequiresRoles("admin")
  public Response getAdminList(@RequestParam(value = "page") int page,
                               @RequestParam(value = "size") int size,
                               @RequestParam(value = "search") String search) {
    Response response = new Response();
    if (page < 1 || size < 1) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("page or size must be greater than zero");
      return response;
    }
    page--;
    List<User> userList = userDao.findAdmins(page, size, search);
    Map<String, Object> data = new HashMap<>();
    data.put("adminList", userList);
    data.put("total", userDao.countAdmins(search));
    response.setData(data);
    return response;
  }

  /**
   * @param account user account
   * @return com.vcode.entity.Response
   * @Description delete user
   * @Date 2020/4/11 11:17
   */
  @DeleteMapping("/")
  @RequiresRoles("admin")
  public Response deleteUser(@RequestParam(value = "account") String account) {
    Response response = new Response();
    userDao.deleteUserByAccount(account);
    return response;
  }


  /**
   * @param user user entity
   * @return com.vcode.entity.Response
   * @Description change user info
   * @Date 2020/4/11 11:17
   */
  @PostMapping("/info")
  @RequiresRoles("admin")
  public Response editUserInfo(@RequestBody @Valid User user) {
    Response response = new Response();

    if (userDao.isNicknameExist(user)) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("nickname is exist");
      return response;
    }
    if (userDao.isEmailExist(user)) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("email is exist");
      return response;
    }

    String err = userDao.updateUser(user);
    if (err != null) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage(err);
      return response;
    }
    return response;
  }
}
