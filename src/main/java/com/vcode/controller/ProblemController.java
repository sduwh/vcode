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

  @Autowired
  private TestCaseConfig testCaseConfig;

  private Logger log = Logger.getLogger("ProblemController");


  /**
   * @param page   页码
   * @param size   一页的容量
   * @param search 查询条件
   * @return com.vcode.entity.Response
   * @Description 获取problems列表
   * @Date 2020/2/11 15:33
   */
  @GetMapping("/list")
  public Response getProblemList(@RequestParam(value = "page") int page,
                                 @RequestParam(value = "size") int size,
                                 @RequestParam(value = "search") String search) {
    Response response = new Response();
    if (page < 1 || size < 1) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("page or size must be greater than zero");
      return response;
    }
    // 数据库中的分页从0开始
    page--;
    List<Problem> problemList = problemDao.findProblems(page, size, search);
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

  /**
   * @param problem problem的字段
   * @return com.vcode.entity.Response
   * @Description 编辑problem的信息
   * @Date 2020/2/11 15:35
   */
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

  /**
   * @param problem problem的字段
   * @return com.vcode.entity.Response
   * @Description 创建problem的信息
   * @Date 2020/2/11 15:36
   */
  @PostMapping("/create")
  public Response createProblem(@RequestBody @Valid Problem problem) {
    Response res = new Response();
    // 添加origin前缀
    problem.setOriginId(problem.getOriginId());

    // check is this problem exist
    if (problemDao.isExist(problem)) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("ID or Title is repeat");
      return res;
    }
    // check testCaseId
    String fileFullPath = "/tmp/" + problem.getTestCaseId() + ".zip";
    String targetDir = problem.getTestCaseId();
    if (!TestCaseHandler.isZipExist(fileFullPath)) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("Test Case is need to upload");
      return res;
    }
    // process testCaseFile: mv the file to target dir
    TestCaseHandler.moveTestCaseFiles(testCaseConfig.getPath(), targetDir);
    problemDao.saveProblem(problem);
    log.info(String.format("create problem: origin_id: %s, title: %s", problem.getOrigin(), problem.getTitle()));
    res.setData(problem);
    return res;
  }

  /**
   * @param map 用于获取 originId
   * @return com.vcode.entity.Response
   * @Description 根据OriginId删除problem
   * @Date 2020/2/11 15:36
   */
  @DeleteMapping("/delete")
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
