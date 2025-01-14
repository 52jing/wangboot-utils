package com.wangboot.core.test.auth;

import com.wangboot.core.auth.authorization.authorizer.SimpleAuthorizer;
import com.wangboot.core.auth.authorization.resource.ApiResource;
import com.wangboot.core.auth.context.IAuthContext;
import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.context.LocalAuthContext;
import com.wangboot.core.auth.context.LoginUser;
import com.wangboot.core.auth.frontend.impl.MockFrontend;
import com.wangboot.core.auth.user.impl.MockUser;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Generated;
import org.springframework.lang.Nullable;

/**
 * 任意模拟用户认证上下文工厂
 *
 * @author wwtg99
 */
@Generated
public class WithMockUserSecurityContextFactory implements WithAuthContextFactory<WithMockUser> {

  @Override
  public IAuthContext createContext(@Nullable WithMockUser annotation) {
    if (Objects.isNull(annotation)) {
      return null;
    }
    MockUser user =
        new MockUser(
            annotation.id(),
            annotation.username(),
            annotation.password(),
            annotation.superuser(),
            annotation.staff(),
            annotation.enabled(),
            annotation.locked(),
            annotation.expired()
                ? OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
                : null);
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
