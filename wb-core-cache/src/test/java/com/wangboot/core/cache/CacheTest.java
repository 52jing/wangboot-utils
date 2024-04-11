package com.wangboot.core.cache;

import cn.hutool.core.util.RandomUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CacheTest {

  @Test
  @SneakyThrows
  public void testCachePackage() {
    String s1 = RandomUtil.randomString(10);
    CachePackage cachePackage = new CachePackage(s1);
    Assertions.assertEquals(s1, cachePackage.toString());
    Assertions.assertTrue(cachePackage.isAlive());
    Assertions.assertEquals(s1, cachePackage.getData(String.class));
    Assertions.assertThrows(IllegalStateException.class, () -> cachePackage.getData(Integer.class));
    CachePackage cachePackage1 = new CachePackage(s1, 10);
    Assertions.assertEquals(10, cachePackage1.getTtl());
    Assertions.assertTrue(cachePackage.getCreateTime() <= System.currentTimeMillis());
    Assertions.assertNotEquals(cachePackage, cachePackage1);
    Assertions.assertEquals(cachePackage.getData(), cachePackage1.getData());
    Assertions.assertTrue(cachePackage1.isAlive());
    Thread.sleep(20);
    Assertions.assertFalse(cachePackage1.isAlive());
  }

  @Test
  @SneakyThrows
  public void testCacheUtils() {
    Assertions.assertNotNull(CacheUtil.getCache());
    String k1 = RandomUtil.randomString(6);
    Object v1 = CacheUtil.get(k1);
    Assertions.assertNull(v1);
    String s1 = RandomUtil.randomString(8);
    Object v2 = CacheUtil.get(k1, s1);
    Assertions.assertNotNull(v2);
    Assertions.assertEquals(s1, v2.toString());
    String v21 = CacheUtil.get("", String.class, null);
    Assertions.assertNull(v21);
    CacheUtil.put(k1, s1);
    Assertions.assertTrue(CacheUtil.has(k1));
    Object v3 = CacheUtil.get(k1);
    Assertions.assertNotNull(v3);
    Assertions.assertEquals(s1, v3.toString());
    String v22 = CacheUtil.get(k1, String.class, null);
    Assertions.assertEquals(s1, v22);
    CacheUtil.remove(k1);
    Assertions.assertFalse(CacheUtil.has(k1));
    CacheUtil.put(k1, s1, 1000L);
    Assertions.assertTrue(CacheUtil.has(k1));
    Thread.sleep(1001);
    Assertions.assertFalse(CacheUtil.has(k1));
    CacheUtil.clear();
    String cacheName = RandomUtil.randomString(6);
    Object v4 = CacheUtil.get(cacheName, k1, null);
    Assertions.assertNull(v4);
    String s2 = RandomUtil.randomString(8);
    Object v5 = CacheUtil.get(cacheName, k1, s2);
    Assertions.assertNotNull(v5);
    Assertions.assertEquals(s2, v5.toString());
    CacheUtil.put(cacheName, k1, s2, 0);
    Assertions.assertTrue(CacheUtil.has(cacheName, k1));
    Object v6 = CacheUtil.get(cacheName, k1, null);
    Assertions.assertNotNull(v6);
    Assertions.assertEquals(s2, v6.toString());
    CacheUtil.remove(cacheName, k1);
    Assertions.assertFalse(CacheUtil.has(cacheName, k1));
    Assertions.assertNull(CacheUtil.get("", null));
    Assertions.assertEquals("1", CacheUtil.get("cache", "", String.class, "1"));
    Assertions.assertEquals("2", CacheUtil.get("", RandomUtil.randomString(6), String.class, "2"));
    CacheUtil.put(k1, s1, 10);
    Thread.sleep(20);
    Assertions.assertNull(CacheUtil.get(k1, String.class));
    Object v7 = CacheUtil.getOrSet(k1, 50, () -> s1);
    Assertions.assertNotNull(v7);
    Assertions.assertEquals(s1, v7.toString());
    Object v8 = CacheUtil.getOrSet(k1, 50, () -> s2);
    Assertions.assertNotNull(v8);
    Assertions.assertEquals(s1, v8.toString());
    String v9 = CacheUtil.getOrSet(k1, String.class, 50, () -> s2);
    Assertions.assertEquals(s1, v9);
    CacheUtil.clear(cacheName);
  }
}
