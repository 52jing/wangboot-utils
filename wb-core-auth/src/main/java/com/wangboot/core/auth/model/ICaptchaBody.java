package com.wangboot.core.auth.model;

import java.io.Serializable;

/**
 * 验证码相关模型接口
 *
 * @author wwtg99
 */
public interface ICaptchaBody extends Serializable {
  /** 获取验证码类型 */
  String getCaptchaType();
  /** 设置验证码类型 */
  void setCaptchaType(String captchaType);

  /** 获取验证码 */
  String getCaptcha();
  /** 设置验证码 */
  void setCaptcha(String captcha);

  /** 获取验证码标识符 */
  String getUuid();
  /** 设置验证码标识符 */
  void setUuid(String uuid);
}
