package com.vcode.dao;

import com.vcode.entity.Rank;
import com.vcode.entity.Submission;

import java.util.HashMap;
import java.util.List;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */
public interface RankDao {
  /**
   * 查询contest对应的rank数据
   *
   * @param contestName contest's name
   * @return contest对应的rank数据
   */
  List<Rank> getContestRankData(String contestName);

  /**
   * 获取全聚德rank数据
   *
   * @return 全局的rank数据
   */
  List<HashMap> getGlobalRankData();

  /**
   * 保存rank数据
   *
   * @param rank rank实例
   * @throws InterruptedException 中断错误
   */
  void saveRank(Rank rank) throws InterruptedException;

  /**
   * 查询用户对应的rank数据
   *
   * @param userAccount     用户账号
   * @param contestName     contest's name， "" 表示全局
   * @param problemOriginId problem's originId
   * @return 用户对应的rank数据
   */
  Rank find(String userAccount, String contestName, String problemOriginId);

  /**
   * 更新rank数据
   *
   * @param submission submission实例
   * @throws InterruptedException 中断错误
   */
  void updateRank(Submission submission) throws InterruptedException;
}
