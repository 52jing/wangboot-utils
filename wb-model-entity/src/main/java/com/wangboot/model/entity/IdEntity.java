package com.wangboot.model.entity;

import java.io.Serializable;

/**
 * 主键 ID 实体
 *
 * @param <I> 主键类
 * @author wwtg99
 */
public interface IdEntity<I extends Serializable> extends Serializable {

  I getId();

  void setId(I id);
}
