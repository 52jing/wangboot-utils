package com.wangboot.core.captcha;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 验证码处理器持有者
 *
 * @author wwtg99
 */
public class CaptchaProcessorHolder {

  private static final ConcurrentMap<String, ICaptchaProcessor> processors =
      new ConcurrentHashMap<>();

  private CaptchaProcessorHolder() {}

  public static void addProcessor(String type, @Nullable ICaptchaProcessor processor) {
    if (StringUtils.hasText(type) && Objects.nonNull(processor)) {
      processors.put(type, processor);
    }
  }

  @Nullable
  public static ICaptchaProcessor getProcessor(String type) {
    if (StringUtils.hasText(type)) {
      return processors.get(type);
    }
    return null;
  }

  /** 校验验证码 */
  public static boolean verifyCaptcha(String type, String captcha, String uuid, boolean remove) {
    ICaptchaProcessor processor = getProcessor(type);
    if (Objects.nonNull(processor)) {
      return processor.validate(uuid, captcha, remove);
    }
    return false;
  }

  /** 校验验证码并删除 */
  public static boolean verifyCaptcha(String type, String captcha, String uuid) {
    return verifyCaptcha(type, captcha, uuid, true);
  }

  /** 生成验证码 */
  @Nullable
  public static ICaptchaData generateCaptcha(String type, String uuid) {
    ICaptchaProcessor processor = getProcessor(type);
    if (Objects.nonNull(processor)) {
      return processor.generate(uuid);
    }
    return null;
  }

  /** 发送验证码 */
  @NonNull
  public static Map<String, String> send(String type, @Nullable ICaptchaData captchaData) {
    ICaptchaProcessor processor = getProcessor(type);
    if (Objects.nonNull(processor) && Objects.nonNull(captchaData)) {
      return processor.send(captchaData);
    }
    return new HashMap<>();
  }

  /** 生成并发送验证码 */
  @NonNull
  public static Map<String, String> generateAndSend(String type, String uuid) {
    return send(type, generateCaptcha(type, uuid));
  }
}
