package com.vcode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */
@Configuration
@EnableAsync
public class ExecutorConfig {
  @Bean
  public Executor getSpiderProblemExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("vcode-parse-spider-problem");
    executor.setMaxPoolSize(20);
    executor.setCorePoolSize(10);
    executor.setQueueCapacity(0);
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    return executor;
  }

  @Bean
  public Executor getJudgeResultExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("vcode-judge-result");
    executor.setMaxPoolSize(20);
    executor.setCorePoolSize(10);
    executor.setQueueCapacity(0);
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    return executor;
  }

  @Bean
  public Executor getSpiderProblemTaskResultExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("vcode-spider-task-result");
    executor.setMaxPoolSize(20);
    executor.setCorePoolSize(10);
    executor.setQueueCapacity(0);
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    return executor;
  }
}
