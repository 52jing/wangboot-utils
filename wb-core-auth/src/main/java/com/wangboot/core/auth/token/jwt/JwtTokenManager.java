package com.wangboot.core.auth.token.jwt;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.RegisteredPayload;
import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.token.AuthToken;
import com.wangboot.core.auth.token.IAuthToken;
import com.wangboot.core.auth.token.ITokenManager;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 基于JWT的令牌管理器
 *
 * @author wwtg99
 */
@AllArgsConstructor
public class JwtTokenManager implements ITokenManager {

  public static final String KEY_TYPE = "typ";
  public static final String KEY_USER_ID = "uid";
  public static final String KEY_FRONTEND_ID = "fid";

  @Getter private final String secret;
  @Getter private final String issuer;
  @Getter private final long accessExpireSecs;
  @Getter private final long refreshExpireSecs;

  @Nullable
  @Override
  public IAuthToken generateToken(
      String tokenType, @NonNull ILoginUser loginUser, long expireSecs) {
    if (!StringUtils.hasText(this.secret)
        || !StringUtils.hasText(loginUser.getUser().getUserId())
        || !StringUtils.hasText(tokenType)) {
      return null;
    }
    // 使用 JWT 生成令牌
    byte[] key = this.secret.getBytes();
    JWT jwt =
        JWT.create()
            .setKey(key)
            .setExpiresAt(new Date(System.currentTimeMillis() + expireSecs * 1000 * 60))
            .setIssuer(this.issuer)
            .setSubject(loginUser.getUser().getUsername())
            .setAudience(loginUser.getFrontend().getId())
            .setIssuedAt(new Date())
            .setPayload(KEY_TYPE, tokenType)
            .setPayload(KEY_USER_ID, loginUser.getUser().getUserId())
            .setPayload(KEY_FRONTEND_ID, loginUser.getFrontend().getId());
    String str = jwt.sign();
    return AuthToken.builder()
        .tokenType(tokenType)
        .issuer(this.issuer)
        .userId(loginUser.getUser().getUserId())
        .username(loginUser.getUser().getUsername())
        .frontendId(loginUser.getFrontend().getId())
        .string(str)
        .build();
  }

  @Nullable
  @Override
  public IAuthToken parse(String token) {
    if (!StringUtils.hasText(token)) {
      return null;
    }
    byte[] key = this.secret.getBytes();
    JWT jwt = JWT.of(token).setKey(key);
    if (jwt.validate(0)) {
      return AuthToken.builder()
          .tokenType(jwt.getPayload(KEY_TYPE).toString())
          .issuer(jwt.getPayload(RegisteredPayload.ISSUER).toString())
          .userId(jwt.getPayload(KEY_USER_ID).toString())
          .username(jwt.getPayload(RegisteredPayload.SUBJECT).toString())
          .frontendId(jwt.getPayload(KEY_FRONTEND_ID).toString())
          .string(token)
          .build();
    }
    return null;
  }
}
