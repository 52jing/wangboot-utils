package com.wangboot.core.auth.middleware;

import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.model.IRefreshTokenBody;
import lombok.Generated;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 刷新令牌检查中间件
 *
 * @author wwtg99
 */
public interface IRefreshTokenMiddleware {

  @Generated
  @Nullable
  default IRefreshTokenBody beforeRefreshToken(@NonNull IRefreshTokenBody refreshTokenBody) {
    return refreshTokenBody;
  }

  @Generated
  default boolean afterRefreshToken(
      @NonNull IRefreshTokenBody refreshTokenBody, @NonNull ILoginUser loginUser) {
    return true;
  }
}
