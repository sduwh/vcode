package com.vcode.controller;

import com.vcode.common.ResponseCode;
import com.vcode.entity.Group;
import com.vcode.entity.Response;
import com.vcode.entity.Submission;
import com.vcode.impl.GroupDaoImpl;
import com.vcode.impl.SubmissionDaoImpl;
import com.vcode.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/group")
public class GroupController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final GroupDaoImpl groupDao;
  private final SubmissionDaoImpl submissionDao;

  @Autowired
  public GroupController(GroupDaoImpl groupDao, SubmissionDaoImpl submissionDao) {
    this.groupDao = groupDao;
    this.submissionDao = submissionDao;
  }

  /**
   * @return 数据库中已保存的全部的tag
   * @Description 返回全部标签api
   */
  @GetMapping("/all")
  public Response getAllGroups() {
    /*
     罗列逻辑
     返回所有的group
     1. 使用findAll()方法列出全部group
     2. 传递给res令其返回
    */
    Response res = new Response();
    List<Group> groupList = groupDao.findAll();
    res.setData(groupList);
    return res;
  }

  /**
   * @param group group entity
   * @return 创建结果
   * @Description 创建group的api
   */
  @PostMapping("/create")
  public Response createGroup(@RequestBody @Valid Group group) {
    /*
     新建逻辑
     根据name创建一个group
     1. 检查group是否存在
     2. 创建新的group保存至数据库
    */
    Response response = new Response();
    if (groupDao.isExist(group)) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage(String.format("The group: %s is exist", group.getGroup_name()));
      logger.debug(response.getMessage());
      return response;
    }//检查group名字是否重复
    groupDao.saveGroup(group);
    logger.info(String.format("The group: %s is created", group.getGroup_name()));
    response.setData(group);
    return response;
  }


  /**
   * @param group_name group的名字
   * @return 对应的user列表
   * @Description 返回全部user的api
   */
  @GetMapping("/sub-users")
  public Response getSubUsers(@RequestParam(value = "group_name") String group_name) {
    /*
     罗列逻辑
     根据name返回name下的所有user
     1. 检查group是否存在
     2. 取出该group相关信息
     3. 列出group下保存的users
    */
    Response response = new Response();
    if (groupDao.findGroupByName(group_name) == null) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("The name is invalid");
      logger.debug(response.getMessage());
      return response;
    }
    Group group = groupDao.findGroupByName(group_name);
    LinkedList<ObjectId> userList = group.getMembers();
    response.setData(userList);
    return response;
  }

  /**
   * @param group_name group的名字
   * @return 对应的contest列表
   * @Description 返回全部contest的api
   */
  @GetMapping("/sub-contests")
  public Response getSubContests(@RequestParam(value = "group_name") String group_name) {
    /*
     罗列逻辑
     根据name返回name下的所有contest
     1. 检查group是否存在
     2. 取出该group相关信息
     3. 列出group下保存的contests
    */
    Response response = new Response();
    if (groupDao.findGroupByName(group_name) == null) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("The name is invalid");
      logger.debug(response.getMessage());
      return response;
    }
    Group group = groupDao.findGroupByName(group_name);
    LinkedList<ObjectId> contestList = group.getContests();
    response.setData(contestList);
    return response;
  }

  /**
   * @return 对应的提交细节
   * @Description 返回全部提交情况的api
   */
  @GetMapping("/sub-details")
  public Response getSubmissionForGroup(@RequestParam(value = "group_name") String group_name,
                                        @RequestParam(value = "page") int page,
                                        @RequestParam(value = "size") int size,
                                        @RequestParam(value = "search") String search) {
    Response response = new Response();
    Group group = groupDao.findGroupByName(group_name);
    if (group == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage(String.format("Group: %s is not exist", group_name));
      logger.debug(response.getMessage());
      return response;
    }
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JwtUtil.getAccount(token);
    page--;
    List<Submission> submissionList = submissionDao.findGroupSubmission(group.getGroup_name(), page, size, search);
    logger.info(String.format("user: %s has access the submission list of group: %s, page: %d, size: %d, search: " +
            "%s", account, group_name, page, size, search));
    Map<String, Object> dataMap = new HashMap<>(2);
    dataMap.put("submissionList", submissionList);
    dataMap.put("total", submissionDao.countGroupSubmission(group.getGroup_name(), search));
    response.setData(dataMap);
    return response;
  }

}
