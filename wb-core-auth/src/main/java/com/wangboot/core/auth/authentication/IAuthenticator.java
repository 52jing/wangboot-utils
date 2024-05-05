package com.wangboot.core.auth.authentication;

import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.token.IAuthToken;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 认证验证器接口<br>
 * 用于验证用户令牌。
 *
 * @author wwtg99
 */
public interface IAuthenticator {
  /**
   * 认证验证<br>
   * 认证验证成功返回 ILoginUser 实例<br>
   * 认证验证失败抛出 RuntimeException 异常或返回 null
   */
  @Nullable
  ILoginUser authenticate(@NonNull IAuthToken authToken);
}
