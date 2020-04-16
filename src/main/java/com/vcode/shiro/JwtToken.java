package com.vcode.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */
public class JwtToken implements AuthenticationToken {

  // 密钥
  private final String token;

  public JwtToken(String token) {
    this.token = token;
  }

  @Override
  public Object getPrincipal() {
    return token;
  }

  @Override
  public Object getCredentials() {
    return token;
  }
}