package com.vcode.util;

import java.util.Random;

public class StringHandler {
  public static String generateRandomStr(int length) {
    String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    Random random = new Random();
    StringBuilder str = new StringBuilder();
    for (int i = 0; i < length; i++) {
      int number = random.nextInt(base.length());
      str.append(base.charAt(number));
    }
    return str.toString();
  }
}
