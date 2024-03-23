package com.wangboot.core.captcha.exception;

/**
 * 验证码图片错误
 *
 * @author wwtg99
 */
public class InvalidCaptchaImageException extends RuntimeException {

  public InvalidCaptchaImageException(String errMsg, Throwable cause) {
    super(errMsg, cause);
  }

  public InvalidCaptchaImageException(String errMsg) {
    super(errMsg);
  }

  public InvalidCaptchaImageException() {
    super();
  }
}
