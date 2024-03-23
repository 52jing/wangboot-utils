package com.wangboot.core.auth.exception;

import lombok.Getter;

/**
 * 不存在的前端
 *
 * @author wwtg99
 */
public class NonExistsFrontendException extends AuthenticationException {

  @Getter private final String frontend;

  public NonExistsFrontendException(String frontend) {
    super("Frontend " + frontend + " not exist");
    this.frontend = frontend;
  }
}
