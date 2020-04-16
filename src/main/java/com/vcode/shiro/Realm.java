package com.vcode.shiro;

import com.vcode.impl.UserDaoImpl;
import com.vcode.entity.User;
import com.vcode.util.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */

@Service
public class Realm extends AuthorizingRealm {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private UserDaoImpl userDao;

  @Autowired
  public void setUserService(UserDaoImpl userDao) {
    this.userDao = userDao;
  }

  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof JwtToken;
  }

  /**
   * 获得用户权限和角色
   * @param principals jwt
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    String account = JwtUtil.getAccount(principals.toString());
    User user = userDao.findUserByUserAccount(account);
    SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
    simpleAuthorizationInfo.addRole(user.getRole());
    Set<String> permission = new HashSet<>(Arrays.asList(user.getPermission().split(",")));
    simpleAuthorizationInfo.addStringPermissions(permission);
    return simpleAuthorizationInfo;
  }

  /**
   * 身份校验
   * @param auth twt
   * @throws AuthenticationException
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
    String token = (String) auth.getCredentials();
    // 解密获得username，用于和数据库进行对比
    String username = JwtUtil.getAccount(token);
    if (username == null) {
      throw new AuthenticationException("token invalid");
    }

    User userBean = userDao.findUserByUserAccount(username);
    if (userBean == null) {
      throw new AuthenticationException("User didn't existed!");
    }

    if (! JwtUtil.verify(token, username, userBean.getPassword())) {
      throw new AuthenticationException("Username or password error");
    }

    return new SimpleAuthenticationInfo(token, token, "realm");
  }
}
