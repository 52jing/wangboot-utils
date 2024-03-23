package com.wangboot.model.entity.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 排序筛选条件
 *
 * @author wwtg99
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class SortFilter implements Serializable {
  private String field;
  private boolean asc = true;

  public SortFilter(String field) {
    this.field = field;
  }
}
