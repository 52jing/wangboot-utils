package com.wangboot.core.auth.token;

import java.io.Serializable;

/**
 * 访问令牌接口
 *
 * @author wwtg99
 */
public interface IAuthToken extends Serializable {

  /** 获取令牌字符串 */
  String getString();

  /** 获取令牌类型 */
  String getTokenType();

  /** 获取用户ID */
  String getUserId();

  /** 获取用户名 */
  String getUsername();

  /** 获取前端ID */
  String getFrontendId();

  /** 获取签发者 */
  String getIssuer();
}
