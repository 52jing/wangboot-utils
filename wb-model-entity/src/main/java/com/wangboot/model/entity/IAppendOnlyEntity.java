package com.wangboot.model.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 仅用于插入实体接口，支持创建人和创建时间
 *
 * @author wwtg99
 */
public interface IAppendOnlyEntity extends Serializable {

  String getCreatedBy();

  void setCreatedBy(String createdBy);

  OffsetDateTime getCreatedTime();

  void setCreatedTime(OffsetDateTime offsetDateTime);
}
