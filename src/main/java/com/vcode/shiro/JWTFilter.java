package com.vcode.shiro;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */
public class JWTFilter extends BasicHttpAuthenticationFilter {
  private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  @Override
  protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
    HttpServletRequest req = (HttpServletRequest) request;
    String authorization = req.getHeader("Authorization");
    return authorization != null;
  }

  @Override
  protected boolean executeLogin(ServletRequest request, ServletResponse response) {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String authorization = httpServletRequest.getHeader("Authorization");
    JWTToken token = new JWTToken(authorization);
    getSubject(request, response).login(token);
    return true;
  }

  @Override
  protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
    httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
    httpServletResponse.setHeader(
            "Access-Control-Allow-Headers",
            httpServletRequest.getHeader("Access-Control-Request-Headers"));
    // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
    if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
      httpServletResponse.setStatus(HttpStatus.OK.value());
      return false;
    }
    return super.preHandle(request, response);
  }

}
