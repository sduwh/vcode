package com.vcode.dao;

import com.vcode.entity.Rank;

import java.util.List;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */
public interface RankDao {
  List<Rank> getContestRankData(String contestName);

  List<Rank> getGlobalRankData();

  void saveRank(Rank rank);
}
