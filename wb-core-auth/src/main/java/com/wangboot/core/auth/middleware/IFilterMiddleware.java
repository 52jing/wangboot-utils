package com.wangboot.core.auth.middleware;

import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.token.IAuthToken;
import lombok.Generated;
import org.springframework.lang.NonNull;

/**
 * 请求认证中间件
 *
 * @author wwtg99
 */
public interface IFilterMiddleware {

  /**
   * 认证提取令牌前处理
   *
   * @param token 令牌
   * @return 处理后的令牌
   */
  @Generated
  default String beforeParseToken(String token) {
    return token;
  }

  /**
   * 认证提取令牌后处理
   *
   * @param token 令牌
   * @param authToken 认证令牌
   * @return 返回 false 抛出认证失败异常
   */
  @Generated
  default boolean afterParseToken(String token, @NonNull IAuthToken authToken) {
    return true;
  }

  /**
   * 认证后处理
   *
   * @param token 令牌
   * @param authToken 认证令牌
   * @param loginUser 登录用户
   * @return 返回 false 抛出认证失败异常
   */
  @Generated
  default boolean afterAuthentication(
      String token, @NonNull IAuthToken authToken, @NonNull ILoginUser loginUser) {
    return true;
  }
}
