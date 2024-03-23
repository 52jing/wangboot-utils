package com.wangboot.model.entity.impl;

import com.wangboot.model.entity.IAppendOnlyEntity;
import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.Data;

/**
 * 仅用于插入实体抽象类，支持创建人和创建时间
 *
 * @author wwtg99
 */
@Data
public abstract class AppendOnlyEntity implements IAppendOnlyEntity, Serializable {

  private String createdBy;
  private OffsetDateTime createdTime;
}
