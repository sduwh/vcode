package com.vcode.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcode.entity.Submission;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * @author moyee
 */
public interface SubmissionDao {

  /**
   * 保存submission
   *
   * @param submission submission实例
   * @return 保存的submission
   */
  Submission saveSubmission(Submission submission);

  /**
   * 通过id查询submission
   *
   * @param objectId id
   * @return 查询结果，不存在返回null
   */
  Submission findById(ObjectId objectId);

  /**
   * 通过id的hex查询submission
   *
   * @param hex ObjectId的hex
   * @return 查询结果，不存在返回null
   */
  Submission findByIdHex(String hex);

  /**
   * 要更新的submission实例
   *
   * @param submission submission
   * @throws InterruptedException 中断错误
   */
  void updateSubmission(Submission submission) throws InterruptedException;

  /**
   * 通过id的hex删除submission
   *
   * @param submissionHexId ObjectId的hex
   */
  void deleteSubmissionByHexId(String submissionHexId);

  /**
   * 判断submission是否已经存在
   *
   * @param submission submission
   * @return bool, true存在，false不存在
   */
  boolean isExist(Submission submission);

  /**
   * 根据查询条件查询submission
   *
   * @param page   页码
   * @param size   一页的大小
   * @param search 查询条件
   * @return 查询结果
   */
  List<Submission> findSubmissions(int page, int size, String search);

  /**
   * 根据查询条件查询submission
   *
   * @param originId 题目originId
   * @param account  用户account
   * @return 查询结果
   */
  List<Submission> findSubmissions(String originId, String account);

  /**
   * 查询problem下的所有提交
   *
   * @param page     页码
   * @param size     一页的大小
   * @param originId 题目originId
   * @return 查询结果
   */
  List<Submission> findProblemSubmissions(int page, int size, String originId);

  /**
   * 查询Contest下的所有提交
   *
   * @param contestName contest's name
   * @param page        页码
   * @param size        一页的大小
   * @param search      查询条件
   * @return 查询结果
   */
  List<Submission> findContestSubmission(String contestName, int page, int size, String search);
  /**
   * 查询group下的所有提交
   *
   * @param groupName group'name
   * @param page        页码
   * @param size        一页的大小
   * @param search      查询条件
   * @return 查询结果
   */
  List<Submission> findGroupSubmission(String groupName, int page, int size, String search);
  /**
   * 统计查询条件下的提交数量
   *
   * @param search 查询条件
   * @return 统计结果
   */
  long count(String search);

  /**
   * 统计一个problem下的提交数量
   *
   * @param problemOriginId problem's origin id
   * @return 统计结果
   */
  long countSubmission(String problemOriginId);

  /**
   * 获取一个contest对应的提交数量
   *
   * @param contestName contest's name
   * @param search      查询条件
   * @return 统计结果
   */
  long countContestSubmission(String contestName, String search);
  /**
   * 获取一个Group对应的提交数量
   *
   * @param groupName Group'name
   * @param search      查询条件
   * @return 统计结果
   */
  long countGroupSubmission(String groupName,String search);
  /**
   * 给submission实例填充信息
   *
   * @param submission submission实例
   * @return 更新信息后的submission
   */
  Submission fillInfo(Submission submission);

  /**
   * 查询用户在同一题目下的最新提交
   *
   * @param account         用户账号
   * @param problemOriginId problemOriginId
   * @return 查询结果
   */
  Submission getUserLastSubmissionBySameProblem(String account, String problemOriginId);

  /**
   * 将submission发送去judge进行判题
   *
   * @param submission submission实例
   * @throws JsonProcessingException json处理数据出错
   */
  void sendToJudgeQueue(Submission submission) throws JsonProcessingException;

  /**
   * 返回判题等待时间超过一分钟的Submission
   *
   * @return 返回SubmissionList
   */
  List<Submission> findJudgingSubmissionList();
}
