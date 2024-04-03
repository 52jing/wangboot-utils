package com.wangboot.core.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

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

  public static String joinStrList(List<String> data, String del) {
    if (Objects.isNull(data)) {
      return "";
    }
    return String.join(del, data);
  }

  public static List<String> splitStrList(String value, String del) {
    if (Objects.isNull(value) || value.length() == 0) {
      return Collections.emptyList();
    }
    return Arrays.asList(value.split(del));
  }

  public static <T> String joinList(List<T> data, String del) {
    if (Objects.isNull(data)) {
      return "";
    }
    return data.stream().map(String::valueOf).collect(Collectors.joining(del));
  }

  public static List<Long> splitLongList(String value, String del) {
    if (Objects.isNull(value) || value.length() == 0) {
      return Collections.emptyList();
    }
    return Arrays.stream(value.split(del)).map(Long::valueOf).collect(Collectors.toList());
  }

  public static List<Integer> splitIntList(String value, String del) {
    if (Objects.isNull(value) || value.length() == 0) {
      return Collections.emptyList();
    }
    return Arrays.stream(value.split(del)).map(Integer::valueOf).collect(Collectors.toList());
  }

  public static List<Float> splitFloatList(String value, String del) {
    if (Objects.isNull(value) || value.length() == 0) {
      return Collections.emptyList();
    }
    return Arrays.stream(value.split(del)).map(Float::valueOf).collect(Collectors.toList());
  }

  public static List<Boolean> splitBooleanList(String value, String del) {
    if (Objects.isNull(value) || value.length() == 0) {
      return Collections.emptyList();
    }
    return Arrays.stream(value.split(del)).map(Boolean::valueOf).collect(Collectors.toList());
  }

  public static Integer getInteger(String value, Integer defaultVal) {
    if (Objects.isNull(value)) {
      return defaultVal;
    }
    return Convert.toInt(value, defaultVal);
  }

  public static Integer getInteger(String value) {
    return getInteger(value, null);
  }

  public static int getIntPrimitive(String value, int defaultVal) {
    Integer v = getInteger(value);
    return Objects.isNull(v) ? defaultVal : v;
  }

  public static Long getLong(String value, Long defaultVal) {
    if (Objects.isNull(value)) {
      return null;
    }
    return Convert.toLong(value, defaultVal);
  }

  public static Long getLong(String value) {
    return getLong(value, null);
  }

  public static long getLongPrimitive(String value, long defaultVal) {
    Long v = getLong(value);
    return Objects.isNull(v) ? defaultVal : v;
  }

  public static Float getFloat(String value, Float defaultVal) {
    if (Objects.isNull(value)) {
      return null;
    }
    return Convert.toFloat(value, defaultVal);
  }

  public static Float getFloat(String value) {
    return getFloat(value, null);
  }

  public static float getFloatPrimitive(String value, float defaultVal) {
    Float v = getFloat(value);
    return Objects.isNull(v) ? defaultVal : v;
  }

  public static Boolean getBoolean(String value, Boolean defaultVal) {
    if (Objects.isNull(value)) {
      return null;
    }
    return Convert.toBool(value, defaultVal);
  }

  public static Boolean getBoolean(String value) {
    return getBoolean(value, null);
  }

  public static boolean getBooleanPrimitive(String value, boolean defaultVal) {
    Boolean v = getBoolean(value);
    return Objects.isNull(v) ? defaultVal : v;
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
