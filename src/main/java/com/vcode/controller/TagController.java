package com.vcode.controller;

import com.vcode.Impl.TagDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Response;
import com.vcode.entity.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/tag")
public class TagController {

  @Autowired
  private TagDaoImpl tagDao;

  private Logger log = Logger.getLogger("TagController");

  /*
 罗列逻辑
 返回所有的tag标签
 1. 使用findAll()方法列出全部tag
 2. 传递给res令其返回
  */
  @GetMapping("/all")
  public Response getAllTags() {
    /**
     * @Description 返回全部标签api
     * @return 数据库中已保存的全部的tag
     */
    Response res = new Response();
    List<Tag> tagList = tagDao.findAll();
    res.setData(tagList);
    return res;
  }

  /*
 新建逻辑
 根据name创建一个tag
 1. 检查tag是否存在
 2. 创建新的tag保存至数据库
  */
  @PostMapping("/create")
  public Response createTag(@RequestBody @Valid Tag tag) {
    /**
     * @Description 创建tag的api
     * @param name tag的名字
     * @param problems tag的名字下对应的问题列表
     * @return 创建结果
     */
    Response res = new Response();
    if (tagDao.isExist(tag)) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("此tag已存在");
      return res;
    }//检查tag名字是否重复
    tagDao.saveTag(tag);
    res.setData(tag);
    return res;
  }

  /*
 罗列逻辑
 根据name返回name下的所有problems
 1. 检查tag是否存在
 2. 取出该tag相关信息
 3. 列出tag下保存的problems
  */
  @GetMapping("/sub-problems")
  public Response getSubProblems(@RequestParam(value = "name") String name) {
    /**
     * @Description 返回全部问题api
     * @param name tag的名字
     * @return 对应的问题列表
     */
    Response res = new Response();
    if (tagDao.findTagByName(name) == null) {
      res.setCode(ResponseCode.ERROR);
      res.setMessage("The name is invalid");
      return res;
    }//检查输入的tag名是否正确
    Tag tag = tagDao.findTagByName(name);
    LinkedList<ObjectId> probList = tag.getProblems();//取出problems
    res.setData(probList);
    return res;
  }

}
