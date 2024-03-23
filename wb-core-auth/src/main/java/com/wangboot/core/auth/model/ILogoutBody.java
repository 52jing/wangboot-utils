package com.wangboot.core.auth.model;

import java.io.Serializable;

/**
 * 登出数据模型接口
 *
 * @author wwtg99
 */
public interface ILogoutBody extends Serializable {
  /** 获取访问令牌 */
  String getAccessToken();
  /** 设置访问令牌 */
  void setAccessToken(String accessToken);

  /** 获取刷新令牌 */
  String getRefreshToken();
  /** 设置刷新令牌 */
  void setRefreshToken(String refreshToken);
}
