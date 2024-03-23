package com.wangboot.core.auth.exception;

import lombok.Generated;
import lombok.Getter;

/**
 * 用户不存在异常
 *
 * @author wwtg99
 */
@Generated
public class NonExistsAccountException extends AuthenticationException {

  @Getter private final String account;

  public NonExistsAccountException(String account) {
    super(account + " not exist");
    this.account = account;
  }
}
