package com.wangboot.core.auth.exception;

import lombok.Generated;

/**
 * 认证异常
 *
 * @author wwtg99
 */
@Generated
public abstract class AuthenticationException extends RuntimeException {

  public AuthenticationException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public AuthenticationException(String msg) {
    super(msg);
  }

  public AuthenticationException() {
    super();
  }
}
