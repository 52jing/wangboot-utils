package com.wangboot.core.reliability.loginlock;

/**
 * 计时锁定器
 *
 * @author wwtg99
 */
public interface IFailedLock {

  /**
   * 获取失败次数
   *
   * @param key 键
   * @return 次数
   */
  long getFailedCount(String key);

  /**
   * 增加失败次数
   *
   * @param key 键
   */
  void incrementFailed(String key);

  /**
   * 是否已锁定
   *
   * @param key 键
   * @return boolean
   */
  boolean isLocked(String key);

  /**
   * 锁定
   *
   * @param key 键
   */
  void lock(String key);

  /**
   * 解锁
   *
   * @param key 键
   */
  void unlock(String key);
}
