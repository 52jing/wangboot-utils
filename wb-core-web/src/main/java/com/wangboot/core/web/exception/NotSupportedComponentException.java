package com.wangboot.core.web.exception;

/**
 * 不支持的组件异常
 *
 * @author wwtg99
 */
public class NotSupportedComponentException extends RuntimeException {
  public NotSupportedComponentException(String msg) {
    super(msg);
  }
}
