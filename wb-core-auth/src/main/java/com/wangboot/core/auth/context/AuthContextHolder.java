package com.wangboot.core.auth.context;

import org.springframework.lang.Nullable;

/**
 * 认证上下文持有者
 *
 * @author wwtg99
 */
public class AuthContextHolder {
  @Nullable private static IAuthContext context;

  private AuthContextHolder() {}

  @Nullable
  public static IAuthContext getContext() {
    return AuthContextHolder.context;
  }

  public static void setContext(@Nullable IAuthContext context) {
    AuthContextHolder.context = context;
  }

  public static void clearContext() {
    AuthContextHolder.context = null;
  }

  public static void init() {
    AuthContextHolder.context = new RequestAuthContext();
  }

  static {
    init();
  }
}
