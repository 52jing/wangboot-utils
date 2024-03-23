package com.wangboot.core.captcha;

import org.springframework.lang.Nullable;

/**
 * 验证码存取器接口
 *
 * @author wwtg99
 */
public interface ICaptchaRepository {

  /**
   * 缓存验证码
   *
   * @param captcha 验证码
   */
  void save(ICaptchaData captcha);

  /**
   * 获取缓存的验证码
   *
   * @param uid 唯一码
   * @return 验证码
   */
  @Nullable
  ICaptchaData get(String uid);

  /**
   * 移除缓存的验证码
   *
   * @param uid 唯一码
   */
  void remove(String uid);
}
