package com.wangboot.core.reliability.blacklist;

/**
 * 黑名单管理器接口
 *
 * @author wwtg99
 */
public interface IBlacklistHolder {
  /**
   * 加入黑名单
   *
   * @param key 键
   * @param secs 存活时间（秒）
   */
  void addBlacklist(String key, long secs);

  /**
   * 加入黑名单
   *
   * @param key 键
   */
  void addBlacklist(String key);

  /**
   * 移除黑名单
   *
   * @param key 键
   */
  void removeBlacklist(String key);

  /**
   * 是否在黑名单
   *
   * @param key 键
   * @return boolean
   */
  boolean inBlacklist(String key);
}
