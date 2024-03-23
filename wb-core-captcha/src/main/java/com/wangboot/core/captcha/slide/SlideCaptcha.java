package com.wangboot.core.captcha.slide;

import com.wangboot.core.captcha.ICaptchaData;
import com.wangboot.core.captcha.exception.InvalidCaptchaImageException;
import com.wangboot.core.captcha.exception.InvalidUidException;
import com.wangboot.core.utils.ObjectUtils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/** 滑块验证码 */
public class SlideCaptcha implements ICaptchaData {

  @Getter private final String uid;

  private final SlideCaptchaConfig property;

  /** X坐标 */
  @Getter private final Integer x;

  /** Y坐标 */
  @Getter private final Integer y;

  /** 滑块图片 */
  private final transient BufferedImage slide;

  /** 背景图片 */
  private final transient BufferedImage back;

  /**
   * 新创建滑块验证码
   *
   * @param property 配置
   * @param uid 识别码
   * @param x x 坐标
   * @param y y 坐标
   * @param slide 滑块图
   * @param back 背景图
   */
  public SlideCaptcha(
      @NonNull SlideCaptchaConfig property,
      String uid,
      int x,
      int y,
      BufferedImage slide,
      BufferedImage back) {
    if (!StringUtils.hasText(uid)) {
      throw new InvalidUidException();
    }
    this.property = property;
    this.uid = uid;
    this.x = x;
    this.y = y;
    this.slide = slide;
    this.back = back;
  }

  /**
   * 从结果创建验证码，用于匹配
   *
   * @param uid 识别码
   * @param x x 坐标
   */
  public SlideCaptcha(String uid, int x) {
    if (!StringUtils.hasText(uid)) {
      throw new InvalidUidException();
    }
    this.property = null;
    this.uid = uid;
    this.x = x;
    this.y = null;
    this.slide = null;
    this.back = null;
  }

  @Override
  public String getCode() {
    return String.valueOf(this.x);
  }

  @Override
  public boolean match(@Nullable ICaptchaData captcha) {
    if (Objects.isNull(captcha)) {
      return false;
    }
    if (!StringUtils.hasText(captcha.getUid()) || !StringUtils.hasText(captcha.getCode())) {
      return false;
    }
    return captcha.getUid().equals(this.getUid())
        && Math.abs(Integer.parseInt(captcha.getCode()) - this.x) <= this.property.getTolerance();
  }

  public String slideToBase64() {
    try {
      return ObjectUtils.image2Base64(this.slide);
    } catch (IOException e) {
      throw new InvalidCaptchaImageException("Invalid slide image", e);
    }
  }

  public String backToBase64() {
    try {
      return ObjectUtils.image2Base64(this.back);
    } catch (IOException e) {
      throw new InvalidCaptchaImageException("Invalid background image", e);
    }
  }
}
