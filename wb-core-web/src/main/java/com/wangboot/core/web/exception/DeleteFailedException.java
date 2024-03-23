package com.wangboot.core.web.exception;

import java.io.Serializable;
import lombok.Generated;
import lombok.Getter;

/**
 * 删除失败异常
 *
 * @author wwtg99
 */
@Generated
public class DeleteFailedException extends RuntimeException {

  @Getter private final Serializable deleteId;

  public DeleteFailedException(Serializable id, Throwable cause) {
    super(cause);
    this.deleteId = id;
  }

  public DeleteFailedException(Serializable id) {
    super();
    this.deleteId = id;
  }
}
