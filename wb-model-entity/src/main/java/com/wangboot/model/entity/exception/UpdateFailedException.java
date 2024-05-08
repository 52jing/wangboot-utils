package com.wangboot.model.entity.exception;

import java.io.Serializable;
import lombok.Generated;
import lombok.Getter;

/**
 * 更新失败异常
 *
 * @author wwtg99
 */
@Generated
public class UpdateFailedException extends RuntimeException {

  @Getter private final Serializable updateId;

  public UpdateFailedException(Serializable id, Throwable cause) {
    super(cause);
    this.updateId = id;
  }

  public UpdateFailedException(Serializable id) {
    super();
    this.updateId = id;
  }
}
