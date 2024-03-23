package com.wangboot.core.auth.exception;

import lombok.Getter;

/**
 * 登录锁定
 *
 * @author wwtg99
 */
public class LoginLockedException extends AuthenticationException {

  @Getter private final String account;

  public LoginLockedException(String account) {
    super(account + " is login locked");
    this.account = account;
  }
}
