package com.vcode.controller;

import com.vcode.impl.RankDaoImpl;
import com.vcode.entity.Rank;
import com.vcode.entity.Response;
import com.vcode.handler.RankDataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final RankDaoImpl rankDao;

  @Autowired
  public RankController(RankDaoImpl rankDao) {
    this.rankDao = rankDao;
  }

  /**
   * @return com.vcode.entity.Response
   * @Description get global rank data
   * @Date 2020/4/11 11:11
   */
  @GetMapping()
  public Response getGlobalRank() {
    Response response = new Response();
    List<HashMap> rankList = rankDao.getGlobalRankData();
    logger.debug("The rank data of global has been accessed");
    Map<String, Object> data = new HashMap<>(1);
    data.put("rankList", rankList);
    response.setData(data);
    return response;
  }

  /**
   * @param contestName contest's name
   * @return com.vcode.entity.Response
   * @Description get rank data for contest
   * @Date 2020/4/11 11:12
   */
  @GetMapping("/contest")
  public Response getContestRank(@RequestParam(value = "contestName") String contestName) {
    Response response = new Response();
    List<Rank> rankList = rankDao.getContestRankData(contestName);
    logger.debug(String.format("The rank data of contest: %s has been accessed", contestName));
    Map<String, Object> data = new HashMap<>(1);
    data.put("rankList", RankDataHandler.sortRankData(rankList));
    response.setData(data);
    return response;
  }
}
