package com.wangboot.model.entity.impl;

import com.wangboot.model.entity.ITreeEntity;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 普通实体抽象类，支持创建人、创建时间、更新人、更新时间、备注
 *
 * @author wwtg99
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class CommonTreeEntity<I extends Serializable> extends CommonEntity
    implements ITreeEntity<I> {
  private I parentId;
  private Integer sort = 0;
}
