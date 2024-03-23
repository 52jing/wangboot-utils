package com.wangboot.core.captcha;

import java.util.Map;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 验证码处理器接口
 *
 * @author wwtg99
 */
public interface ICaptchaProcessor {

  /** 获取存取器 */
  @NonNull
  ICaptchaRepository getRepository();

  /** 生成验证码，用于第一次生成随机验证码 */
  @NonNull
  ICaptchaData generateCaptcha(String uid);

  /** 从 code 生成验证码，用于验证 */
  @NonNull
  ICaptchaData fromCode(String uid, String code);

  /** 生成验证码并存储 */
  @NonNull
  default ICaptchaData generate(String uid) {
    ICaptchaData captchaData = generateCaptcha(uid);
    getRepository().save(captchaData);
    return captchaData;
  }

  /**
   * 校验验证码
   *
   * @param captcha 验证码
   * @param remove 验证后是否删除
   * @return boolean
   */
  default boolean validate(@Nullable ICaptchaData captcha, boolean remove) {
    if (Objects.isNull(captcha) || !StringUtils.hasText(captcha.getUid())) {
      return false;
    }
    ICaptchaData saved = getRepository().get(captcha.getUid());
    if (Objects.isNull(saved)) {
      // 不存在
      return false;
    }
    // 一次性，匹配后删除
    if (remove) {
      getRepository().remove(captcha.getUid());
    }
    return saved.match(captcha);
  }

  /**
   * 校验验证码
   *
   * @param uid 验证码唯一码
   * @param code 验证码值
   * @param remove 验证后是否删除
   * @return boolean
   */
  default boolean validate(String uid, String code, boolean remove) {
    return validate(fromCode(uid, code), remove);
  }

  /** 发送验证码 */
  @NonNull
  Map<String, String> send(@Nullable ICaptchaData captcha);
}
