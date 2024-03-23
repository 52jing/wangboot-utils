package com.wangboot.model.entity;

import java.io.Serializable;

/**
 * 树状实体接口，支持父节点ID，排序
 *
 * @param <I> 主键类
 * @author wwtg99
 */
public interface ITreeEntity<I extends Serializable> extends IdEntity<I> {

  I getParentId();

  void setParentId(I parentId);

  Integer getSort();

  void setSort(Integer sort);

  boolean hasChildren();
}
