package com.wangboot.core.captcha.image;

import cn.hutool.captcha.*;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.codec.Base64;
import com.wangboot.core.captcha.ICaptchaData;
import com.wangboot.core.captcha.exception.InvalidUidException;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 图片验证码
 *
 * @author wwtg99
 */
public class ImageCaptcha implements ICaptchaData {

  @Getter private final String uid;
  @Getter private final String code;
  private final ImageCaptchaConfig property;
  private final CodeGenerator codeGenerator;
  private final ICaptcha captcha;

  /**
   * 新创建图片验证码
   *
   * @param property 配置
   * @param uid 识别码
   * @param codeGenerator 代码生成器
   */
  public ImageCaptcha(
      @NonNull ImageCaptchaConfig property, String uid, @Nullable CodeGenerator codeGenerator) {
    if (!StringUtils.hasText(uid)) {
      throw new InvalidUidException();
    }
    this.property = property;
    this.uid = uid;
    if (Objects.isNull(codeGenerator)) {
      this.codeGenerator = new RandomGenerator(this.property.getLength());
    } else {
      this.codeGenerator = codeGenerator;
    }
    this.captcha = this.createCaptcha();
    this.code = this.captcha.getCode();
  }

  /**
   * 从结果创建验证码，用于匹配
   *
   * @param uid 识别码
   * @param code 验证码
   */
  public ImageCaptcha(String uid, String code) {
    if (!StringUtils.hasText(uid)) {
      throw new InvalidUidException();
    }
    this.uid = uid;
    this.code = code;
    this.property = null;
    this.codeGenerator = null;
    this.captcha = null;
  }

  @Override
  public boolean match(@Nullable ICaptchaData captcha) {
    if (Objects.isNull(captcha)) {
      return false;
    }
    if (!StringUtils.hasText(captcha.getUid()) || !StringUtils.hasText(captcha.getCode())) {
      return false;
    }
    return this.captcha.verify(captcha.getCode());
  }

  /**
   * 获取 Base64 编码的图片
   *
   * @return String
   */
  public String toBase64() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    this.captcha.write(outputStream);
    byte[] imageBytes = outputStream.toByteArray();
    return Base64.encode(imageBytes);
  }

  /** 生成验证码 */
  private ICaptcha createCaptcha() {
    AbstractCaptcha c;
    switch (this.property.getType()) {
      case "circle":
        c =
            new CircleCaptcha(
                this.property.getWidth(),
                this.property.getHeight(),
                this.property.getLength(),
                this.property.getChaos());
        break;
      case "shear":
        c =
            new ShearCaptcha(
                this.property.getWidth(),
                this.property.getHeight(),
                this.property.getLength(),
                this.property.getChaos());
        break;
      case "gif":
        c =
            new GifCaptcha(
                this.property.getWidth(), this.property.getHeight(), this.property.getLength());
        break;
      default:
        c =
            new LineCaptcha(
                this.property.getWidth(),
                this.property.getHeight(),
                this.property.getLength(),
                this.property.getChaos());
    }
    // 设置代码生成器
    c.setGenerator(this.codeGenerator);
    return c;
  }
}
