package com.wangboot.core.auth.exception;

/**
 * 没有权限异常
 *
 * @author wwtg99
 */
public class PermissionDeniedException extends AuthenticationException {

  public PermissionDeniedException(String msg) {
    super(msg);
  }

  public PermissionDeniedException() {
    super();
  }
}
