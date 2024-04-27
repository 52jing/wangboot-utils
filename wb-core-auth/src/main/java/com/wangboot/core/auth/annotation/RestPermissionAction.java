package com.wangboot.core.auth.annotation;

import java.lang.annotation.*;

/**
 * Restful 权限操作
 *
 * @author wwtg99
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RestPermissionAction {
  String value();
}
