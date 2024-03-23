package com.wangboot.core.test.auth;

import com.wangboot.core.auth.context.IAuthContext;
import java.lang.annotation.Annotation;
import org.springframework.lang.Nullable;

/**
 * 认证上下文工厂接口
 *
 * @param <A> 注解
 * @author wwtg99
 */
public interface WithAuthContextFactory<A extends Annotation> {

  /**
   * 创建认证上下文
   *
   * @param annotation 注解
   * @return 认证上下文
   */
  @Nullable
  IAuthContext createContext(@Nullable A annotation);
}
