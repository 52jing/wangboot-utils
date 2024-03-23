package com.wangboot.core.auth.middleware.filter;

import com.wangboot.core.auth.middleware.IFilterMiddleware;
import com.wangboot.core.reliability.blacklist.IBlacklistHolder;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 令牌黑名单检查
 *
 * @author wwtg99
 */
@RequiredArgsConstructor
public class BlacklistFilter implements IFilterMiddleware {

  @Nullable private final IBlacklistHolder blacklistHolder;

  @Override
  public String beforeParseToken(String token) {
    if (Objects.nonNull(this.blacklistHolder) && !this.blacklistHolder.inBlacklist(token)) {
      return token;
    }
    return "";
  }
}
