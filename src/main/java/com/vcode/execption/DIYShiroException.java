package com.vcode.execption;

import com.vcode.common.ResponseCode;
import com.vcode.entity.Response;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
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

  /**
   * @Description 处理Shiro权限拦截异常
   * @Date 2020/2/26 15:22
   * @return java.util.Map<java.lang.String,java.lang.Object>
   */

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(ShiroException.class)
  public Response handle401(ShiroException e) {
    Response response = new Response();
    response.setCode(ResponseCode.FAIL);
    response.setMessage("permission denied");
    Map<String, Object> map = new HashMap<>();
    map.put("401",  e.getMessage());
    response.setData(map);
    return response;
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(UnauthorizedException.class)
  public Response handle401() {
    Response response = new Response();
    response.setCode(ResponseCode.FAIL);
    response.setMessage("permission denied");
    Map<String, Object> map = new HashMap<>();
    map.put("401",  "Unauthorized");
    response.setData(map);
    return response;
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response globalException(HttpServletRequest request, Throwable ex) {
    Response response = new Response();
    response.setCode(ResponseCode.ERROR);
    response.setMessage("permission denied");
    response.setError(getStatus(request).value()+ " " +ex.getMessage());
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
