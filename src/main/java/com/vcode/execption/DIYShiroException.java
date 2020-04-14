package com.vcode.execption;

import com.vcode.common.ResponseCode;
import com.vcode.entity.Response;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author moyee
 * @version 1.0.0
 * @Description 自定义异常
 * @Date 2020/2/25
 */
@RestControllerAdvice
public class DIYShiroException {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(ShiroException.class)
  public Response handle401(ShiroException e) {
    logger.error(e.toString());
    Response response = new Response();
    response.setCode(ResponseCode.FAIL);
    response.setMessage("Unauthorized");
    Map<String, Object> map = new HashMap<>();
    map.put("401", e.getMessage());
    response.setData(map);
    return response;
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(UnauthorizedException.class)
  public Response handle403() {
    Response response = new Response();
    response.setCode(ResponseCode.FAIL);
    response.setMessage("Access Denied");
    Map<String, Object> map = new HashMap<>();
    map.put("403", "forbidden");
    response.setData(map);
    return response;
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response globalException(HttpServletRequest request, Throwable e) {
    logger.error(e.toString());
    Response response = new Response();
    response.setCode(ResponseCode.ERROR);
    response.setMessage("Server Bad");
    response.setError(getStatus(request).value() + " " + e.getMessage());
    return response;
  }

  private HttpStatus getStatus(HttpServletRequest request) {
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    if (statusCode == null) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return HttpStatus.valueOf(statusCode);
  }
}
