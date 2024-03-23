package com.wangboot.core.auth.middleware.logout;

import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.exception.LogoutFailedException;
import com.wangboot.core.auth.middleware.ILogoutMiddleware;
import com.wangboot.core.auth.model.ILogoutBody;
import com.wangboot.core.auth.token.IAuthToken;
import com.wangboot.core.auth.token.ITokenManager;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * 用户和访问令牌匹配验证
 *
 * @author wwtg99
 */
@RequiredArgsConstructor
public class UserTokenValidation implements ILogoutMiddleware {

  @NonNull private final ITokenManager tokenManager;

  @Override
  public ILogoutBody beforeLogout(@NonNull ILogoutBody logoutBody, @NonNull ILoginUser loginUser) {
    IAuthToken authToken = this.tokenManager.parse(logoutBody.getAccessToken());
    if (Objects.nonNull(authToken)
        && this.tokenManager.validate(authToken)
        && authToken.getUserId().equals(loginUser.getUser().getUserId())) {
      return logoutBody;
    } else {
      throw new LogoutFailedException();
    }
  }
}
