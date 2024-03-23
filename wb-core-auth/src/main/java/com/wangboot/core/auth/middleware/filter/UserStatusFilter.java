package com.wangboot.core.auth.middleware.filter;

import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.middleware.IFilterMiddleware;
import com.wangboot.core.auth.token.IAuthToken;
import org.springframework.lang.NonNull;

/**
 * 用户状态过滤
 *
 * @author wwtg99
 */
public class UserStatusFilter implements IFilterMiddleware {

  @Override
  public boolean afterAuthentication(
      String token, @NonNull IAuthToken authToken, @NonNull ILoginUser loginUser) {
    if (!loginUser.isLogin()) {
      return false;
    }
    // 判断用户状态
    return loginUser.isUserValid();
  }
}
