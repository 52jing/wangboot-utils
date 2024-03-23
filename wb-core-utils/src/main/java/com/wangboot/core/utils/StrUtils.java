package com.wangboot.core.utils;

import cn.hutool.core.text.CharSequenceUtil;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 字符串工具类
 *
 * @author wwtg99
 */
public class StrUtils {

  /** 下划线 */
  private static final char SEPARATOR = '_';

  private StrUtils() {}

  /** 驼峰转下划线命名 */
  public static String toUnderScoreCase(String str) {
    if (Objects.isNull(str) || str.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    // 前置字符是否大写
    boolean preCharIsUpperCase;
    // 当前字符是否大写
    boolean curreCharIsUpperCase;
    // 下一字符是否大写
    boolean nexteCharIsUpperCase = true;
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (i > 0) {
        preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
      } else {
        preCharIsUpperCase = false;
      }

      curreCharIsUpperCase = Character.isUpperCase(c);

      if (i < (str.length() - 1)) {
        nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
      }

      if ((preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase)
          || ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase)) {
        sb.append(SEPARATOR);
      }
      sb.append(Character.toLowerCase(c));
    }

    return sb.toString();
  }

  /**
   * 下划线转驼峰命名
   *
   * @param str 下划线命名
   * @param capitalize 是否首字母大写
   */
  public static String toCamelCase(String str, boolean capitalize) {
    if (Objects.isNull(str) || str.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    String[] ss = str.split("_");
    boolean first = true;
    for (String s : ss) {
      String lower = s.toLowerCase();
      if (first) {
        if (capitalize) {
          sb.append(lower.substring(0, 1).toUpperCase()).append(lower.substring(1));
        } else {
          sb.append(lower);
        }
        first = false;
      } else {
        sb.append(lower.substring(0, 1).toUpperCase()).append(lower.substring(1));
      }
    }
    return sb.toString();
  }

  /**
   * 将字符串切分成多段，每段不超过定长字符数
   *
   * @param str 字符串
   * @param lineWidth 每行最大长度
   * @return 字符串列表
   */
  public static List<String> splitStrByLineWidth(String str, int lineWidth) {
    List<String> res = new ArrayList<>();
    int i = 0;
    while (i < str.length()) {
      int j = i + lineWidth;
      if (j > str.length()) {
        j = str.length();
      }
      res.add(str.substring(i, j));
      i = j;
    }
    return res;
  }

  /**
   * 获取时间区间
   *
   * @param period 区间字符串，以 s（秒）、m（分）、h（小时）结尾，例如 10s, 5m, 1h，没有单位则默认秒
   * @return Duration
   */
  public static Duration getDuration(String period) {
    char[] numbers = "1234567890".toCharArray();
    if (period.toLowerCase().endsWith("h")) {
      String n = period.substring(0, period.length() - 1);
      if (CharSequenceUtil.containsOnly(n, numbers)) {
        return Duration.ofHours(Long.parseLong(n));
      } else {
        throw new IllegalArgumentException("Invalid period string " + period);
      }
    } else if (period.toLowerCase().endsWith("m")) {
      String n = period.substring(0, period.length() - 1);
      if (CharSequenceUtil.containsOnly(n, numbers)) {
        return Duration.ofMinutes(Long.parseLong(n));
      } else {
        throw new IllegalArgumentException("Invalid period string " + period);
      }
    } else if (period.toLowerCase().endsWith("s")) {
      String n = period.substring(0, period.length() - 1);
      if (CharSequenceUtil.containsOnly(n, numbers)) {
        return Duration.ofSeconds(Long.parseLong(n));
      } else {
        throw new IllegalArgumentException("Invalid period string " + period);
      }
    } else {
      if (CharSequenceUtil.containsOnly(period, numbers)) {
        return Duration.ofSeconds(Long.parseLong(period));
      } else {
        throw new IllegalArgumentException("Invalid period string " + period);
      }
    }
  }
}
