package com.wangboot.core.utils.image.impl;

import cn.hutool.core.util.StrUtil;
import java.awt.image.BufferedImage;
import java.io.IOException;
import lombok.Getter;
import lombok.Setter;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * 压缩创建缩略图
 *
 * @author wwtg99
 */
public class CompressThumbnail extends AbstractThumbnail {

  /** 压缩率 0 <= quality <= 1 */
  @Getter @Setter private float quality;

  public CompressThumbnail(float quality) {
    if (quality > 1) {
      quality = 1f;
    } else if (quality < 0) {
      quality = 0f;
    }
    this.quality = quality;
  }

  public CompressThumbnail() {
    this(0.6f);
  }

  @Override
  public BufferedImage createThumb(BufferedImage image) throws IOException {
    this.autoFont(image.getWidth());
    Thumbnails.Builder<BufferedImage> builder =
        Thumbnails.of(image)
            .size(image.getWidth(), image.getHeight())
            .outputFormat("JPEG")
            .outputQuality(this.getQuality());
    if (StrUtil.isNotEmpty(this.getWatermarkText())) {
      builder.watermark(
          Positions.BOTTOM_RIGHT, this.generateWatermarkImage(), this.getWatermarkOpacity());
    }
    return builder.asBufferedImage();
  }
}
