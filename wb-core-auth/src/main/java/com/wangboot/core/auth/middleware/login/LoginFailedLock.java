package com.wangboot.core.auth.middleware.login;

import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.exception.LoginLockedException;
import com.wangboot.core.auth.middleware.ILoginMiddleware;
import com.wangboot.core.auth.model.ILoginBody;
import com.wangboot.core.auth.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * 登录锁定检查
 *
 * @author wwtg99
 */
@RequiredArgsConstructor
public class LoginFailedLock implements ILoginMiddleware {

  private final IUserService userService;

  @Override
  public boolean afterLogin(@NonNull ILoginBody body, @NonNull ILoginUser loginUser) {
    if (this.userService.isUserLocked(loginUser.getUser().getUserId())) {
      throw new LoginLockedException(loginUser.getUser().getUsername());
    }
    return true;
  }
}
