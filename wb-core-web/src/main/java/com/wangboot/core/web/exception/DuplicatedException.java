package com.wangboot.core.web.exception;

import lombok.Generated;

/**
 * 数据重复异常
 *
 * @author wwtg99
 */
@Generated
public class DuplicatedException extends RuntimeException {

  public DuplicatedException(Throwable cause) {
    super(cause);
  }

  public DuplicatedException() {
    super();
  }
}
