package com.wangboot.model.entity.request;

import lombok.Generated;
import lombok.Getter;

/**
 * 参数运算符
 *
 * @author wwtg99
 */
@Generated
public enum FilterOperator {
  EQ(1, "eq"),
  GT(2, "gt"),
  GE(3, "ge"),
  LT(4, "lt"),
  LE(5, "le"),
  NE(6, "ne"),
  IN(7, "in"),
  STARTSWITH(8, "startswith"),
  ENDSWITH(9, "endswith"),
  CONTAINS(10, "contains"),
  NULL(11, "null"),
  NONNULL(12, "nonNull"),
  EMPTY(13, "empty"),
  NONEMPTY(14, "nonEmpty");

  @Getter private final int id;

  @Getter private final String name;

  FilterOperator(int id, String name) {
    this.id = id;
    this.name = name;
  }
}
