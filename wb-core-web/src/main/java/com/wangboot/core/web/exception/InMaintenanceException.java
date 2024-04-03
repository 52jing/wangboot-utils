package com.wangboot.core.web.exception;

public class InMaintenanceException extends RuntimeException {
  public InMaintenanceException(String msg) {
    super(msg);
  }
}
