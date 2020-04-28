package com.vcode.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author moyee
 * @version 1.0.0
 * @Description vcode judge
 * @Date
 */
@Component
@ConfigurationProperties(prefix = "judge")
public class JudgeConfig {
  private String host;
  private String port;

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getJudgeUrl() {
    String httpStartString = "http://";
    if (this.host.startsWith(httpStartString)) {
      return this.getHost() + ":" + this.getPort();
    } else {
      return httpStartString + this.getHost() + ":" + this.getPort();
    }
  }

  public String caseReadyApi() {
    return this.getJudgeUrl() + "/api/case/ready";
  }

  public String caseCheckApi() {
    return this.getJudgeUrl() + "/api/case/check";
  }
}
