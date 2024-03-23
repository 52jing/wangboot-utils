package com.wangboot.core.auth.exception;

import lombok.Getter;

/**
 * 账户已超期异常
 *
 * @author wwtg99
 */
public class ExpiredAccountException extends AuthenticationException {

  @Getter private final String account;

  public ExpiredAccountException(String account) {
    super(account + " is expired");
    this.account = account;
  }
}
