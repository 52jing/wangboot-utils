package com.wangboot.core.test.auth;

import com.wangboot.core.auth.authorization.authorizer.SimpleAuthorizer;
import com.wangboot.core.auth.authorization.resource.ApiResource;
import com.wangboot.core.auth.context.IAuthContext;
import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.context.LocalAuthContext;
import com.wangboot.core.auth.context.LoginUser;
import com.wangboot.core.auth.frontend.impl.MockFrontend;
import com.wangboot.core.auth.user.impl.MockUser;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Generated;
import org.springframework.lang.Nullable;

/**
 * 管理员用户认证上下文工厂
 *
 * @author wwtg99
 */
@Generated
public class WithAdminUserAuthContextFactory implements WithAuthContextFactory<WithAdminUser> {

  @Override
  @Nullable
  public IAuthContext createContext(@Nullable WithAdminUser annotation) {
    if (Objects.isNull(annotation)) {
      return null;
    }
    MockUser user =
        new MockUser(
            annotation.id(),
            annotation.username(),
            annotation.password(),
            true,
            true,
            true,
            false,
            null);
    MockFrontend frontend =
        new MockFrontend(
            annotation.frontendId(),
            annotation.frontendName(),
            annotation.frontendType(),
            annotation.staffOnly(),
            annotation.allowRegister());
    List<ApiResource> authorities =
        Arrays.stream(annotation.authorities())
            .map(ApiResource::of)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    SimpleAuthorizer authorizer = new SimpleAuthorizer(user, authorities);
    ILoginUser loginUser = new LoginUser(user, frontend, authorizer);
    return new LocalAuthContext(loginUser);
  }
}
