package com.wangboot.core.test.auth;

import java.lang.annotation.*;

/**
 * 单元测试认证上下文注解
 *
 * @author wwtg99
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WithAuthContext {

  /** 认证上下文工厂类型 */
  Class<? extends WithAuthContextFactory<? extends Annotation>> factory();
}
