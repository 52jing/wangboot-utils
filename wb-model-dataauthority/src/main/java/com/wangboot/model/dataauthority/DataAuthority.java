package com.wangboot.model.dataauthority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限注解<br>
 * 类型为 DATA_SCOPE，USER_ID_FIELD, CUSTOM_FIELD 须提供 field 指定数据权限筛选字段
 *
 * @author wwtg99
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataAuthority {

  /** 数据权限类型 */
  DataAuthorityType value() default DataAuthorityType.NONE;
  /** 数据权限字段 */
  String field() default "";
}
