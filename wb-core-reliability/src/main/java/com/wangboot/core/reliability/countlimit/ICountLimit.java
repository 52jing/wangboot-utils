package com.wangboot.core.reliability.countlimit;

/**
 * 计数限制器接口
 *
 * @author wwtg99
 */
public interface ICountLimit {

  /** 增加计数 */
  void increment(String key, long count);

  /** 增加计数 1 */
  default void increment(String key) {
    increment(key, 1);
  }

  /** 是否已达到限制 */
  boolean isLimited(String key);

  /** 获取当前计数 */
  long getCount(String key);

  /** 重置计数 */
  void reset(String key);
}
