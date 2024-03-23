package com.wangboot.core.web.param;

import cn.hutool.core.convert.Convert;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 参数工具类
 *
 * @author wwtg99
 */
public class ParamUtils {

  /** 参数列表分隔符 */
  public static final String PARAM_LIST_DELIMITER = ",";

  private ParamUtils() {}

  @NonNull
  public static String convertStrListToParamValue(@NonNull List<String> data) {
    return String.join(PARAM_LIST_DELIMITER, data);
  }

  @NonNull
  public static List<String> getStrList(@NonNull String value) {
    return Arrays.asList(value.split(PARAM_LIST_DELIMITER));
  }

  @NonNull
  public static String convertIntListToParamValue(@NonNull List<Long> data) {
    return data.stream().map(String::valueOf).collect(Collectors.joining(PARAM_LIST_DELIMITER));
  }

  @NonNull
  public static List<Long> getIntList(@NonNull String value) {
    return Arrays.stream(value.split(PARAM_LIST_DELIMITER))
        .map(Long::valueOf)
        .collect(Collectors.toList());
  }

  @NonNull
  public static String convertFloatListToParamValue(@NonNull List<Float> data) {
    return data.stream().map(String::valueOf).collect(Collectors.joining(PARAM_LIST_DELIMITER));
  }

  @NonNull
  public static List<Float> getFloatList(@NonNull String value) {
    return Arrays.stream(value.split(PARAM_LIST_DELIMITER))
        .map(Float::valueOf)
        .collect(Collectors.toList());
  }

  @NonNull
  public static String convertBoolListToParamValue(@NonNull List<Boolean> data) {
    return data.stream().map(String::valueOf).collect(Collectors.joining(PARAM_LIST_DELIMITER));
  }

  @NonNull
  public static List<Boolean> getBoolList(@NonNull String value) {
    return Arrays.stream(value.split(PARAM_LIST_DELIMITER))
        .map(Boolean::valueOf)
        .collect(Collectors.toList());
  }

  @Nullable
  public static Integer getInt(@Nullable String value, @Nullable Integer defaultVal) {
    if (Objects.isNull(value)) {
      return null;
    }
    return Convert.toInt(value, defaultVal);
  }

  @Nullable
  public static Integer getInt(@Nullable String value) {
    return Convert.toInt(value, null);
  }

  public static int getIntPrimitive(@Nullable String value, int defaultVal) {
    Integer v = getInt(value);
    return Objects.isNull(v) ? defaultVal : v;
  }

  @Nullable
  public static Long getLong(@Nullable String value, @Nullable Long defaultVal) {
    if (Objects.isNull(value)) {
      return null;
    }
    return Convert.toLong(value, defaultVal);
  }

  @Nullable
  public static Long getLong(@Nullable String value) {
    return Convert.toLong(value, null);
  }

  public static long getLongPrimitive(@Nullable String value, long defaultVal) {
    Long v = getLong(value);
    return Objects.isNull(v) ? defaultVal : v;
  }

  @Nullable
  public static Float getFloat(@Nullable String value, @Nullable Float defaultVal) {
    if (Objects.isNull(value)) {
      return null;
    }
    return Convert.toFloat(value, defaultVal);
  }

  @Nullable
  public static Float getFloat(@Nullable String value) {
    return Convert.toFloat(value, null);
  }

  public static float getFloatPrimitive(@Nullable String value, float defaultVal) {
    Float v = getFloat(value);
    return Objects.isNull(v) ? defaultVal : v;
  }

  @Nullable
  public static Boolean getBoolean(@Nullable String value, @Nullable Boolean defaultVal) {
    if (Objects.isNull(value)) {
      return null;
    }
    return Convert.toBool(value, defaultVal);
  }

  @Nullable
  public static Boolean getBoolean(@Nullable String value) {
    return Convert.toBool(value, null);
  }

  public static boolean getBooleanPrimitive(@Nullable String value, boolean defaultVal) {
    Boolean v = getBoolean(value);
    return Objects.isNull(v) ? defaultVal : v;
  }
}
