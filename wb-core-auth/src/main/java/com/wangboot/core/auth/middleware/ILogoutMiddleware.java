package com.wangboot.core.auth.middleware;

import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.model.ILogoutBody;
import lombok.Generated;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 登出检查中间件
 *
 * @author wwtg99
 */
public interface ILogoutMiddleware {

  @Generated
  @Nullable
  default ILogoutBody beforeLogout(@NonNull ILogoutBody logoutBody, @NonNull ILoginUser loginUser) {
    return logoutBody;
  }

  @Generated
  default boolean afterLogout(@NonNull ILogoutBody logoutBody, @NonNull ILoginUser loginUser) {
    return true;
  }
}
