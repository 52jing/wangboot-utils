package com.wangboot.core.test.auth;

import java.lang.annotation.*;

/**
 * 单元测试注入管理员用户
 *
 * @author wwtg99
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithAuthContext(factory = WithAdminUserAuthContextFactory.class)
public @interface WithAdminUser {
  String id() default "1";

  String username() default "admin";

  String password() default "123456";

  String frontendId() default "";

  String frontendName() default "";

  String frontendType() default "";

  boolean staffOnly() default false;

  boolean allowRegister() default false;

  String[] authorities() default {};
}
