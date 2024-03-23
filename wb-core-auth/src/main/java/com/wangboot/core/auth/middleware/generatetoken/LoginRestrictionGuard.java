package com.wangboot.core.auth.middleware.generatetoken;

import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.middleware.IGenerateTokenMiddleware;
import com.wangboot.core.auth.security.LoginRestriction;
import com.wangboot.core.auth.security.LoginRestrictionStrategy;
import com.wangboot.core.auth.token.TokenPair;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * 登录限制记录
 *
 * @author wwtg99
 */
@RequiredArgsConstructor
public class LoginRestrictionGuard implements IGenerateTokenMiddleware {

  @NonNull private final LoginRestrictionStrategy strategy;

  @NonNull private final LoginRestriction loginRestriction;

  @Override
  public boolean afterTokenGeneration(@NonNull ILoginUser loginUser, @NonNull TokenPair tokenPair) {
    if (Objects.nonNull(tokenPair.getAccessToken())) {
      this.loginRestriction.acquireGuard(
          this.strategy, loginUser, tokenPair.getAccessToken().getString());
    }
    return true;
  }
}
