package com.vcode.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // 跨域配置
    registry.addMapping("/**")
            .allowedHeaders("*")
            .allowedMethods("*")
            .allowedOrigins("*")
            .allowCredentials(true) // 是否允许发送cookie
            .exposedHeaders(HttpHeaders.SET_COOKIE);;
  }
}
