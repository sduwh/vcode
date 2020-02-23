package com.vcode.util;

import java.io.File;

public class TestCaseHandler {
  public static boolean isZipExist(String filePath) {
    File file = new File(filePath);
    return file.exists();
  }
}
