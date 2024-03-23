package com.wangboot.model.entity.request;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 筛选参数定义
 *
 * @author wwtg99
 */
public class ParamFilterDefinition {

  @Getter private final Map<String, Definition> params;

  public ParamFilterDefinition() {
    params = new HashMap<>();
  }

  public ParamFilterDefinition addFilter(String param, @Nullable Definition definition) {
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

  @Data
  @AllArgsConstructor
  public static class Definition {
    private String field;
    private FilterOperator operator = FilterOperator.EQ;
    private ParamValType type = ParamValType.STR;

    public Definition(String field) {
      this.field = field;
    }
  }
}
