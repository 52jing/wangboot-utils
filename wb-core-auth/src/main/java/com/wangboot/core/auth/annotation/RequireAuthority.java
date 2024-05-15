package com.wangboot.core.auth.annotation;

import java.lang.annotation.*;

/**
 * 需求权限
 *
 * @author wwtg99
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequireAuthority {
  String[] value();

  /** true 则要求满足全部权限 false 则要求满足任一权限 */
  boolean all() default true;
}
