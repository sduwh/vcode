package com.vcode.controller;

import com.vcode.Impl.TagDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Response;
import com.vcode.entity.Tag;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

  private TagDaoImpl tagDao;

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  public TagController(TagDaoImpl tagDao) {
    this.tagDao = tagDao;
  }


  /**
   * @return 数据库中已保存的全部的tag
   * @Description 返回全部标签api
   */
  @GetMapping("/all")
  public Response getAllTags() {
    /*
     罗列逻辑
     返回所有的tag标签
     1. 使用findAll()方法列出全部tag
     2. 传递给res令其返回
    */
    Response res = new Response();
    List<Tag> tagList = tagDao.findAll();
    res.setData(tagList);
    return res;
  }


  /**
   * @param tag tag entity
   * @return 创建结果
   * @Description 创建tag的api
   */
  @PostMapping("/create")
  public Response createTag(@RequestBody @Valid Tag tag) {
    /*
     新建逻辑
     根据name创建一个tag
     1. 检查tag是否存在
     2. 创建新的tag保存至数据库
    */
    Response response = new Response();
    if (tagDao.isExist(tag)) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage(String.format("The tag: %s is exist", tag.getName()));
      logger.debug(response.getMessage());
      return response;
    }//检查tag名字是否重复
    tagDao.saveTag(tag);
    logger.info(String.format("The tag: %s is created", tag.getName()));
    response.setData(tag);
    return response;
  }


  /**
   * @param name tag的名字
   * @return 对应的问题列表
   * @Description 返回全部问题api
   */
  @GetMapping("/sub-problems")
  public Response getSubProblems(@RequestParam(value = "name") String name) {
    /*
     罗列逻辑
     根据name返回name下的所有problems
     1. 检查tag是否存在
     2. 取出该tag相关信息
     3. 列出tag下保存的problems
    */
    Response response = new Response();
    if (tagDao.findTagByName(name) == null) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("The name is invalid");
      logger.debug(response.getMessage());
      return response;
    }
    Tag tag = tagDao.findTagByName(name);
    LinkedList<ObjectId> probList = tag.getProblems();
    response.setData(probList);
    return response;
  }
}
