package com.wangboot.core.test.auth;

import java.lang.annotation.*;

/**
 * 单元测试注入内部用户
 *
 * @author wwtg99
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithAuthContext(factory = WithStaffUserSecurityContextFactory.class)
public @interface WithStaffUser {
  String id() default "2";

  String username() default "staff";

  String password() default "123456";

  String frontendId() default "";

  String frontendName() default "";

  String frontendType() default "";

  boolean staffOnly() default false;

  boolean allowRegister() default false;

  String[] authorities() default {};
}
