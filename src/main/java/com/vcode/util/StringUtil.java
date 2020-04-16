package com.vcode.util;

import java.util.Random;

/**
 * @author moyee
 */
public class StringUtil {

  /**
   * @return java.lang.String
   * @Description generate random string
   * @Date 2020/2/24 12:36
   */
  public static String generateRandomStr() {
    String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    Random random = new Random(System.currentTimeMillis());
    int stringLength = 8;
    StringBuilder str = new StringBuilder();
    for (int i = 0; i < stringLength; i++) {
      int number = random.nextInt(base.length());
      str.append(base.charAt(number));
    }
    str.append(System.currentTimeMillis());
    return str.toString();
  }
}
