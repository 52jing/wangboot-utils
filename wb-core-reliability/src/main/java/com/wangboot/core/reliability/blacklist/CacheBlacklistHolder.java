package com.wangboot.core.reliability.blacklist;

import com.wangboot.core.cache.CacheUtil;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * 缓存黑名单管理器
 *
 * @author wwtg99
 */
public class CacheBlacklistHolder implements IBlacklistHolder {

  /** 前缀 */
  @Getter private final String prefix;

  /** 加入黑名单时间（秒） */
  @Getter private final long secs;

  public CacheBlacklistHolder(String prefix, long secs) {
    this.prefix = prefix;
    this.secs = secs;
  }

  @Override
  public void addBlacklist(String key, long secs) {
    if (StringUtils.hasText(key)) {
      CacheUtil.put(prefix + key, "1", secs * 1000);
    }
  }

  @Override
  public void addBlacklist(String key) {
    this.addBlacklist(key, this.secs);
  }

  @Override
  public void removeBlacklist(String key) {
    if (StringUtils.hasText(key)) {
      CacheUtil.remove(prefix + key);
    }
  }

  @Override
  public boolean inBlacklist(String key) {
    if (StringUtils.hasText(key)) {
      return CacheUtil.has(prefix + key);
    }
    return true;
  }
}
