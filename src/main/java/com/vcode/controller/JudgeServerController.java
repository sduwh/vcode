package com.vcode.controller;

import com.vcode.entity.Response;
import com.vcode.impl.JudgeServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author moyee
 * @version 1.0.0
 * @Description judge controller
 * @Date
 */
@RestController
@RequestMapping("/admin/judge")
public class JudgeServerController {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final JudgeServerImpl judgeServerDao;

  @Autowired
  public JudgeServerController(JudgeServerImpl judgeServer){
    this.judgeServerDao = judgeServer;
  }

  @GetMapping("/oj")
  public Response getOjList(){
    Response response = new Response();
    List<String> ojList = judgeServerDao.getOjSupport();
    logger.debug(String.format("oj list: %s", ojList.toString()));
    response.setData(ojList);
    return response;
  }
}
