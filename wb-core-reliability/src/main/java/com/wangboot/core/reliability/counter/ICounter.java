package com.wangboot.core.reliability.counter;

/**
 * 计数限制器接口
 *
 * @author wwtg99
 */
public interface ICounter {

  /** 增加计数 */
  long increment(String key, long count);

  /** 增加计数 1 */
  default long increment(String key) {
    return increment(key, 1);
  }

  /** 获取当前计数 */
  long getCount(String key);

  /** 重置计数 */
  void reset(String key);
}
