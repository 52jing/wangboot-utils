package com.wangboot.core.auth.middleware.logout;

import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.middleware.ILogoutMiddleware;
import com.wangboot.core.auth.model.ILogoutBody;
import com.wangboot.core.reliability.blacklist.IBlacklistHolder;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 登出黑名单处理
 *
 * @author wwtg99
 */
@RequiredArgsConstructor
public class BlacklistHandler implements ILogoutMiddleware {

  @Nullable private final IBlacklistHolder blacklistHolder;

  @Override
  public boolean afterLogout(@NonNull ILogoutBody logoutBody, @NonNull ILoginUser loginUser) {
    // 添加黑名单
    if (Objects.nonNull(this.blacklistHolder)) {
      this.blacklistHolder.addBlacklist(logoutBody.getAccessToken());
      this.blacklistHolder.addBlacklist(logoutBody.getRefreshToken());
    }
    return true;
  }
}
