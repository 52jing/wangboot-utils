package com.wangboot.core.reliability;

import cn.hutool.core.util.RandomUtil;
import com.wangboot.core.reliability.blacklist.CacheBlacklistHolder;
import com.wangboot.core.reliability.blacklist.IBlacklistHolder;
import com.wangboot.core.reliability.countlimit.CacheCountLimit;
import com.wangboot.core.reliability.countlimit.ICountLimit;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReliabilityTest {
  @Test
  @SneakyThrows
  public void testRateLimit() {
    ICountLimit rateLimit = new CacheCountLimit(3, 1);
    String k1 = RandomUtil.randomString(6);
    Assertions.assertFalse(rateLimit.isLimited(k1));
    rateLimit.increment(k1);
    Assertions.assertFalse(rateLimit.isLimited(k1));
    rateLimit.increment(k1);
    Assertions.assertFalse(rateLimit.isLimited(k1));
    rateLimit.increment(k1);
    Assertions.assertTrue(rateLimit.isLimited(k1));
    Thread.sleep(1100);
    Assertions.assertFalse(rateLimit.isLimited(k1));
  }

  @Test
  @SneakyThrows
  public void testBlacklist() {
    IBlacklistHolder blacklistHolder = new CacheBlacklistHolder("test", 1000);
    String token1 = RandomUtil.randomString(32);
    Assertions.assertFalse(blacklistHolder.inBlacklist(token1));
    blacklistHolder.addBlacklist(token1, 1000);
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
