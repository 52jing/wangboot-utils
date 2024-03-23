package com.wangboot.core.auth.authentication.authenticator;

import com.wangboot.core.auth.authentication.IAuthenticator;
import com.wangboot.core.auth.authorization.IAuthorizer;
import com.wangboot.core.auth.authorization.IAuthorizerService;
import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.context.LoginUser;
import com.wangboot.core.auth.frontend.IFrontendModel;
import com.wangboot.core.auth.frontend.IFrontendService;
import com.wangboot.core.auth.token.IAuthToken;
import com.wangboot.core.auth.user.IUserModel;
import com.wangboot.core.auth.user.IUserService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 基于令牌认证用户
 *
 * @author wwtg99
 */
@RequiredArgsConstructor
public class TokenAuthenticator implements IAuthenticator {

  private final IUserService userService;

  private final IFrontendService frontendService;

  private final IAuthorizerService authorizerService;

  @Nullable
  @Override
  public ILoginUser authenticate(@NonNull IAuthToken authToken) {
    IUserModel userModel = this.userService.getUserModelById(authToken.getUserId());
    if (Objects.isNull(userModel)) {
      return null;
    }
    IFrontendModel frontendModel =
        this.frontendService.getFrontendModelById(authToken.getFrontendId());
    if (Objects.isNull(frontendModel)) {
      return null;
    }
    IAuthorizer authorizer = this.authorizerService.getAuthorizer(userModel);
    return new LoginUser(userModel, frontendModel, authorizer);
  }
}
