package com.vcode.common;

/**
 * @author moyee
 * @version 1.0.0
 * @Description redis common key
 * @Date
 */
public class RedisCode {

  /**
   * rank表的读写锁
   */
  public static String RANK_LOCK = "rank-lock";

  /**
   * vcode-spider 爬取的problem消息队列
   */
  public static String PROBLEM_TOPIC = "vcode-spider-problem";

  /**
   * 爬取指定题目的任务队列
   */
  public static String TARGET_CRAWL_TOPIC = "vcode-spider-target";

  /**
   * 爬取指定题目的任务结果队列，从此队列获取任务结果
   */
  public static String TARGET_CRAWL_RESULT_TOPIC = "vcode-spider-target-result";

  /**
   * vcode-spider支持的远程oj
   */
  public static String OJ_LIST = "vcode-spider-namespace";

  /**
   * 本地判题task队列
   */
  public static String JUDGE_TASK_TOPIC = "vcode-judge-task";

  /**
   * 远程判题task队列
   */
  public static String JUDGE_REMOTE_TASK_TOPIC = "vcode-judge-remote-task";

  /**
   * 判题结果队列
   */
  public static String JUDGE_RESULT_TOPIC = "vcode-judge-status";
}
