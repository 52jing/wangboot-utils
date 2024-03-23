package com.wangboot.core.auth.exception;

/**
 * 登录失败
 *
 * @author wwtg99
 */
public class LoginFailedException extends AuthenticationException {

  public LoginFailedException(String msg) {
    super(msg);
  }

  public LoginFailedException() {
    super();
  }
}
