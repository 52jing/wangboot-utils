package com.wangboot.core.auth.exception;

import lombok.Getter;

/**
 * 用户已锁定异常
 *
 * @author wwtg99
 */
public class LockedAccountException extends AuthenticationException {

  @Getter private final String account;

  public LockedAccountException(String account) {
    super(account + " is locked");
    this.account = account;
  }
}
