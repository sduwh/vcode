package com.vcode.controller;

import com.vcode.Impl.RankDaoImpl;
import com.vcode.entity.Rank;
import com.vcode.entity.Response;
import com.vcode.util.RankDataUtil;
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

  private RankDaoImpl rankDao;

  @Autowired
  public RankController(RankDaoImpl rankDao) {
    this.rankDao = rankDao;
  }

  @GetMapping()
  public Response getGlobalRank() {
    return null;
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

  @PostMapping("/insert-one-data")
  public Response insertOneData() {
    Response response = new Response();
    Rank rank = new Rank("201600800528", "vscode", "test1", 123123, "vcode-test1");
    rankDao.save(rank);
    return response;
  }

  @PostMapping("/test")
  public Response insertData() {
    Response response = new Response();
    Rank rank = new Rank("201600800528", "vscode", "test1111", 123123, "vcode-test1");
    rank.setAcNum(6);
    Rank rank1 = new Rank("201600800590", "vscode", "test1111", 123123, "vcode-test1");
    rank.setAcNum(7);
    try {
      rankDao.saveRank(rank);
      rankDao.saveRank(rank1);
    }catch (InterruptedException e) {
      System.out.println(e.getMessage());
    }
    return response;
  }
}
