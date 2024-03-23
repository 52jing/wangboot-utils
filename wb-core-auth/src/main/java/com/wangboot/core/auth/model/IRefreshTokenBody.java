package com.wangboot.core.auth.model;

import java.io.Serializable;

public interface IRefreshTokenBody extends Serializable {
  /** 获取访问令牌 */
  String getAccessToken();
  /** 设置访问令牌 */
  void setAccessToken(String accessToken);

  /** 获取刷新令牌 */
  String getRefreshToken();
  /** 设置刷新令牌 */
  void setRefreshToken(String refreshToken);
}
