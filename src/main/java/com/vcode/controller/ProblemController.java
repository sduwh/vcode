package com.vcode.controller;

import com.vcode.Impl.ProblemDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Problem;
import com.vcode.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/problem")
public class ProblemController {

  @Autowired
  private ProblemDaoImpl problemDao;

  private Logger log = Logger.getLogger("ProblemController");

  @GetMapping("/list")
  public Response getProblemList(@RequestParam(value = "page") int page,
                                 @RequestParam(value = "size") int size) {
    Response response = new Response();
    System.out.println(page);
    if (page < 1 || size < 1) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("page or size must be greater than zero");
      return response;
    }

    List<Problem> problemList = problemDao.findProblemsByPageAndSize(page, size);
    response.setData(problemList);
    return response;
  }

  @GetMapping("/detail")
  public Response getProblemDetail(@RequestParam(value = "originId") String originId) {
    Response response = new Response();
    if (originId == null || originId.length() == 0) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("originId 不能为空");
      return response;
    }
    Problem problem = problemDao.findByOriginId(originId);
    if (problem == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("Problem is not exit");
      return response;
    }
    response.setData(problem);
    return response;
  }

  @PostMapping("/edit")
  public Response editProblem(@RequestBody @Valid Problem problem) {
    Response res = new Response();
    if (problemDao.isExist(problem)) {
      problemDao.updateProblem(problem);
      return res;
    }
    res.setCode(ResponseCode.FAIL);
    res.setMessage("此problem不存在");
    return res;
  }

  @PostMapping("/create")
  public Response createProblem(@RequestBody @Valid Problem problem) {
    Response res = new Response();
    if (problemDao.isExist(problem)) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("此problem已存在");
      return res;
    }
    // 添加origin前缀
    problem.setOriginId(problem.getOriginId());
    problemDao.saveProblem(problem);
    res.setData(problem);
    return res;
  }

  @PostMapping("/delete")
  public Response deleteProblemByOriginId(@RequestBody Map<String, String> map) {
    Response res = new Response();
    String originId = map.get("originId");
    if (originId == null) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("originId is required");
      return res;
    }
    problemDao.deleteProblemByOriginId(originId);
    return res;
  }
}
