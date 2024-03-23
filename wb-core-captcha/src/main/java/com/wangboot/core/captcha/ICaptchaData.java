package com.wangboot.core.captcha;

import java.io.Serializable;
import org.springframework.lang.Nullable;

/**
 * 验证码接口
 *
 * @author wwtg99
 */
public interface ICaptchaData extends Serializable {
  /** 验证码值 */
  String getCode();

  /** 验证码识别码，用于缓存键 */
  String getUid();

  /** 是否匹配 */
  boolean match(@Nullable ICaptchaData captcha);
}
