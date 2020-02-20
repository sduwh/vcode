package com.vcode.controller;

import com.vcode.Impl.ContestDaoImpl;
import com.vcode.Impl.ProblemDaoImpl;
import com.vcode.common.ResponseCode;
import com.vcode.entity.Contest;
import com.vcode.entity.Problem;
import com.vcode.entity.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/contest")
public class ContestController {

  @Autowired
  private ContestDaoImpl contestDao;

  @Autowired
  private ProblemDaoImpl problemDao;

  private Logger log = Logger.getLogger("ContestController");

  @GetMapping("/list")
  public Response getContestsList(@RequestParam(value = "page") int page,
                                  @RequestParam(value = "size") int size,
                                  @RequestParam(value = "search") String search) {

    /**
     * @Description 获取contest列表
     * @Date 2020/2/11 15:33
     * @param page 页码
     * @param size 一页的容量
     * @param search 查询条件
     * @return com.vcode.entity.Response
     */
    Response response = new Response();
    if (page < 1 || size < 1) {
      response.setCode(ResponseCode.ERROR);
      response.setMessage("page or size must be greater than zero");
      return response;
    }
    page--;
    HashMap<String, Object> resMap = new HashMap<>();
    List<Contest> contestList = contestDao.findContests(page, size, search);
    resMap.put("contestList", contestList);
    resMap.put("total", contestDao.count(search));
    response.setData(resMap);
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
    if (contestDao.isExist(contest)) {
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
    if (contestDao.isExist(contest)) {
      contestDao.updateContest(contest);
      return response;
    }
    response.setCode(ResponseCode.FAIL);
    response.setMessage("contest不存在");
    return response;
  }

  @PostMapping("/delete")
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

  @GetMapping("/problems")
  public Response problems(@RequestParam(value = "contestTitle") String name) {
    Response response = new Response();

    Contest contest = contestDao.findByName(name);
    if (contest == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("此Contest不存在");
      return response;
    }

    LinkedList<ObjectId> problemIds = contest.getProblems();
    List<Problem> problems = problemDao.getAllProblems(problemIds);
    HashMap<String, Object> map = new HashMap<>();
    map.put("problems", problems);
    response.setData(map);

    return response;
  }

  @PostMapping("/problem")
  public Response addProblem(@RequestBody Map<String, String> map) {
    Response response = new Response();
    // get params
    String problemOriginId = map.get("problemOriginId");
    String contestTitle = map.get("contestTitle");
    if (contestTitle == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("contestTitle is required");
      return response;
    }
    if (problemOriginId == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("problemOriginId is required");
      return response;
    }

    // find Object
    Contest contest = contestDao.findByName(contestTitle);
    if (contest == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("contest不存在");
      return response;
    }

    Problem problem = problemDao.findByOriginId(problemOriginId);
    if (problem == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("problem不存在");
      return response;
    }

    contestDao.addProblem(contest, problem);
    return response;
  }

  @DeleteMapping("/problem")
  public Response RemoveProblem(@RequestParam(value = "problemOriginId") String problemOriginId,
                                @RequestParam(value = "contestTitle") String contestTitle) {

    Response response = new Response();
    // find Object
    Contest contest = contestDao.findByName(contestTitle);
    if (contest == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("contest不存在");
      return response;
    }

    Problem problem = problemDao.findByOriginId(problemOriginId);
    if (problem == null) {
      response.setCode(ResponseCode.FAIL);
      response.setMessage("problem不存在");
      return response;
    }

    contestDao.removeProblem(contest, problem);
    return response;
  }
}
