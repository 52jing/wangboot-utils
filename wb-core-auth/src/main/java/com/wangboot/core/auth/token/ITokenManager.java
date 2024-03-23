package com.wangboot.core.auth.token;

import com.wangboot.core.auth.context.ILoginUser;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 令牌管理器接口
 *
 * @author wwtg99
 */
public interface ITokenManager {

  /**
   * 生成令牌
   *
   * @param tokenType 令牌类型
   * @param loginUser 登录用户上下文
   * @param expireSecs 失效时间（秒）
   * @return 令牌对象
   */
  @Nullable
  IAuthToken generateToken(String tokenType, @NonNull ILoginUser loginUser, long expireSecs);

  /**
   * 解析令牌
   *
   * @param token 令牌字符串
   * @return 令牌对象
   */
  @Nullable
  IAuthToken parse(String token);

  /** 获取访问令牌失效时间（秒） */
  long getAccessExpireSecs();

  /** 获取刷新令牌失效时间（秒） */
  long getRefreshExpireSecs();

  /** 签发者 */
  String getIssuer();

  @Nullable
  default IAuthToken generateAccessToken(@NonNull ILoginUser loginUser) {
    return generateToken(TokenPair.ACCESS_TOKEN_TYPE, loginUser, getAccessExpireSecs());
  }

  @Nullable
  default IAuthToken generateRefreshToken(@NonNull ILoginUser loginUser) {
    return generateToken(TokenPair.REFRESH_TOKEN_TYPE, loginUser, getRefreshExpireSecs());
  }

  /**
   * 验证令牌是否有效
   *
   * @param authToken 令牌
   * @return boolean
   */
  default boolean validate(@Nullable IAuthToken authToken) {
    if (Objects.isNull(authToken)) {
      return false;
    }
    return authToken.getIssuer().equals(this.getIssuer());
  }

  /**
   * 生成令牌对
   *
   * @param loginUser 登录用户上下文
   * @return 令牌对
   */
  default TokenPair generateTokenPair(@NonNull ILoginUser loginUser) {
    return new TokenPair(generateAccessToken(loginUser), generateRefreshToken(loginUser));
  }
}
