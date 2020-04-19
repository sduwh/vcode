package com.vcode.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcode.entity.CrawlProblemTask;

import java.util.List;

/**
 * @author moyee
 * @version 1.0.0
 * @Description 操作problem爬虫任务的实体类的接口
 * @Date
 */
public interface CrawlProblemTaskDao {

  /**
   * 创建一个新的任务
   *
   * @param task task实例
   * @return 保存至mongo中的task数据
   * @throws JsonProcessingException json处理错误
   */
  CrawlProblemTask createTask(CrawlProblemTask task) throws JsonProcessingException;

  /**
   * 通过id的hex值删除任务
   *
   * @param idHex objectId hex
   */
  void deleteTask(String idHex);


  /**
   * 根据ObjectId的hex值查找task
   *
   * @param taskId objectId hex
   * @return task 实例
   */
  CrawlProblemTask findTaskByOriginTaskId(String taskId);

  /**
   * task 分页查询
   *
   * @param page   页码
   * @param size   页的大小
   * @param search 查询条件
   * @return 查询结果
   */
  List<CrawlProblemTask> findTaskList(int page, int size, String search);

  /**
   * 统计符合查询条件的task数量
   *
   * @param search 查询条件
   * @return 统计结果
   */
  long count(String search);

  /**
   * 根据task_id更新task结果
   *
   * @param crawlProblemTask 最新的task实例
   */
  void updateResult(CrawlProblemTask crawlProblemTask);
}
