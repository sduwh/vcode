package com.vcode.config;

import com.vcode.entity.User;
import com.vcode.impl.UserDaoImpl;
import com.vcode.shiro.ShiroCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.applet.AppletContext;
import java.security.NoSuchAlgorithmException;

/**
 * @author moyee
 * @version 1.0.0
 * @Description 初始化
 * @Date
 */
@Component
public class SystemInitializer implements ApplicationListener<ContextRefreshedEvent> {
  private final UserDaoImpl userDao;

  @Autowired
  public SystemInitializer(UserDaoImpl userDao){
    this.userDao = userDao;
  }
  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    User user =  userDao.findUserByUserAccount("admin");
    if (user == null){
      try {
        user = new User("admin", "vcodeadmin");
        user.setRole(ShiroCommon.ROLE_ADMIN);
        userDao.saveUser(user);
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      }
    }
  }
}
