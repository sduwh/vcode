package com.vcode.controller;

import com.vcode.Impl.TagDaoImpl;
import com.vcode.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    return null;
  }

  // 根据name创建一个tag
  @PostMapping("/create")
  public Response createTag() {
    return null;
  }

  // 根据name返回name下的所有problems
  @GetMapping("/sub-problems")
  public Response getSubProblems() {
    return null;
  }

}
