package com.wangboot.model.entity.request;

import lombok.Generated;

/**
 * 搜索匹配策略
 *
 * @author wwtg99
 */
@Generated
public enum SearchStrategy {
  // str%
  LEFT_LIKE,
  // %str
  RIGHT_LIKE,
  // %str%
  BOTH_LIKE
}
