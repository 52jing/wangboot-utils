package com.wangboot.model.dataauthority.datascope;

import com.wangboot.core.auth.authorization.IAuthorizationResource;

/**
 * 数据权限范围模型接口
 *
 * @author wwtg99
 */
public interface IDataScopeModel extends IAuthorizationResource {

  /** 获取数据权限名称 */
  String getDataScopeName();

  /** 获取数据权限名称 */
  @Override
  default String getResourceName() {
    return this.getDataScopeName();
  }
}
