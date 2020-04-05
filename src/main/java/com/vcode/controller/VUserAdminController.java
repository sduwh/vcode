package com.vcode.controller;

import com.vcode.Impl.VUserDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Response;
import com.vcode.entity.VUser;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author moyee
 * @version 1.0.0
 * @Description vuser admin controller
 * @Date 2020.02.26
 */
@RestController
@RequestMapping("/admin/user")
public class VUserAdminController {

  private VUserDaoImpl userDao;

  private Logger log = Logger.getLogger("UserAdminController");

  @Autowired
  public VUserAdminController(VUserDaoImpl vUserDao) {
    this.userDao = vUserDao;
  }

  @GetMapping("/list")
  @RequiresRoles("admin")
  public Response getUserList(@RequestParam(value = "page") int page,
                              @RequestParam(value = "size") int size,
                              @RequestParam(value = "search") String search) {
    Response response = new Response();
    if (page < 1 || size < 1) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("page or size must be greater than zero");
      return response;
    }
    page--;
    List<VUser> userList = userDao.findUsers(page, size, search);
    Map<String, Object> data = new HashMap<>();
    data.put("userList", userList);
    data.put("total", userDao.count(search));
    response.setData(data);
    return response;
  }

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
    List<VUser> userList = userDao.findAdmins(page, size, search);
    Map<String, Object> data = new HashMap<>();
    data.put("adminList", userList);
    data.put("total", userDao.countAdmins(search));
    response.setData(data);
    return response;
  }


  @DeleteMapping("/")
  @RequiresRoles("admin")
  public Response deleteUser(@RequestParam(value = "account") String account) {
    Response response = new Response();
    userDao.deleteUserByAccount(account);
    return response;
  }

  @PostMapping("/info")
  @RequiresRoles("admin")
  public Response editUserInfo(@RequestBody @Valid VUser user) {
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
