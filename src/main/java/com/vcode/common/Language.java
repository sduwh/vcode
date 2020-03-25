package com.vcode.common;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */
public class Language {
  public static Set<String> languages = new HashSet<>(Arrays.asList("c", "c++", "java", "python3", "go"));
  public static boolean checkLanguage(String language) {
    return languages.contains(language);
  }
}
