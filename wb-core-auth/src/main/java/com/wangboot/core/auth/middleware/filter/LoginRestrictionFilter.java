package com.wangboot.core.auth.middleware.filter;

import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.exception.KickedOutException;
import com.wangboot.core.auth.middleware.IFilterMiddleware;
import com.wangboot.core.auth.security.LoginRestriction;
import com.wangboot.core.auth.security.LoginRestrictionStrategy;
import com.wangboot.core.auth.token.IAuthToken;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 登录限制过滤
 *
 * @author wwtg99
 */
@RequiredArgsConstructor
public class LoginRestrictionFilter implements IFilterMiddleware {

  @Nullable private final LoginRestrictionStrategy strategy;

  @Nullable private final LoginRestriction loginRestriction;

  @Override
  public boolean afterAuthentication(
      String token, @NonNull IAuthToken authToken, @NonNull ILoginUser loginUser) {
    LoginRestrictionStrategy s =
        Objects.nonNull(this.strategy) ? this.strategy : LoginRestrictionStrategy.NO_RESTRICTION;
    if (Objects.nonNull(this.loginRestriction)
        && !this.loginRestriction.passGuard(s, loginUser, token)) {
      throw new KickedOutException();
    }
    return true;
  }
}
