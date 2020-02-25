package com.vcode.controller;

import com.vcode.Handler.TestCaseHandler;
import com.vcode.Impl.ProblemDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.config.TestCaseConfig;
import com.vcode.entity.Problem;
import com.vcode.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/problem")
public class ProblemController {

  @Autowired
  private ProblemDaoImpl problemDao;

  private Logger log = Logger.getLogger("ProblemController");


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
      return response;
    }
    // 数据库中的分页从0开始
    page--;
    List<Problem> problemList = problemDao.findProblems(page, size, search, visible);
    HashMap<String, Object> resMap = new HashMap<>();
    resMap.put("problem_list", problemList);
    resMap.put("total", problemDao.count(search));
    response.setData(resMap);
    return response;
  }

  /**
   * @param originId problem的唯一标示
   * @return com.vcode.entity.Response
   * @Description 获取problems的详情
   * @Date 2020/2/11 15:34
   */
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
}
