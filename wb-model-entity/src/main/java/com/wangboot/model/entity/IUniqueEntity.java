package com.wangboot.model.entity;

/**
 * 支持唯一性实体
 *
 * @author wwtg99
 */
public interface IUniqueEntity {

  /** * 获取唯一字段组合，第一层数组为组合，第二层数组为字段，字段应使用驼峰写法。 */
  String[][] getUniqueTogetherFields();
}
