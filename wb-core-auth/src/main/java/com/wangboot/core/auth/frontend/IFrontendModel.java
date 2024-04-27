package com.wangboot.core.auth.frontend;

import java.io.Serializable;

/**
 * 前端模型接口
 *
 * @author wwtg99
 */
public interface IFrontendModel extends Serializable {

  /** 前端ID */
  String getId();

  /** 前端名称 */
  String getName();

  /** 前端类型 */
  String getType();

  /** 是否仅内部用户使用 */
  boolean staffOnly();

  /** 是否允许注册 */
  boolean allowRegister();
}
