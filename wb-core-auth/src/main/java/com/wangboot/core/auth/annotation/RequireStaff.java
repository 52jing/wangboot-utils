package com.wangboot.core.auth.annotation;

import java.lang.annotation.*;

/**
 * 需求内部用户
 *
 * @author wwtg99
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequireStaff {
  boolean value() default true;
}
