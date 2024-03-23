package com.wangboot.model.entity.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
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
  private String val;
  private FilterOperator operator = FilterOperator.EQ;
  private ParamValType type = ParamValType.STR;

  public FieldFilter(String field, String val) {
    this.field = field;
    this.val = val;
  }
}
