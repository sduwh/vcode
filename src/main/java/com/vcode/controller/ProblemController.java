package com.vcode.controller;

import com.vcode.Impl.ProblemDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Problem;
import com.vcode.entity.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/problem")
public class ProblemController {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private ProblemDaoImpl problemDao;

  @Autowired
  public ProblemController(ProblemDaoImpl problemDao) {
    this.problemDao = problemDao;
  }

  /**
   * @param page    页码
   * @param size    一页的容量
   * @param search  查询条件
   * @param visible 题目可见
   * @return com.vcode.entity.Response
   * @Description 获取problems列表
   * @Date 2020/2/11 15:33
   */
  @GetMapping("/list")
  public Response getProblemList(@RequestParam(value = "page") int page,
                                 @RequestParam(value = "size") int size,
                                 @RequestParam(value = "search") String search,
                                 @RequestParam(value = "visible") boolean visible) {
    Response response = new Response();
    if (page < 1 || size < 1) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("page or size must be greater than zero");
      logger.debug(response.getMessage());
      return response;
    }
    // 数据库中的分页从0开始
    page--;
    List<Problem> problemList = problemDao.findProblems(page, size, search, visible);
    Map<String, Object> resMap = new HashMap<>();
    resMap.put("problem_list", problemList);
    resMap.put("total", problemDao.count(search, true));
    response.setData(resMap);
    return response;
  }

  /**
   * @param problemOriginId problem的唯一标示
   * @return com.vcode.entity.Response
   * @Description 获取problems的详情
   * @Date 2020/2/11 15:34
   */
  @GetMapping("/detail")
  public Response getProblemDetail(@RequestParam(value = "originId") String problemOriginId) {

    Response response = new Response();
    if (problemOriginId == null || problemOriginId.length() == 0) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("The params originId is required");
      logger.debug(response.getMessage());
      return response;
    }
    Problem problem = problemDao.findByOriginId(problemOriginId);
    if (problem == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage(String.format("Problem: %s is not exit", problemOriginId));
      logger.debug(response.getMessage());
      return response;
    }
    logger.info(String.format("Problem: %s has been accessed", problemOriginId));
    response.setData(problem);
    return response;
  }
}
