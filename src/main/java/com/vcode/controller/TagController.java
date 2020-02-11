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

  // 返回所有的tag标签
  @GetMapping("/all")
  public Response getAllTags() {
    Response res = new Response();
    List<Tag> tagList = tagDao.findAll();
    res.setData(tagList);
    return res;
  }

  // 根据name创建一个tag
  @PostMapping("/create")
  public Response createTag(@RequestBody @Valid Tag tag) {
    Response res = new Response();
    if (tagDao.isExist(tag)) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("此Tag已存在");
      return res;
    }
    tagDao.saveTag(tag);
    res.setData(tag);
    return res;
  }

  // 根据name返回name下的所有problems
  @GetMapping("/sub-problems")
  public Response getSubProblems(@RequestParam(value = "name") String name) {
    Response res = new Response();
    if (tagDao.findTagByName(name) == null) {
      res.setCode(ResponseCode.ERROR);
      res.setMessage("The name is invalid");
    }
    Tag tag = tagDao.findTagByName(name);
    LinkedList<ObjectId> probList = tag.getProblems();
    res.setData(probList);
    return null;
  }

}
