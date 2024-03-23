package com.wangboot.core.web.param;

import org.springframework.lang.Nullable;

/**
 * 动态参数配置接口
 *
 * @author wwtg99
 */
public interface IParamConfig {
  @Nullable
  String getParamConfig(@Nullable String key);
}
