package com.vcode.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcode.common.ResponseCode;
import com.vcode.entity.CrawlProblemTask;
import com.vcode.entity.Response;
import com.vcode.impl.CrawlProblemTaskDaoImpl;
import com.vcode.shiro.ShiroCommon;
import com.vcode.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author moyee
 * @version 1.0.0
 * @Description admin爬虫接口
 * @Date
 */
@RestController
@RequestMapping("/admin/crawl")
public class AdminCrawlController {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final CrawlProblemTaskDaoImpl crawlProblemTaskDao;

  @Autowired
  public AdminCrawlController(CrawlProblemTaskDaoImpl crawlProblemTaskDao) {
    this.crawlProblemTaskDao = crawlProblemTaskDao;
  }


  /**
   * @param taskId task_id
   * @return com.vcode.entity.Response
   * @Description 获取task详情
   * @Date 2020/4/19 17:55
   */
  @GetMapping()
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response getCrawlTaskDetail(@RequestParam(value = "taskId") String taskId) {
    Response response = new Response();
    CrawlProblemTask crawlProblemTask = crawlProblemTaskDao.findTaskByOriginTaskId(taskId);
    logger.debug(String.format("task: %s is accessed", crawlProblemTask.getTaskId()));
    Map<String, Object> map = new HashMap<>(1);
    map.put("task", crawlProblemTask);
    response.setData(map);
    return response;
  }

  /**
   * @param crawlProblemTask task所要求的参数
   * @return com.vcode.entity.Response
   * @Description 创建task
   * @Date 2020/4/19 17:56
   */
  @PostMapping()
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response createCrawlTask(@RequestBody @Valid CrawlProblemTask crawlProblemTask) {
    Response response = new Response();
    logger.debug(String.format("task: %s", crawlProblemTask.getTaskId()));
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);
    crawlProblemTask.setAuthor(account);
    try {
      CrawlProblemTask createdTask = crawlProblemTaskDao.createTask(crawlProblemTask);
      logger.info(String.format("create task: %s", createdTask.toJsonString()));
      response.setData(createdTask);
    } catch (JsonProcessingException e) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage(String.format("create task error: %s", e.getMessage()));
      response.setError(e.getMessage());
      logger.error(response.getMessage());
    }
    return response;
  }


  /**
   * @param page   页码
   * @param size   一页大小
   * @param search 查询条件
   * @return com.vcode.entity.Response
   * @Description 获取一页task列表
   * @Date 2020/4/19 17:57
   */
  @GetMapping("/list")
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response getCrawlTaskList(@RequestParam(value = "page") int page,
                                   @RequestParam(value = "size") int size,
                                   @RequestParam(value = "search") String search) {
    Response response = new Response();
    page--;
    logger.debug(String.format("find task list, page: %d,size: %d,search: %s", page, size, search));
    List<CrawlProblemTask> crawlProblemTaskList = crawlProblemTaskDao.findTaskList(page, size, search);
    Map<String, Object> map = new HashMap<>(2);
    map.put("taskList", crawlProblemTaskList);
    map.put("total", crawlProblemTaskDao.count(search));
    response.setData(map);
    return response;
  }

  @PostMapping("/clone")
  @RequiresRoles(
          value = {ShiroCommon.ROLE_ADMIN, ShiroCommon.ROLE_TEACHER, ShiroCommon.ROLE_CAPTION},
          logical = Logical.OR)
  public Response cloneCrawTask(@RequestBody @Valid CrawlProblemTask crawlProblemTask) {
    Response response = new Response();
    CrawlProblemTask newTask = new CrawlProblemTask();
    newTask.setOj(crawlProblemTask.getOj());
    newTask.setKey(crawlProblemTask.getKey());
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);
    newTask.setAuthor(account);
    try {
      CrawlProblemTask createdTask = crawlProblemTaskDao.createTask(newTask);
      response.setData(createdTask);
      logger.info(String.format("clone task success: %s", createdTask.toJsonString()));
    } catch (JsonProcessingException e) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage(String.format("clone task error: %s", e.getMessage()));
      response.setError(e.getMessage());
      logger.error(response.getMessage());
    }
    return response;
  }
}
