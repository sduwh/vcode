package com.vcode.controller;

import com.vcode.Impl.ProblemDaoImpl;
import com.vcode.Impl.SubmissionDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Problem;
import com.vcode.entity.Response;
import com.vcode.entity.Submission;
import com.vcode.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/submission")
public class SubmissionController {

  @Autowired
  private SubmissionDaoImpl submissionDao;

  @Autowired
  private ProblemDaoImpl problemDao;

  private Logger log = Logger.getLogger("SubmissionController");

  /**
   * @Description get submission list
   * @Date 2020/3/19 14:30
   * @param page page number
   * @param size page size
   * @param search search key words
   * @return com.vcode.entity.Response
   */
  @GetMapping("/list")
  public Response getSubmissions(@RequestParam(value = "page") int page,
                                 @RequestParam(value = "size") int size,
                                 @RequestParam(value = "search") String search){

    Response response = new Response();
    if (page < 1 || size < 1) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("page or size must be greater than zero");
      return response;
    }
    page--;
    try {
      List<Submission> submissionList = submissionDao.findSubmissions(page, size, search);
      HashMap<String, Object> resMap = new HashMap<>();
      resMap.put("submissionList", submissionList);
      resMap.put("total", submissionDao.count(search));
      response.setData(resMap);
      return response;
    }catch (Exception e){
      response.setCode(ResponseCode.ERROR);
      response.setMessage("find error");
      response.setError(e.getMessage());
      return response;
    }
  }

  /**
   * @Description 获取Submission的详情
   * @param problemOriginId problem的唯一标示
   * @return com.vcode.entity.Response
   */
  @GetMapping("/detail")
  public Response getSubmissionDetail(@RequestParam(value = "originId") String problemOriginId) {
    Response res = new Response();
    if (problemOriginId == null) {
      res.setCode(ResponseCode.ERROR);
      res.setMessage("originId is required");
      return res;
    }
    Submission submission = submissionDao.findById(problemOriginId);
    if (submission == null) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("Submission is not exit");
      return res;
    }
    res.setData(submission);
    return res;
  }

  /**
   * @Description 修改Submission详情
   * @param submission 的submission字段
   * @return com.vcode.entity.Response
   */
  @PostMapping("/edit")
  public Response editSubmission(@RequestBody @Valid Submission submission) {
    Response res = new Response();
    if (submissionDao.isExist(submission)) {
      submissionDao.updateSubmission(submission);
      res.setMessage("修改成功");
      return res;
    }
    res.setCode(ResponseCode.FAIL);
    res.setMessage("此Submission不存在");
    return res;
  }

  /**
   * @Description 创建一个新的Submission
   * @param submission 的submission字段
   * @return com.vcode.entity.Response
   */
  @PostMapping()
  @RequiresAuthentication
  public Response uploadSubmission(@RequestBody @Valid Submission submission) {
    Response res = new Response();
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JWTUtil.getAccount(token);

    submission.setUserAccount(account);
    if (submissionDao.isExist(submission)) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("the submission is repeat");
      return res;
    }
    submission = submissionDao.fillInfo(submission);
    if (submission.getProblemTitle() == null) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("problem is not exist");
      return res;
    }
    submission.setResult("success");
    submissionDao.saveSubmission(submission);
    problemDao.incSubmissionNum(submission.getProblemOriginId());
    return res;
  }

  /**
   * @Description 删除一个Submission
   * @param map 用于获取problemOriginId
   * @return com.vcode.entity.Response
   */
  @DeleteMapping("/delete")
  public Response deleteSubmissionByOriginId(@RequestBody Map<String, String> map) {
    Response res = new Response();
    String id = map.get("problemOriginId");
    if (id == null) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("originId is required");
      return res;
    }
    submissionDao.deleteProblemByOriginId(id);
    return res;
  }

  @GetMapping("/problem/user")
  @RequiresAuthentication
  public Response getUserSubmissionForProblem(@RequestParam(value = "originId") String problemOriginId){
    Response res = new Response();
    Subject subject = SecurityUtils.getSubject();
    String token = (String) subject.getPrincipal();
    String account = JWTUtil.getAccount(token);

    List<Submission> submissionList = submissionDao.findSubmissions(problemOriginId, account);
    Map<String, Object> dataMap = new HashMap<>();
    dataMap.put("submissionList", submissionList);
    res.setData(dataMap);
    return res;
  }
}
