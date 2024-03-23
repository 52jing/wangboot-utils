package com.wangboot.core.test.auth;

import java.lang.annotation.*;

/**
 * 单元测试注入模拟用户
 *
 * @author wwtg99
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithAuthContext(factory = WithMockUserSecurityContextFactory.class)
public @interface WithMockUser {
  String id() default "1";

  String username() default "admin";

  String password() default "123456";

  boolean superuser() default false;

  boolean staff() default false;

  boolean enabled() default true;

  boolean locked() default false;

  boolean expired() default false;

  String frontendId() default "";

  String frontendName() default "";

  String frontendType() default "";

  boolean staffOnly() default false;

  boolean allowRegister() default false;

  String[] authorities() default {};
}
