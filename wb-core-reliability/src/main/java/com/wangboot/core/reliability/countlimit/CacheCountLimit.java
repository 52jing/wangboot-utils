package com.wangboot.core.reliability.countlimit;

import com.wangboot.core.cache.CacheUtil;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * 基于缓存的计数器
 *
 * @author wwtg99
 */
@AllArgsConstructor
public class CacheCountLimit implements ICountLimit {

  private static final String DEFAULT_PREFIX = "LIMIT:";

  /** 限制数量 */
  @Getter private final long limitThreshold;

  /** 计数周期时间（秒） */
  @Getter private final long periodSeconds;

  /** 前缀 */
  @Getter private final String prefix;

  public CacheCountLimit(long limitThreshold, long periodSeconds) {
    this(limitThreshold, periodSeconds, DEFAULT_PREFIX);
  }

  @Override
  public void increment(String key, long count) {
    if (StringUtils.hasText(key)) {
      synchronized (this) {
        long n = this.getCount(key);
        CacheUtil.put(this.getKey(key), n + count, this.periodSeconds);
      }
    }
  }

  @Override
  public boolean isLimited(String key) {
    long n = this.getCount(key);
    return n >= this.limitThreshold;
  }

  @Override
  public long getCount(String key) {
    if (StringUtils.hasText(key)) {
      Object obj = CacheUtil.get(this.getKey(key));
      if (Objects.nonNull(obj) && obj instanceof Long) {
        return (Long) obj;
      }
    }
    return 0;
  }

  @Override
  public void reset(String key) {
    if (StringUtils.hasText(key)) {
      CacheUtil.remove(this.getKey(key));
    }
  }

  private String getKey(String key) {
    return this.prefix + key;
  }
}
