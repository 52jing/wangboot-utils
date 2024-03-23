package com.wangboot.core.auth.middleware;

import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.model.ILoginBody;
import lombok.Generated;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 登录检查中间件
 *
 * @author wwtg99
 */
public interface ILoginMiddleware {

  /**
   * 登录认证前处理
   *
   * @param body 登录请求对象
   * @return 处理后的登录请求对象
   */
  @Generated
  @Nullable
  default ILoginBody beforeLogin(@NonNull ILoginBody body) {
    return body;
  }

  /**
   * 登录认证后处理
   *
   * @param body 登录请求对象
   * @param loginUser 认证的用户上下文
   * @return 返回 false 抛出登录失败异常
   */
  @Generated
  default boolean afterLogin(@NonNull ILoginBody body, @NonNull ILoginUser loginUser) {
    return true;
  }
}
