package com.wangboot.core.auth.middleware.filter;

import com.wangboot.core.auth.middleware.IFilterMiddleware;
import com.wangboot.core.auth.token.IAuthToken;
import com.wangboot.core.auth.token.TokenPair;
import org.springframework.lang.NonNull;

/**
 * 令牌类型过滤
 *
 * @author wwtg99
 */
public class AccessTokenTypeFilter implements IFilterMiddleware {
  @Override
  public boolean afterParseToken(String token, @NonNull IAuthToken authToken) {
    return authToken.getTokenType().equals(TokenPair.ACCESS_TOKEN_TYPE);
  }
}
