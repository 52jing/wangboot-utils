package com.wangboot.starter.autoconfiguration;

import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangboot.core.auth.authentication.IAuthenticator;
import com.wangboot.core.auth.authentication.authenticator.TokenAuthenticator;
import com.wangboot.core.auth.authorization.IAuthorizerService;
import com.wangboot.core.auth.frontend.IFrontendService;
import com.wangboot.core.auth.security.LoginRestriction;
import com.wangboot.core.auth.token.ITokenManager;
import com.wangboot.core.auth.token.jwt.JwtTokenManager;
import com.wangboot.core.auth.user.IUserService;
import com.wangboot.core.captcha.CaptchaProcessorHolder;
import com.wangboot.core.captcha.ICaptchaProcessor;
import com.wangboot.core.captcha.ICaptchaRepository;
import com.wangboot.core.captcha.image.ImageCaptchaConfig;
import com.wangboot.core.captcha.image.ImageCaptchaProcessor;
import com.wangboot.core.captcha.repository.CacheCaptchaRepository;
import com.wangboot.core.captcha.repository.MapCaptchaRepository;
import com.wangboot.core.crypto.CryptoProcessor;
import com.wangboot.core.reliability.blacklist.CacheBlacklistHolder;
import com.wangboot.core.reliability.blacklist.IBlacklistHolder;
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

  /** 令牌管理器 */
  @Bean
  @ConditionalOnMissingBean(ITokenManager.class)
  public ITokenManager tokenManager() {
    return new JwtTokenManager(
        this.wbProperties.getSecret(),
        this.wbProperties.getNamespace(),
        this.wbProperties.getAuth().getExpires(),
        this.wbProperties.getAuth().getRefreshExpires());
  }

  /** 认证验证器 */
  @Bean
  @ConditionalOnMissingBean(IAuthenticator.class)
  public IAuthenticator authenticator(
      IUserService userService,
      IFrontendService frontendService,
      IAuthorizerService authorizerService) {
    return new TokenAuthenticator(userService, frontendService, authorizerService);
  }

  /** 黑名单管理器 */
  @Bean
  @ConditionalOnMissingBean(IBlacklistHolder.class)
  public IBlacklistHolder blacklistHolder() {
    return new CacheBlacklistHolder(
        this.wbProperties.getAuth().getBlacklistPrefix(),
        this.wbProperties.getAuth().getBlacklistTtl());
  }

  /** 登录限制器 */
  @Bean
  public LoginRestriction loginRestriction() {
    return new LoginRestriction();
  }

  /** 加解密处理器 */
  @Bean
  public CryptoProcessor cryptoProcessor(ObjectMapper objectMapper) {
    if (wbProperties.getCrypto().isEnabled()) {
      return new CryptoProcessor(
          objectMapper,
          this.wbProperties.getCrypto().getPrivateKey().replace("\n", ""),
          this.wbProperties.getCrypto().getPublicKey().replace("\n", ""));
    } else {
      return null;
    }
  }

  /** 验证码存储器 */
  @Bean
  @ConditionalOnMissingBean(ICaptchaRepository.class)
  public ICaptchaRepository captchaRepository() {
    switch (wbProperties.getCaptcha().getRepositoryType()) {
      case "cache":
        return new CacheCaptchaRepository(wbProperties.getCaptcha().getTtl() * 1000);
      case "map":
      default:
        return new MapCaptchaRepository();
    }
  }

  /** 图片验证码处理器 */
  @Bean
  public ICaptchaProcessor imageCaptchaProcessor(ICaptchaRepository captchaRepository) {
    if (wbProperties.getCaptcha().getImage().isEnabled()) {
      ImageCaptchaConfig config =
          BeanUtil.copyProperties(wbProperties.getCaptcha().getImage(), ImageCaptchaConfig.class);
      CodeGenerator codeGenerator = wbProperties.getComponents().getCaptchaCodeGenerator();
      ImageCaptchaProcessor processor =
          new ImageCaptchaProcessor(config, captchaRepository, codeGenerator);
      CaptchaProcessorHolder.addProcessor("image", processor);
      return processor;
    } else {
      return null;
    }
  }
}
