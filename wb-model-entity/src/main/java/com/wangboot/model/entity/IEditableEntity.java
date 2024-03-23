package com.wangboot.model.entity;

/**
 * 只读实体 readOnly 方法返回 true 则表示此对象不可修改和删除。
 *
 * @author wwtg99
 */
public interface IEditableEntity {

  boolean readonly();
}
