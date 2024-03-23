package com.wangboot.core.web.exception;

import java.io.Serializable;
import lombok.Generated;
import lombok.Getter;

/**
 * 级联删除失败异常
 *
 * @author wwtg99
 */
@Generated
public class DeleteCascadeFailedException extends RuntimeException {

  @Getter private final Serializable deleteId;

  public DeleteCascadeFailedException(Serializable id, Throwable cause) {
    super(cause);
    this.deleteId = id;
  }

  public DeleteCascadeFailedException(Serializable id) {
    super();
    this.deleteId = id;
  }
}
