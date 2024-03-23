package com.wangboot.core.cache;

import java.io.Serializable;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 缓存对象增加存活时间
 *
 * @author wwtg99
 */
@EqualsAndHashCode
public class CachePackage implements Serializable {

  /** 数据 */
  @Getter private final Object data;

  /** 创建时间(毫秒) */
  @Getter private final long createTime;

  /** 存活时间(毫秒) */
  @Getter private final long ttl;

  /**
   * @param data 数据
   * @param ttl 存活时间（毫秒）
   */
  public CachePackage(Object data, long ttl) {
    this.data = data;
    this.createTime = System.currentTimeMillis();
    this.ttl = ttl;
  }

  /** @param data 数据 */
  public CachePackage(Object data) {
    this(data, 0);
  }

  public <T> T getData(Class<T> type) {
    if (Objects.nonNull(this.data) && Objects.nonNull(type) && !type.isInstance(this.data)) {
      throw new IllegalStateException(
          "Cached value is not of required type [" + type.getName() + "]: " + this.data);
    }
    return type.cast(this.data);
  }

  /** 是否存活 */
  public boolean isAlive() {
    if (this.ttl == 0) {
      return true;
    }
    return System.currentTimeMillis() < this.createTime + this.ttl;
  }

  @Override
  public String toString() {
    return data.toString();
  }
}
