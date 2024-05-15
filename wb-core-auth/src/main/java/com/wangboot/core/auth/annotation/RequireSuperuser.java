package com.wangboot.core.auth.annotation;

import java.lang.annotation.*;

/**
 * 需求超级管理员
 *
 * @author wwtg99
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequireSuperuser {
  boolean value() default true;
}
