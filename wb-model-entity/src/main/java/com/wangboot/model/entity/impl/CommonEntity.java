package com.wangboot.model.entity.impl;

import com.wangboot.model.entity.ICommonEntity;
import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.Data;

/**
 * 普通实体抽象类，支持创建人、创建时间、更新人、更新时间、备注
 *
 * @author wwtg99
 */
@Data
public abstract class CommonEntity implements ICommonEntity, Serializable {
  private String remark = "";
  private String createdBy;
  private OffsetDateTime createdTime;
  private String updatedBy;
  private OffsetDateTime updatedTime;
}
