package com.wangboot.core.captcha.slide;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlideCaptchaConfig {
  /** 滑块宽度 */
  private int slideWidth = 45;

  /** 滑块高度 */
  private int slideHeight = 60;

  /** 半径 */
  private int circleR = 6;

  /** 距离点 */
  private int r1 = 3;

  /** 容忍度 */
  private int tolerance = 5;

  /** 滑块验证码图片目录 */
  private String imageDir = "captcha_images";

  /** 滑块验证码目录文件 */
  private String contentFile = "slide.txt";
}
