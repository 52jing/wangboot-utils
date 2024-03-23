package com.wangboot.core.reliability.loginlock;

import com.wangboot.core.reliability.blacklist.IBlacklistHolder;
import com.wangboot.core.reliability.countlimit.ICountLimit;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * 基于缓存的锁定器
 *
 * @author wwtg99
 */
public class CacheFailedLock implements IFailedLock {

  private final ICountLimit countLimit;

  private final IBlacklistHolder blacklistHolder;

  /** 锁定时间（秒） */
  @Getter @Setter private long lockSeconds;

  public CacheFailedLock(
      @NonNull ICountLimit countLimit,
      @NonNull IBlacklistHolder blacklistHolder,
      long lockSeconds) {
    this.countLimit = countLimit;
    this.blacklistHolder = blacklistHolder;
    this.lockSeconds = lockSeconds;
  }

  @Override
  public long getFailedCount(String key) {
    return this.countLimit.getCount(key);
  }

  @Override
  public void incrementFailed(String key) {
    this.countLimit.increment(key);
  }

  @Override
  public boolean isLocked(String key) {
    return this.blacklistHolder.inBlacklist(key);
  }

  @Override
  public void lock(String key) {
    if (StringUtils.hasText(key)) {
      this.blacklistHolder.addBlacklist(key, this.lockSeconds * 1000);
    }
  }

  @Override
  public void unlock(String key) {
    if (StringUtils.hasText(key)) {
      this.blacklistHolder.removeBlacklist(key);
    }
  }
}
