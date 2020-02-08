package com.vcode.controller;

import com.vcode.Impl.ProblemDaoImpl;
import com.vcode.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/problem")
public class ProblemController {

  @Autowired
  private ProblemDaoImpl problemDao;

  private Logger log = Logger.getLogger("ProblemController");

  @GetMapping("/list")
  public Response getProblemList() {
    return null;
  }

  @GetMapping("/detail")
  public Response getProblemDetail() {
    return null;
  }

  @PostMapping("/edit")
  public Response editProblem() {
    return null;
  }

  @PostMapping("/create")
  public Response createProblem() {
    return null;
  }

  @PostMapping("/create-list")
  public Response createProblemByList() {
    return null;
  }

  @PostMapping("/delete")
  public Response deleteProblemByOriginId() {
    return null;
  }
}
