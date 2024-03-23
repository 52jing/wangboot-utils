package com.wangboot.model.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 普通实体接口，支持创建人、创建时间、更新人、更新时间、备注
 *
 * @author wwtg99
 */
public interface ICommonEntity extends Serializable {

  String getCreatedBy();

  void setCreatedBy(String createdBy);

  OffsetDateTime getCreatedTime();

  void setCreatedTime(OffsetDateTime offsetDateTime);

  String getUpdatedBy();

  void setUpdatedBy(String updatedBy);

  OffsetDateTime getUpdatedTime();

  void setUpdatedTime(OffsetDateTime updateTime);

  String getRemark();

  void setRemark(String remark);
}
