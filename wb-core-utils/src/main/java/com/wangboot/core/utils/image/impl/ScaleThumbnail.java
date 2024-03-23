package com.wangboot.core.utils.image.impl;

import cn.hutool.core.util.StrUtil;
import java.awt.image.BufferedImage;
import java.io.IOException;
import lombok.Getter;
import lombok.Setter;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * 指定比例创建缩略图
 *
 * @author wwtg99
 */
public class ScaleThumbnail extends AbstractThumbnail {

  /** 比例 0 <= scale <= 1 */
  @Getter @Setter private float scale;

  public ScaleThumbnail(float scale) {
    if (scale > 1) {
      scale = 1f;
    } else if (scale <= 0) {
      scale = 0f;
    }
    this.scale = scale;
  }

  @Override
  public BufferedImage createThumb(BufferedImage image) throws IOException {
    this.autoFont(Math.round(image.getWidth() * this.getScale()));
    Thumbnails.Builder<BufferedImage> builder = Thumbnails.of(image).scale(this.getScale());
    if (StrUtil.isNotEmpty(this.getWatermarkText())) {
      builder.watermark(
          Positions.BOTTOM_RIGHT, this.generateWatermarkImage(), this.getWatermarkOpacity());
    }
    return builder.asBufferedImage();
  }
}
