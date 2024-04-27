package com.wangboot.model.entity.controller;

import java.lang.annotation.*;

/**
 * 控制器接口开放控制
 *
 * @author wwtg99
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EnableApi {

  ControllerApiGroup value() default ControllerApiGroup.NONE;

  ControllerApi[] includes() default {};

  ControllerApi[] excludes() default {};
}
