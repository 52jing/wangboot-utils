package com.wangboot.core.auth.context;

import org.springframework.lang.Nullable;

/**
 * 本地认证上下文
 *
 * @author wwtg99
 */
public class LocalAuthContext implements IAuthContext {

  private final ThreadLocal<IAuthentication> local;

  public LocalAuthContext(IAuthentication authentication) {
    this();
    this.local.set(authentication);
  }

  public LocalAuthContext() {
    this.local = new ThreadLocal<>();
  }

  @Override
  @Nullable
  public IAuthentication getAuthentication() {
    return this.local.get();
  }

  @Override
  public void setAuthentication(@Nullable IAuthentication authentication) {
    this.local.set(authentication);
  }
}
