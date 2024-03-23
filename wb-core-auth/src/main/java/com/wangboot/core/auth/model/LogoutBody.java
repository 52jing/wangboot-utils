package com.wangboot.core.auth.model;

import lombok.*;

/**
 * 登出请求体基础类
 *
 * @author wwtg99
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class LogoutBody implements ILogoutBody {

  private static final long serialVersionUID = 2L;

  private String accessToken;
  private String refreshToken;
}
