package com.wangboot.model.entity.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * 字段筛选条件
 *
 * @author wwtg99
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class FieldFilter implements Serializable {
  private String field;
  private String val = "";
  private FilterOperator operator = FilterOperator.EQ;
  private ParamValType type = ParamValType.STR;

  @Generated
  public FieldFilter(String field, FilterOperator operator, ParamValType type) {
    this(field, "", operator, type);
  }

  @Generated
  public FieldFilter(String field, String val) {
    this.field = field;
    this.val = val;
  }

  @Generated
  public FieldFilter(String field) {
    this.field = field;
  }
}
