package com.wangboot.core.auth.exception;

import lombok.Getter;

/**
 * 用户名密码不匹配异常
 *
 * @author wwtg99
 */
public class UsernamePasswordMismatchException extends AuthenticationException {

  @Getter private final String username;

  public UsernamePasswordMismatchException(String username) {
    super(username + " password mismatch");
    this.username = username;
  }
}
