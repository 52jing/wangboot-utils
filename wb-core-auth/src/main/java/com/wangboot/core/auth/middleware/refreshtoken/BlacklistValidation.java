package com.wangboot.core.auth.middleware.refreshtoken;

import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.middleware.IRefreshTokenMiddleware;
import com.wangboot.core.auth.model.IRefreshTokenBody;
import com.wangboot.core.reliability.blacklist.IBlacklistHolder;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 令牌黑名单检查
 *
 * @author wwtg99
 */
@RequiredArgsConstructor
public class BlacklistValidation implements IRefreshTokenMiddleware {

  @Nullable private final IBlacklistHolder blacklistHolder;
  private final long expires;

  @Override
  public IRefreshTokenBody beforeRefreshToken(@NonNull IRefreshTokenBody refreshTokenBody) {
    if (Objects.isNull(this.blacklistHolder)
        || !this.blacklistHolder.inBlacklist(refreshTokenBody.getRefreshToken())) {
      return refreshTokenBody;
    }
    return null;
  }

  @Override
  public boolean afterRefreshToken(
      @NonNull IRefreshTokenBody refreshTokenBody, @NonNull ILoginUser loginUser) {
    // 旧令牌加入黑名单
    if (Objects.nonNull(this.blacklistHolder)) {
      if (StringUtils.hasText(refreshTokenBody.getAccessToken())) {
        this.blacklistHolder.addBlacklist(refreshTokenBody.getAccessToken(), this.expires);
      }
      this.blacklistHolder.addBlacklist(refreshTokenBody.getRefreshToken(), this.expires);
    }
    return true;
  }
}
