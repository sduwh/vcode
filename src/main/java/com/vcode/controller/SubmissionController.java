package com.vcode.controller;

import com.vcode.Impl.SubmissionDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Response;
import com.vcode.entity.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/submission")
public class SubmissionController {

  @Autowired
  private SubmissionDaoImpl submissionDao;

  private Logger log = Logger.getLogger("SubmissionController");

  @GetMapping("/detail")
  public Response getSubmissionDetail(@RequestParam(value = "originId") String problemOriginId) {
    /**
     * @Description 获取Submission的详情
     * @param originId problem的唯一标示
     * @return com.vcode.entity.Response
     */
    Response res = new Response();
    if (problemOriginId == null) {
      res.setCode(ResponseCode.ERROR);
      res.setMessage("originId 不能为空");
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

  @PostMapping("/edit")
  public Response editSubmission(@RequestBody @Valid Submission submission) {
    /**
     * @Description 修改Submission详情
     * @param Submission 的submission字段
     * @return com.vcode.entity.Response
     */
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

  @PostMapping("/create")
  public Response createSubmission(@RequestBody @Valid Submission submission) {
    /**
     * @Description 创建一个新的Submission
     * @param Submission 的submission字段
     * @return com.vcode.entity.Response
     */
    Response res = new Response();
    if (submissionDao.isExist(submission)) {
      res.setCode(ResponseCode.FAIL);
      res.setMessage("此Submission已存在");
      return res;
    }
    submissionDao.saveSubmission(submission);
    res.setData(submission);
    return res;
  }

  @DeleteMapping("/delete")
  public Response deleteSubmissionByOriginId(@RequestBody Map<String, String> map) {
    /**
     * @Description 删除一个Submission
     * @param map 用于获取problemOriginId
     * @return com.vcode.entity.Response
     */
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
}
