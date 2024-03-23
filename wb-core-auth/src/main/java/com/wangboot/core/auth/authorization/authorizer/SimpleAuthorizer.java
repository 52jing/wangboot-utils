package com.wangboot.core.auth.authorization.authorizer;

import com.wangboot.core.auth.authorization.IAuthorizationResource;
import com.wangboot.core.auth.authorization.IAuthorizer;
import com.wangboot.core.auth.user.IUserModel;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 简单授权验证者
 *
 * @author wwtg99
 */
@AllArgsConstructor
public class SimpleAuthorizer implements IAuthorizer {

  @Getter @NonNull private final IUserModel userModel;

  @Getter @NonNull private final Collection<? extends IAuthorizationResource> authorities;

  @Override
  public boolean authorize(@Nullable IAuthorizationResource resource) {
    if (Objects.isNull(resource)) {
      return false;
    }
    return this.getAuthorities().contains(resource);
  }

  @Override
  public boolean authorizeAny(IAuthorizationResource... resources) {
    if (Objects.isNull(resources) || resources.length == 0) {
      return false;
    }
    return Arrays.stream(resources).anyMatch(this.getAuthorities()::contains);
  }

  @Override
  public boolean authorizeAll(IAuthorizationResource... resources) {
    if (Objects.isNull(resources) || resources.length == 0) {
      return false;
    }
    return Arrays.stream(resources).allMatch(this.getAuthorities()::contains);
  }
}
