package com.wangboot.core.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 刷新请求体基础类
 *
 * @author wwtg99
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenBody implements IRefreshTokenBody {
  private static final long serialVersionUID = 3L;

  private String accessToken;
  private String refreshToken;
}
