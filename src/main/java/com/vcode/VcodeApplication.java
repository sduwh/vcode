package com.vcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author moyee
 */
@EnableScheduling
@SpringBootApplication
public class VcodeApplication {

  public static void main(String[] args) {
    SpringApplication.run(VcodeApplication.class, args);
  }

}
