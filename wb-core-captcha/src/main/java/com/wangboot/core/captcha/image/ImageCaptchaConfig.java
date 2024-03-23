package com.wangboot.core.captcha.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片验证码配置
 *
 * @author wwtg99
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageCaptchaConfig {
  /** 验证码类型: circle, shear, gif, line */
  private String type = "line";

  /** 图片宽度 */
  @Builder.Default private int width = 200;

  /** 图片高度 */
  @Builder.Default private int height = 100;

  /** 字符长度 */
  @Builder.Default private int length = 4;

  /** 混乱度 */
  @Builder.Default private int chaos = 10;
}
