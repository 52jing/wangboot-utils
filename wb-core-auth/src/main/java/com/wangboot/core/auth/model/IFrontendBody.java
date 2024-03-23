package com.wangboot.core.auth.model;

import java.io.Serializable;

/**
 * 前端相关模型接口
 *
 * @author wwtg99
 */
public interface IFrontendBody extends Serializable {
  /** 获取前端 ID */
  String getFrontendId();

  /** 设置前端 ID */
  void setFrontendId(String frontendId);
}
