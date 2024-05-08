package com.wangboot.model.entity.exception;

import lombok.Generated;

/**
 * 找不到异常
 *
 * @author wwtg99
 */
@Generated
public class NotFoundException extends RuntimeException {

  public NotFoundException(String msg) {
    super(msg);
  }

  public NotFoundException() {
    super();
  }
}
