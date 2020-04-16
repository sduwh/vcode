package com.vcode.controller;

import com.vcode.impl.AboutDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.About;
import com.vcode.entity.Response;
import com.vcode.shiro.ShiroCommon;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author moyee
 */
@RestController
@RequestMapping("/about")
public class AboutController {

  private final AboutDaoImpl aboutDao;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  public AboutController(AboutDaoImpl aboutDao) {
    this.aboutDao = aboutDao;
  }

  @PostMapping("/doc")
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response editDoc(@RequestBody @Valid About about) {

    Response response = new Response();

    aboutDao.updateAbout(about);
    logger.debug("About doc is update: " + about.getDoc());
    return response;
  }

  @GetMapping("/doc")
  public Response getDoc() {
    Response response = new Response();

    if (!aboutDao.isExist()) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("doc is not exist");
      logger.debug(response.getMessage());
      response.setData("");
    } else {
      response.setData(aboutDao.getAbout().getDoc());
      logger.debug(String.format("get doc: %s", response.getData()));
    }
    return response;
  }
}
