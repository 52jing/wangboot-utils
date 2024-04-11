package com.wangboot.core.cache;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.extra.spring.SpringUtil;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 缓存操作工具类
 *
 * @author wwtg99
 */
public class CacheUtil {
  public static String defaultCacheName = "cache";

  private static CacheManager cacheManager;
  private static final Object lock = new Object();

  private CacheUtil() {}

  /**
   * 获取缓存管理器
   *
   * @return CacheManager
   */
  @Nullable
  public static CacheManager getCacheManager() {
    try {
      return SpringUtil.getBean(CacheManager.class);
    } catch (UtilException | NullPointerException | NoSuchBeanDefinitionException e) {
      synchronized (lock) {
        if (Objects.isNull(cacheManager)) {
          cacheManager = new ConcurrentMapCacheManager();
        }
      }
      return cacheManager;
    }
  }

  /**
   * 获取缓存
   *
   * @param name 缓存名称
   * @return Cache
   */
  @Nullable
  public static Cache getCache(String name) {
    if (StringUtils.hasText(name)) {
      return Optional.ofNullable(getCacheManager()).map((m) -> m.getCache(name)).orElse(null);
    }
    return null;
  }

  /**
   * 获取默认缓存
   *
   * @return Cache
   */
  @Nullable
  public static Cache getCache() {
    return getCache(defaultCacheName);
  }

  /**
   * 获取缓存数据
   *
   * @param name 缓存名称
   * @param key 缓存键
   * @param defaultValue 默认值
   * @return 缓存值
   */
  @Nullable
  public static <T> T get(String name, String key, Class<T> type, @Nullable T defaultValue) {
    if (!StringUtils.hasText(key)) {
      return defaultValue;
    }
    Cache cache = getCache(name);
    if (Objects.isNull(cache)) {
      return defaultValue;
    }
    CachePackage cachePackage = cache.get(key, CachePackage.class);
    if (Objects.isNull(cachePackage)) {
      return defaultValue;
    }
    if (cachePackage.isAlive()) {
      return cachePackage.getData(type);
    } else {
      // delete cache
      cache.evict(key);
    }
    return defaultValue;
  }

  /**
   * 获取缓存数据
   *
   * @param name 缓存名称
   * @param key 缓存键
   * @param defaultValue 默认值
   * @return 缓存值
   */
  @Nullable
  public static Object get(String name, String key, @Nullable Object defaultValue) {
    return get(name, key, Object.class, defaultValue);
  }

  /**
   * 获取缓存数据
   *
   * @param key 缓存键
   * @param defaultValue 默认值
   * @return 缓存值
   */
  @Nullable
  public static Object get(String key, @Nullable Object defaultValue) {
    return get(defaultCacheName, key, defaultValue);
  }

  /**
   * 获取缓存数据
   *
   * @param key 缓存键
   * @return 缓存值
   */
  @Nullable
  public static Object get(String key) {
    return get(defaultCacheName, key, null);
  }

  /**
   * 获取缓存数据
   *
   * @param key 缓存键
   * @param type 值类型
   * @param defaultValue 默认值
   * @param <T> 值类型
   * @return 缓存值
   */
  @Nullable
  public static <T> T get(String key, Class<T> type, @Nullable T defaultValue) {
    return get(defaultCacheName, key, type, defaultValue);
  }

  /**
   * 获取缓存数据
   *
   * @param key 缓存键
   * @param type 值类型
   * @param <T> 值类型
   * @return 缓存值
   */
  @Nullable
  public static <T> T get(String key, Class<T> type) {
    return get(defaultCacheName, key, type, null);
  }

  /**
   * 获取缓存，不存在则设置
   *
   * @param name 缓存名称
   * @param key 缓存键
   * @param type 值类型
   * @param ttl 存活时间（毫秒）, 0 则不限时
   * @param setIfAbsent 不存在获取数据函数
   * @param <T> 值类型
   * @return 缓存值
   */
  @Nullable
  public static <T> T getOrSet(
      String name, String key, Class<T> type, long ttl, @Nullable Supplier<T> setIfAbsent) {
    T data = get(name, key, type, null);
    if (Objects.isNull(data) && Objects.nonNull(setIfAbsent)) {
      // 不存在则使用函数获取并赋值
      data = setIfAbsent.get();
      if (Objects.nonNull(data)) {
        put(name, key, data, ttl);
      }
    }
    return data;
  }

  /**
   * 获取缓存，不存在则设置
   *
   * @param key 缓存键
   * @param type 值类型
   * @param ttl 存活时间（毫秒）, 0 则不限时
   * @param setIfAbsent 不存在获取数据函数
   * @param <T> 值类型
   * @return 缓存值
   */
  @Nullable
  public static <T> T getOrSet(
      String key, Class<T> type, long ttl, @Nullable Supplier<T> setIfAbsent) {
    return getOrSet(defaultCacheName, key, type, ttl, setIfAbsent);
  }

  /**
   * 获取缓存，不存在则设置
   *
   * @param key 缓存键
   * @param ttl 存活时间（毫秒）, 0 则不限时
   * @param setIfAbsent 不存在获取数据函数
   * @return 缓存值
   */
  @Nullable
  public static Object getOrSet(String key, long ttl, @Nullable Supplier<Object> setIfAbsent) {
    return getOrSet(defaultCacheName, key, Object.class, ttl, setIfAbsent);
  }

  /**
   * 设置缓存
   *
   * @param name 缓存名称
   * @param key 缓存键
   * @param value 缓存值
   * @param ttl 存活时间（毫秒）, 0 则不限时
   */
  public static void put(String name, String key, @Nullable Object value, long ttl) {
    Cache cache = getCache(name);
    if (Objects.nonNull(cache)) {
      CachePackage cachePackage = new CachePackage(value, ttl);
      cache.put(key, cachePackage);
    }
  }

  /**
   * 设置缓存
   *
   * @param key 缓存键
   * @param value 缓存值
   * @param ttl 存活时间（毫秒）, 0 则不限时
   */
  public static void put(String key, @Nullable Object value, long ttl) {
    put(defaultCacheName, key, value, ttl);
  }

  /**
   * 设置缓存
   *
   * @param key 缓存键
   * @param value 缓存值
   */
  public static void put(String key, @Nullable Object value) {
    put(defaultCacheName, key, value, 0);
  }

  /**
   * 缓存键是否存在
   *
   * @param name 缓存名称
   * @param key 缓存键
   * @return boolean
   */
  public static boolean has(String name, String key) {
    Object obj = get(name, key, null);
    return Objects.nonNull(obj);
  }

  /**
   * 缓存键是否存在
   *
   * @param key 缓存键
   * @return boolean
   */
  public static boolean has(String key) {
    return has(defaultCacheName, key);
  }

  /**
   * 删除缓存
   *
   * @param name 缓存名称
   * @param key 缓存键
   */
  public static void remove(String name, String key) {
    Cache cache = getCache(name);
    if (Objects.nonNull(cache)) {
      cache.evict(key);
    }
  }

  /**
   * 删除缓存
   *
   * @param key 缓存键
   */
  public static void remove(String key) {
    remove(defaultCacheName, key);
  }

  /**
   * 清空缓存
   *
   * @param name 缓存名称
   */
  public static void clear(String name) {
    Cache cache = getCache(name);
    if (Objects.nonNull(cache)) {
      cache.clear();
    }
  }

  /** 清空缓存 */
  public static void clear() {
    clear(defaultCacheName);
  }

  //  @FunctionalInterface
  //  public interface SetIfAbsent<T> {
  //    T set();
  //  }
}
