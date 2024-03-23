package com.wangboot.core.captcha.repository;

import com.wangboot.core.cache.CacheUtil;
import com.wangboot.core.captcha.ICaptchaData;
import com.wangboot.core.captcha.ICaptchaRepository;
import org.springframework.lang.Nullable;

/**
 * 基于 Cache 模块的存取器实现
 *
 * @author wwtg99
 */
public class CacheCaptchaRepository implements ICaptchaRepository {

  /** 缓存时间（毫秒） */
  private final long ttl;

  public CacheCaptchaRepository(long ttl) {
    this.ttl = ttl;
  }

  @Override
  public void save(ICaptchaData captcha) {
    CacheUtil.put(captcha.getUid(), captcha, this.ttl);
  }

  @Override
  @Nullable
  public ICaptchaData get(String uid) {
    Object obj = CacheUtil.get(uid);
    if (obj instanceof ICaptchaData) {
      return (ICaptchaData) obj;
    }
    return null;
  }

  @Override
  public void remove(String uid) {
    CacheUtil.remove(uid);
  }
}
