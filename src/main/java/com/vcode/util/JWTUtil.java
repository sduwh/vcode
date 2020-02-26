package com.vcode.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.logging.Logger;

public class JWTUtil {
  private static final Logger log = Logger.getLogger("jwt");

  // 有效时间120分钟
  private static final long EXPIRE_TIME = 120 * 60 * 1000;

  /**
   * @param account 用户账号
   * @param secret  用户密码
   * @return a token
   * @Description 签名加密token
   * @Date 2020/1/30 18:59
   */
  public static String sign(String account, String secret) {
    Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
    Algorithm algorithm = Algorithm.HMAC256(secret);
    return JWT.create()
            .withClaim("account", account)
            .withExpiresAt(date)
            .sign(algorithm);
  }

  /**
   * @param token   加密token
   * @param account 用户账号
   * @param secret  用户密码
   * @return token 是否合法
   * @Description 检查token是否合法（用户信息以及时效）
   * @Date 2020/1/30 19:00
   */
  public static Boolean verify(String token, String account, String secret) {
    Algorithm algorithm = Algorithm.HMAC256(secret);
    JWTVerifier jwtVerifier = JWT.require(algorithm)
            .withClaim("account", account)
            .build();
    try {
      DecodedJWT jwt = jwtVerifier.verify(token);
      if (!account.equals(getAccount(token))){
        return false;
      }
      if (jwt.getExpiresAt().before(new Date(System.currentTimeMillis()))) {
        // 时效已过期
        return false;
      }
    } catch (JWTVerificationException e) {
      // token 非法
      log.warning(e.toString());
      return false;
    }
    return true;
  }

  /**
   * @Description get user's account in token
   * @Date 2020/2/26 16:15
   * @param token 1
   * @return java.lang.String
   */
  public static String getAccount(String token) {
    try {
      DecodedJWT jwt = JWT.decode(token);
      return jwt.getClaim("account").asString();
    } catch (JWTDecodeException e) {
      return null;
    }
  }
}
