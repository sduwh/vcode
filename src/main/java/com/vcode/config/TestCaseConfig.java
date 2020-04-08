package com.vcode.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "test-case")
@PropertySource("classpath:config.properties")
public class TestCaseConfig {
  String path;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
