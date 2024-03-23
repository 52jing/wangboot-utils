package com.wangboot.core.auth.middleware;

import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.token.TokenPair;
import lombok.Generated;
import org.springframework.lang.NonNull;

/**
 * 生成令牌对中间件
 *
 * @author wwtg99
 */
public interface IGenerateTokenMiddleware {
  /**
   * 令牌生成后处理
   *
   * @param loginUser 认证的用户上下文
   * @param tokenPair 令牌对
   * @return 返回 false 抛出登录失败异常
   */
  @Generated
  default boolean afterTokenGeneration(
      @NonNull ILoginUser loginUser, @NonNull TokenPair tokenPair) {
    return true;
  }
}
