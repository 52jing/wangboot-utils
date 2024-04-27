package com.wangboot.core.auth.middleware.login;

import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.exception.LoginFailedException;
import com.wangboot.core.auth.middleware.ILoginMiddleware;
import com.wangboot.core.auth.model.ILoginBody;
import org.springframework.lang.NonNull;

/**
 * 内部用户登录检查
 *
 * @author wwtg99
 */
public class StaffOnlyCheck implements ILoginMiddleware {
  @Override
  public boolean afterLogin(@NonNull ILoginBody body, @NonNull ILoginUser loginUser) {
    if (loginUser.getFrontend().staffOnly() && !loginUser.getUser().checkStaff()) {
      throw new LoginFailedException("require staff only but user is not staff");
    }
    return true;
  }
}
