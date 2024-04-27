package com.wangboot.core.auth.annotation;

import java.lang.annotation.*;

/**
 * Restful 权限前缀
 *
 * @author wwtg99
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestPermissionPrefix {
  /** 权限组 */
  String group() default "";
  /** 权限名称 */
  String name();
}
