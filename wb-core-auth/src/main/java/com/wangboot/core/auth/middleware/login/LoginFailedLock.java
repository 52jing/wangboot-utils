package com.wangboot.core.auth.middleware.login;

import com.wangboot.core.auth.exception.LoginLockedException;
import com.wangboot.core.auth.middleware.ILoginMiddleware;
import com.wangboot.core.auth.model.ILoginBody;
import com.wangboot.core.reliability.loginlock.IFailedLock;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 登录锁定检查
 *
 * @author wwtg99
 */
@RequiredArgsConstructor
public class LoginFailedLock implements ILoginMiddleware {

  @Nullable private final IFailedLock failedLock;

  @Override
  public ILoginBody beforeLogin(@NonNull ILoginBody body) {
    if (Objects.nonNull(this.failedLock) && this.failedLock.isLocked(body.getUsername())) {
      throw new LoginLockedException(body.getUsername());
    }
    return body;
  }
}
