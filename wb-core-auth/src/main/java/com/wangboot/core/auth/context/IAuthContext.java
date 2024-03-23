package com.wangboot.core.auth.context;

import java.io.Serializable;
import org.springframework.lang.Nullable;

/**
 * 认证上下文
 *
 * @author wwtg99
 */
public interface IAuthContext extends Serializable {

  @Nullable
  IAuthentication getAuthentication();

  void setAuthentication(@Nullable IAuthentication authentication);
}
