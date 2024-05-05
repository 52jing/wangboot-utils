package com.wangboot.core.reliability;

import cn.hutool.core.util.RandomUtil;
import com.wangboot.core.reliability.blacklist.CacheBlacklistHolder;
import com.wangboot.core.reliability.blacklist.IBlacklistHolder;
import com.wangboot.core.reliability.counter.CacheCounter;
import com.wangboot.core.reliability.counter.ICounter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReliabilityTest {
  @Test
  @SneakyThrows
  public void testCountLimit() {
    ICounter counter = new CacheCounter(1);
    String k1 = RandomUtil.randomString(6);
    Assertions.assertEquals(0, counter.getCount(k1));
    Assertions.assertEquals(1, counter.increment(k1));
    Assertions.assertEquals(2, counter.increment(k1));
    Thread.sleep(1100);
    Assertions.assertEquals(0, counter.getCount(k1));
    Assertions.assertEquals(1, counter.increment(k1));
    Assertions.assertEquals(2, counter.increment(k1));
    counter.reset(k1);
    Assertions.assertEquals(0, counter.getCount(k1));
  }

  @Test
  @SneakyThrows
  public void testBlacklist() {
    IBlacklistHolder blacklistHolder = new CacheBlacklistHolder("test", 1);
    String token1 = RandomUtil.randomString(32);
    Assertions.assertFalse(blacklistHolder.inBlacklist(token1));
    blacklistHolder.addBlacklist(token1, 1);
    Assertions.assertTrue(blacklistHolder.inBlacklist(token1));
    blacklistHolder.removeBlacklist(token1);
    Assertions.assertFalse(blacklistHolder.inBlacklist(token1));
    blacklistHolder.addBlacklist(token1);
    Assertions.assertTrue(blacklistHolder.inBlacklist(token1));
    Thread.sleep(1100);
    Assertions.assertFalse(blacklistHolder.inBlacklist(token1));
    Assertions.assertTrue(blacklistHolder.inBlacklist(null));
  }
}
