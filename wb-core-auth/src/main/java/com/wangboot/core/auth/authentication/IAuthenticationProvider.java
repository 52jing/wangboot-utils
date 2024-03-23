package com.wangboot.core.auth.authentication;

import com.wangboot.core.auth.model.ILoginBody;
import com.wangboot.core.auth.user.IUserModel;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 认证提供者
 *
 * @author wwtg99
 */
public interface IAuthenticationProvider {

  /**
   * 登录认证<br>
   * 认证成功返回 IUserModel 实例<br>
   * 认证失败抛出 RuntimeException 异常或返回 null
   */
  @Nullable
  IUserModel authenticate(@NonNull ILoginBody model);
}
