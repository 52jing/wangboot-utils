package com.wangboot.core.auth.middleware.refreshtoken;

import com.wangboot.core.auth.middleware.IRefreshTokenMiddleware;
import com.wangboot.core.auth.model.IRefreshTokenBody;
import com.wangboot.core.auth.token.IAuthToken;
import com.wangboot.core.auth.token.ITokenManager;
import com.wangboot.core.auth.token.TokenPair;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * 刷新令牌类型检查
 *
 * @author wwtg99
 */
@RequiredArgsConstructor
public class RefreshTokenTypeCheck implements IRefreshTokenMiddleware {

  @NonNull private final ITokenManager tokenManager;

  @Override
  public IRefreshTokenBody beforeRefreshToken(@NonNull IRefreshTokenBody refreshTokenBody) {
    if (!StringUtils.hasText(refreshTokenBody.getRefreshToken())) {
      return null;
    }
    IAuthToken authToken = this.tokenManager.parse(refreshTokenBody.getRefreshToken());
    if (Objects.isNull(authToken)) {
      return null;
    }
    if (!TokenPair.REFRESH_TOKEN_TYPE.equals(authToken.getTokenType())) {
      return null;
    }
    return refreshTokenBody;
  }
}
