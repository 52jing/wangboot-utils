package com.wangboot.model.dataauthority;

import com.wangboot.model.dataauthority.factory.IDataAuthorizerFactory;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限注解<br>
 *
 * @author wwtg99
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataAuthority {

  /** 数据权限字段 */
  String field();

  /** 数据权限工厂类 */
  Class<? extends IDataAuthorizerFactory> factory();
}
