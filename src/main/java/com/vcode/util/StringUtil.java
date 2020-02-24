package com.vcode.util;

import java.util.Random;

public class StringUtil {

  /**
   * @Description generate random string
   * @Date 2020/2/24 12:36
   * @return java.lang.String
   */
  public static String generateRandomStr() {
    String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    Random random = new Random(System.currentTimeMillis());
    StringBuilder str = new StringBuilder();
    for (int i = 0; i < 8; i++) {
      int number = random.nextInt(base.length());
      str.append(base.charAt(number));
    }
    str.append(System.currentTimeMillis());
    return str.toString();
  }
}
