package com.wangboot.core.web.param;

import com.wangboot.core.utils.StrUtils;
import java.util.Collections;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 动态参数配置接口
 *
 * @author wwtg99
 */
public interface IParamConfig {

  /** 参数列表分隔符 */
  String PARAM_LIST_DELIMITER = ",";

  @Nullable
  String getParamConfig(@Nullable String key);

  @NonNull
  default List<String> getParamConfigAsStrList(@Nullable String key) {
    return getStrList(getParamConfig(key));
  }

  @NonNull
  default List<Integer> getParamConfigAsIntList(@Nullable String key) {
    return getIntList(getParamConfig(key));
  }

  @NonNull
  default List<Long> getParamConfigAsLongList(@Nullable String key) {
    return getLongList(getParamConfig(key));
  }

  @NonNull
  default List<Float> getParamConfigAsFloatList(@Nullable String key) {
    return getFloatList(getParamConfig(key));
  }

  @NonNull
  default List<Boolean> getParamConfigAsBooleanList(@Nullable String key) {
    return getBooleanList(getParamConfig(key));
  }

  @NonNull
  default List<String> getStrList(@Nullable String val) {
    if (StringUtils.hasText(val)) {
      return StrUtils.splitStrList(val, PARAM_LIST_DELIMITER);
    }
    return Collections.emptyList();
  }

  @NonNull
  default List<Integer> getIntList(@Nullable String val) {
    if (StringUtils.hasText(val)) {
      return StrUtils.splitIntList(val, PARAM_LIST_DELIMITER);
    }
    return Collections.emptyList();
  }

  @NonNull
  default List<Long> getLongList(@Nullable String val) {
    if (StringUtils.hasText(val)) {
      return StrUtils.splitLongList(val, PARAM_LIST_DELIMITER);
    }
    return Collections.emptyList();
  }

  @NonNull
  default List<Float> getFloatList(@Nullable String val) {
    if (StringUtils.hasText(val)) {
      return StrUtils.splitFloatList(val, PARAM_LIST_DELIMITER);
    }
    return Collections.emptyList();
  }

  @NonNull
  default List<Boolean> getBooleanList(@Nullable String val) {
    if (StringUtils.hasText(val)) {
      return StrUtils.splitBooleanList(val, PARAM_LIST_DELIMITER);
    }
    return Collections.emptyList();
  }
}
