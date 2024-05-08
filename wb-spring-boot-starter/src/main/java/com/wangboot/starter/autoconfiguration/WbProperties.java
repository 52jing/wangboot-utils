package com.wangboot.starter.autoconfiguration;

import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import com.wangboot.core.auth.security.LoginRestrictionStrategy;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;

/**
 * 应用配置属性
 *
 * @author wwtg99
 */
@Data
@Validated
@ConfigurationProperties(prefix = WbProperties.PREFIX, ignoreInvalidFields = true)
public class WbProperties {
  public static final String PREFIX = "app";

  /** 命名空间 */
  private String namespace = "";

  /** 应用密钥 */
  @NotBlank private String secret;

  /** 调试开关 */
  private boolean debug = false;

  /** 维护中标记 */
  private boolean inMaintenance = false;

  /** 维护通知 */
  private String maintenanceNotice = "";

  /** 时区 */
  private String timezone = "UTC";

  /** 日期格式 */
  private String dateFormat = "yyyy-MM-dd HH:mm:ss";

  /** 上传大小限制（B），0 不限制 */
  @Min(0)
  private long uploadLimits = 0;

  /** 上传图片生成的缩略图大小 */
  @Min(1)
  private int uploadImageThumbSize = 200;

  /** 认证配置 */
  @NestedConfigurationProperty @Valid private AuthConfig auth = new AuthConfig();

  /** 验证码配置 */
  @NestedConfigurationProperty @Valid private CaptchaConfig captcha = new CaptchaConfig();

  /** 加密配置 */
  @NestedConfigurationProperty @Valid private CryptoConfig crypto = new CryptoConfig();

  /** 线程池配置 */
  @NestedConfigurationProperty @Valid private ThreadConfig threads = new ThreadConfig();

  /** 请求限速配置 */
  @NestedConfigurationProperty @Valid private RateLimit rateLimit = new RateLimit();

  /** 组件 */
  private Components components = new Components();

  @Data
  @Validated
  public static class AuthConfig {

    /** Token 请求头 */
    private String header = "Authorization";

    /** Token 类型 */
    @NotBlank private String type = "Bearer";

    /** Access Token 失效时间（分钟） */
    @Min(0)
    private long expires = 1440;

    /** Refresh Token 失效时间（分钟） */
    @Min(0)
    private long refreshExpires = 43200;

    /** 黑名单缓存前缀 */
    @NotBlank private String blacklistPrefix = "BLACKLIST:";

    /** 黑名单缓存最大时间（毫秒） */
    @Min(0)
    private int blacklistTtl = 2592000;

    /** 登录失败累计周期（秒） */
    @Min(0)
    private long loginFailedCheckSeconds = 300L;

    /** 登录限制策略 */
    private LoginRestrictionStrategy loginRestrictionStrategy =
        LoginRestrictionStrategy.NO_RESTRICTION;
  }

  @Data
  @Validated
  public static class ThreadConfig {
    /** 线程名前缀 */
    @NotBlank private String prefix = "thread-pool-";

    /** 最大核心数 */
    @Min(1)
    private int maxPoolSize = 20;

    /** 核心线程数 */
    @Min(1)
    private int corePoolSize = 5;

    /** 队列容量 */
    @Min(1)
    private int queueCapacity = 100;

    /** 线程活跃时间 */
    @Min(1)
    private int keepAliveSeconds = 60;
  }

  @Data
  @Validated
  public static class RateLimit {
    /** 是否启用 */
    private boolean enabled = false;

    /** 限制数量 */
    @Min(0)
    private long limitThreshold = 60L;

    /** 计数周期时间（秒） */
    @Min(0)
    private long periodSeconds = 60L;
  }

  /** 验证码 */
  @Data
  @Validated
  public static class CaptchaConfig {
    /** 是否启用 */
    private boolean enabled = false;

    /** 验证码有效时间（秒），0 则不限制 */
    @Min(0)
    private long ttl = 300L;

    /** 验证码存储器类型 */
    private String repositoryType = "cache";

    @NestedConfigurationProperty @Valid private ImageCaptchaConfig image = new ImageCaptchaConfig();

    @NestedConfigurationProperty @Valid private SlideCaptchaConfig slide = new SlideCaptchaConfig();
  }

  /** 图片验证码 */
  @Data
  @Validated
  public static class ImageCaptchaConfig {
    /** 是否启用 */
    private boolean enabled = false;

    /** 验证码类型: circle, shear, gif, line */
    private String type = "line";

    /** 图片宽度 */
    @Min(1)
    private int width = 200;

    /** 图片高度 */
    @Min(1)
    private int height = 100;

    /** 字符长度 */
    @Min(1)
    private int length = 4;

    /** 混乱度 */
    @Min(1)
    private int chaos = 10;
  }

  /** 滑块验证码 */
  @Data
  @Validated
  public static class SlideCaptchaConfig {
    /** 是否启用 */
    private boolean enabled = false;

    /** 滑块宽度 */
    @Min(1)
    private int slideWidth = 45;

    /** 滑块高度 */
    @Min(1)
    private int slideHeight = 60;

    /** 半径 */
    @Min(1)
    private int circleR = 6;

    /** 距离点 */
    @Min(0)
    private int r1 = 3;

    /** 容忍度 */
    @Min(0)
    private int tolerance = 5;

    /** 滑块验证码图片目录 */
    private String imageDir = "captcha_images";

    /** 滑块验证码目录文件 */
    private String contentFile = "slide.txt";
  }

  @Data
  @Validated
  public static class CryptoConfig {
    /** 是否启用 */
    private boolean enabled = false;

    /** 加密模式：aes_rsa，aes_sm2，rsa，sm2 */
    private String mode = "";

    /** 公钥 */
    private String publicKey = "";

    /** 私钥 */
    private String privateKey = "";
  }

  @Data
  public static class Components {
    private Class<? extends PasswordEncoder> passwordEncoderClass = BCryptPasswordEncoder.class;
    private CodeGenerator captchaCodeGenerator = new RandomGenerator(4);
  }
}
