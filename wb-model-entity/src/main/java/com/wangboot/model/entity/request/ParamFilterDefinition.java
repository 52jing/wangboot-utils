package com.wangboot.model.entity.request;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 筛选参数定义
 *
 * @author wwtg99
 */
public class ParamFilterDefinition {

  @Getter private final Map<String, FieldFilter> params;

  public ParamFilterDefinition() {
    params = new HashMap<>();
  }

  public ParamFilterDefinition addFilter(String param, @Nullable FieldFilter definition) {
    this.params.put(param, definition);
    return this;
  }

  public ParamFilterDefinition addFilter(String param) {
    return addFilter(param, null);
  }

  @NonNull
  public static ParamFilterDefinition newInstance() {
    return new ParamFilterDefinition();
  }
}
