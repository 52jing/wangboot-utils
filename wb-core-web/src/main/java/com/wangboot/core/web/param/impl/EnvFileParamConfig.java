package com.wangboot.core.web.param.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.wangboot.core.web.param.IParamConfig;
import java.util.Objects;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 本地环境配置实现
 *
 * @author wwtg99
 */
public class EnvFileParamConfig implements IParamConfig {

  public static final String SEP = ".";

  private final Environment environment;

  private final String prefix;

  public EnvFileParamConfig(@Nullable Environment environment, String prefix) {
    if (Objects.nonNull(environment)) {
      this.environment = environment;
    } else {
      this.environment = SpringUtil.getBean(Environment.class);
    }
    this.prefix = StringUtils.hasText(prefix) ? prefix : "";
  }

  public EnvFileParamConfig(String prefix) {
    this(null, prefix);
  }

  public EnvFileParamConfig() {
    this(null, "");
  }

  @Override
  public String getParamConfig(String key) {
    if (!StringUtils.hasText(key)) {
      return null;
    }
    return this.environment.getProperty(getKey(key));
  }

  @NonNull
  private String getKey(@NonNull String key) {
    if (StringUtils.hasText(this.prefix)) {
      return this.prefix + SEP + key;
    } else {
      return key;
    }
  }
}
