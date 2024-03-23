package com.wangboot.starter.autoconfiguration;

import com.wangboot.core.utils.password.PasswordStrategyManager;
import com.wangboot.core.web.exception.NotSupportedComponentException;
import java.util.Objects;
import lombok.Getter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableConfigurationProperties({WbProperties.class})
public class WbAutoConfiguration {

  private final WbProperties wbProperties;
  @Getter private final WbPropertiesCustomizer wbPropertiesCustomizer;

  public WbAutoConfiguration(
      WbProperties wbProperties, ObjectProvider<WbPropertiesCustomizer> configCustomizerProvider) {
    this.wbProperties = wbProperties;
    this.wbPropertiesCustomizer = configCustomizerProvider.getIfAvailable();
    if (Objects.nonNull(this.wbPropertiesCustomizer)) {
      this.wbPropertiesCustomizer.customize(wbProperties);
    }
  }

  /** 强散列哈希加密实现 */
  @Bean
  @ConditionalOnMissingBean(PasswordEncoder.class)
  public PasswordEncoder passwordEncoder() {
    try {
      return this.wbProperties.getComponents().getPasswordEncoderClass().newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new NotSupportedComponentException("PasswordEncoder");
    }
  }

  /** 密码策略管理器 */
  @Bean
  @ConditionalOnMissingBean(PasswordStrategyManager.class)
  public PasswordStrategyManager passwordStrategyManager() {
    return new PasswordStrategyManager();
  }

  /** 黑名单管理器 */
  //  @Bean
  //  @ConditionalOnMissingBean(IBlacklistHolder.class)
  //  public IBlacklistHolder blacklistHolder() {
  //    return new CacheBlacklistHolder(
  //      config.getAuthConfig().getBlacklistPrefix(), config.getAuthConfig().getBlacklistTtl());
  //  }

  /** 登录计数器 */
  //  @Bean
  //  @ConditionalOnMissingBean(ICountLimit.class)
  //  public ICountLimit loginLimit() {
  //    return new CacheCountLimit(
  //      config.getAuthConfig().getLoginFailedThreshold(),
  //      config.getAuthConfig().getLoginFailedCheckSeconds());
  //  }

  /** 加密处理器 */
  //  @Bean
  //  public CryptoProcessor cryptoProcessor(ObjectMapper objectMapper) {
  //    if (config.isApiCryptoEnabled()) {
  //      return new CryptoProcessor(
  //        objectMapper,
  //        config.getCryptoConfig().getPrivateKey().replace("\n", ""),
  //        config.getCryptoConfig().getPublicKey().replace("\n", ""));
  //    }
  //    return null;
  //  }

}
