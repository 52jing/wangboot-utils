package com.wangboot.core.auth.authorization.authorizer;

import com.wangboot.core.auth.authorization.IAuthorizationResource;
import com.wangboot.core.auth.authorization.IAuthorizer;
import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 允许或拒绝所有授权验证者
 *
 * @author wwtg99
 */
@AllArgsConstructor
public class GrantAllAuthorizer implements IAuthorizer {

  private final boolean grant;

  @Override
  @NonNull
  public Collection<? extends IAuthorizationResource> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public boolean authorize(@Nullable IAuthorizationResource resource) {
    return this.grant;
  }

  @Override
  public boolean authorizeAny(IAuthorizationResource... resources) {
    return this.grant;
  }

  @Override
  public boolean authorizeAll(IAuthorizationResource... resources) {
    return this.grant;
  }
}
