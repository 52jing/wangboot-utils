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

  @Getter private final String prefix;

  @Getter private final long ttl;

  public CacheBlacklistHolder(String prefix, long ttl) {
    this.prefix = prefix;
    this.ttl = ttl;
  }

  @Override
  public void addBlacklist(String key, long ttl) {
    if (StringUtils.hasText(key)) {
      CacheUtil.put(prefix + key, "1", ttl);
    }
  }

  @Override
  public void addBlacklist(String key) {
    this.addBlacklist(key, this.ttl);
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
