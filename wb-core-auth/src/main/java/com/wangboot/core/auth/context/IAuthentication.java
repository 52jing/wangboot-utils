package com.wangboot.core.auth.context;

import java.io.Serializable;
import java.security.Principal;

/**
 * 认证接口
 *
 * @author wwtg99
 */
public interface IAuthentication extends Principal, Serializable {

  /** 获取凭证 */
  Object getCredentials();

  /** 获取主体 */
  Object getPrincipal();

  /** 是否已认证 */
  boolean isAuthenticated();
}
