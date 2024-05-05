package com.wangboot.core.web.crypto;

import java.lang.annotation.*;

/**
 * 排除加密注解
 *
 * @author wwtg99
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface ExcludeEncryption {}
