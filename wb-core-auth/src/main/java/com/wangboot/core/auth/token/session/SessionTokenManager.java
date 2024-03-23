package com.wangboot.core.auth.token.session;

import cn.hutool.core.lang.UUID;
import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.token.AuthToken;
import com.wangboot.core.auth.token.IAuthToken;
import com.wangboot.core.auth.token.ITokenManager;
import com.wangboot.core.cache.CacheUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 基于缓存的令牌管理器
 *
 * @author wwtg99
 */
@AllArgsConstructor
public class SessionTokenManager implements ITokenManager {

  private static final String CACHE_PREFIX = "SESSION_TOKEN_";

  @Getter private final String issuer;
  @Getter private final long accessExpireSecs;
  @Getter private final long refreshExpireSecs;

  @Override
  @Nullable
  public IAuthToken generateToken(
      String tokenType, @NonNull ILoginUser loginUser, long expireSecs) {
    if (!StringUtils.hasText(loginUser.getUser().getUserId()) || !StringUtils.hasText(tokenType)) {
      return null;
    }
    UUID uuid = UUID.randomUUID();
    String str = uuid.toString(true);
    IAuthToken token =
        AuthToken.builder()
            .tokenType(tokenType)
            .issuer(this.issuer)
            .userId(loginUser.getUser().getUserId())
            .username(loginUser.getUser().getUsername())
            .frontendId(loginUser.getFrontend().getId())
            .string(str)
            .build();
    CacheUtil.put(getCacheKey(str), token, expireSecs * 1000);
    return token;
  }

  @Nullable
  @Override
  public IAuthToken parse(String token) {
    if (!StringUtils.hasText(token)) {
      return null;
    }
    return CacheUtil.get(getCacheKey(token), AuthToken.class);
  }

  public static String getCacheKey(String key) {
    return CACHE_PREFIX + key;
  }
}
