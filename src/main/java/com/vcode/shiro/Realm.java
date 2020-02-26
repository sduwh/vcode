package com.vcode.shiro;

import com.vcode.Impl.VUserDaoImpl;
import com.vcode.entity.VUser;
import com.vcode.util.JWTUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
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
  private static final Logger LOGGER = LogManager.getLogger(Realm.class);

  private VUserDaoImpl userDao;

  @Autowired
  public void setUserService(VUserDaoImpl userDao) {
    this.userDao = userDao;
  }

  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof JWTToken;
  }

  /**
   * 获得用户权限和角色
   * @param principals
   * @return
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    String account = JWTUtil.getAccount(principals.toString());
    VUser user = userDao.findUserByUserAccount(account);
    SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
    simpleAuthorizationInfo.addRole(user.getRole());
    Set<String> permission = new HashSet<>(Arrays.asList(user.getPermission().split(",")));
    simpleAuthorizationInfo.addStringPermissions(permission);
    return simpleAuthorizationInfo;
  }

  /**
   * 身份校验
   * @param auth
   * @return
   * @throws AuthenticationException
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
    String token = (String) auth.getCredentials();
    // 解密获得username，用于和数据库进行对比
    String username = JWTUtil.getAccount(token);
    if (username == null) {
      throw new AuthenticationException("token invalid");
    }

    VUser userBean = userDao.findUserByUserAccount(username);
    if (userBean == null) {
      throw new AuthenticationException("User didn't existed!");
    }

    if (! JWTUtil.verify(token, username, userBean.getPassword())) {
      throw new AuthenticationException("Username or password error");
    }

    return new SimpleAuthenticationInfo(token, token, "realm");
  }

}
