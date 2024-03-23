package com.wangboot.model.dataauthority;

/**
 * 数据权限类型<br>
 * NONE：不限制，返回所有数据<br>
 * DATA_SCOPE：基于 IDataScopeModel 限制，获取用户授权的 DataScope 内的所有数据<br>
 * USER_ID_FIELD：仅获取创建用户是自己的数据<br>
 *
 * @author wwtg99
 */
public enum DataAuthorityType {
  NONE,
  DATA_SCOPE,
  USER_ID_FIELD
}
