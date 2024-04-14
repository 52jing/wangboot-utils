package com.wangboot.model.entity.event;

import java.lang.annotation.*;

/**
 * 启用操作日志
 *
 * @author wwtg99
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EnableOperationLog {
  // 资源名称，默认类名
  String value() default "";
}
