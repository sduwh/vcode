package com.vcode.controller;

import com.vcode.Impl.ContestDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Contest;
import com.vcode.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/contest")
public class ContestController {

  @Autowired
  private ContestDaoImpl contestDao;

  private Logger log = Logger.getLogger("ContestController");

  @GetMapping("/list")
  public Response getContestsList(@RequestParam(value = "page") int page,
                                  @RequestParam(value = "size") int size) {

    /**
     * @Description 获取contest列表
     * @Date 2020/2/11 15:33
     * @param page 页码
     * @param size 一页的容量
     * @return com.vcode.entity.Response
     */
    Response response = new Response();
    if (page < 1 || size < 1) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("page or size must be greater than zero");
      return response;
    }
    page--;
    List<Contest> problemList = contestDao.findContestsByPageAndSize(page, size);
    response.setData(problemList);
    return response;
  }

  @GetMapping("/detail")
  public Response getContestDetail(@RequestParam(value = "name") @Size(min = 1) String contestName) {
    /**
     * @Description 获取Contest的详细信息
     * @Date 2020/2/11 16:13
     * @param contestName 1
     * @return com.vcode.entity.Response
     */
    Response response = new Response();
    if (contestName == null || contestName.length() == 0) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("name is required");
      return response;
    }
    Contest contest = contestDao.findByName(contestName);
    if (contest == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("此Contest不存在");
      return response;
    }
    response.setData(contest);
    return response;
  }

  @PostMapping("/create")
  public Response createContest(@RequestBody @Valid Contest contest) {
    /**
     * @Description 创建一个新的contest
     * @Date 2020/2/11 16:24
     * @param contest contest的相关信息
     * @return com.vcode.entity.Response
     */
    Response response = new Response();
    if (contestDao.isExist(contest.getName())) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("contest已存在");
      return response;
    }
    contestDao.saveContest(contest);
    return response;
  }

  @PostMapping("/edit")
  public Response editContest(@RequestBody @Valid Contest contest) {
    /**
     * @Description 编辑contest的信息
     * @Date 2020/2/11 16:24
     * @param contest contest的相关信息
     * @return com.vcode.entity.Response
     */
    Response response = new Response();
    if (contestDao.isExist(contest.getName())) {
      contestDao.updateContest(contest);
      return response;
    }
    response.setCode(ResponseCode.FAIL);
    response.setMessage("contest不存在");
    return response;
  }

  @DeleteMapping("/delete")
  public Response delContest(@RequestBody Map<String, String> map) {
    /**
     * @Description 删除contest
     * @Date 2020/2/11 16:25
     * @param map 用于获取contestName
     * @return com.vcode.entity.Response
     */
    Response response = new Response();
    String contestName = map.get("name");
    if (contestName == null || contestName.length() == 0) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("name is required");
      return response;
    }
    contestDao.deleteContestByName(contestName);
    return response;
  }
}
