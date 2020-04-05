package com.vcode.controller;

import com.vcode.Impl.RankDaoImpl;
import com.vcode.entity.Rank;
import com.vcode.entity.Response;
import com.vcode.util.RankDataUtil;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.tools.java.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */

@RestController
@RequestMapping("/rank")
public class RankController {

  private RankDaoImpl rankDao;

  @Autowired
  public RankController(RankDaoImpl rankDao) {
    this.rankDao = rankDao;
  }

  @GetMapping()
  public Response getGlobalRank() {
    Response response = new Response();
    List<HashMap> rankList = rankDao.getGlobalRankData();
    Map<String, Object> data = new HashMap<>();
    data.put("rankList", rankList);
    response.setData(data);
    return response;
  }

  @GetMapping("/contest")
  public Response getContestRank(@RequestParam(value = "contestName") String contestName) {
    Response response = new Response();
    List<Rank> rankList = rankDao.getContestRankData(contestName);
    Map<String, Object> data = new HashMap<>();
    data.put("rankList", RankDataUtil.SortRankData(rankList));
    response.setData(data);
    return response;
  }
}
