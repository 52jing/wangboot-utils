package com.wangboot.core.reliability.counter;

import com.wangboot.core.cache.CacheUtil;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * 基于缓存的计数器
 *
 * @author wwtg99
 */
@AllArgsConstructor
public class CacheCounter implements ICounter {

  private static final String DEFAULT_PREFIX = "LIMIT:";

  /** 计数周期时间（秒） */
  @Getter @Setter private long periodSeconds;

  /** 前缀 */
  @Getter private final String prefix;

  public CacheCounter(long periodSeconds) {
    this(periodSeconds, DEFAULT_PREFIX);
  }

  @Override
  public long increment(String key, long count) {
    if (StringUtils.hasText(key)) {
      synchronized (this) {
        long n = this.getCount(key);
        long m = n + count;
        CacheUtil.put(this.getKey(key), m, this.periodSeconds * 1000);
        return m;
      }
    }
    return 0;
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
