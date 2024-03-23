package com.wangboot.core.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Restful 权限前缀
 *
 * @author wwtg99
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RestPermissionPrefix {
  /** 权限组 */
  String group() default "";
  /** 权限名称 */
  String name();
}
