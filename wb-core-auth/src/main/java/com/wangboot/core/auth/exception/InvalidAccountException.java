package com.wangboot.core.auth.exception;

import lombok.Getter;

/**
 * 无效的账号
 *
 * @author wwtg99
 */
public class InvalidAccountException extends AuthenticationException {

  @Getter private final String account;

  public InvalidAccountException(String account) {
    super(account + " is invalid");
    this.account = account;
  }
}
