package com.wangboot.core.utils.image.impl;

import cn.hutool.core.util.StrUtil;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * 指定宽高创建缩略图
 *
 * @author wwtg99
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SizeThumbnail extends AbstractThumbnail {

  private Integer width;

  private Integer height;

  private Integer maxWidth;

  private Integer maxHeight;

  private Integer minWidth;

  private Integer minHeight;

  public SizeThumbnail(Integer width, Integer height) {
    this.width = width;
    this.height = height;
    this.maxWidth = null;
    this.maxHeight = null;
    this.minWidth = null;
    this.minHeight = null;
  }

  @Override
  public BufferedImage createThumb(BufferedImage image) throws IOException {
    int originalWidth = image.getWidth();
    int originalHeight = image.getHeight();
    this.calculateThumbWidthHeight(originalWidth, originalHeight);
    this.autoFont(this.getWidth());
    Thumbnails.Builder<BufferedImage> builder =
        Thumbnails.of(image).forceSize(this.getWidth(), this.getHeight());
    if (StrUtil.isNotEmpty(this.getWatermarkText())) {
      builder.watermark(
          Positions.BOTTOM_RIGHT, this.generateWatermarkImage(), this.getWatermarkOpacity());
    }
    return builder.asBufferedImage();
  }

  protected void calculateThumbWidthHeight(int originalWidth, int originalHeight) {
    if (Objects.isNull(this.width) && Objects.isNull(this.height)) {
      // 未指定宽高
      this.width = originalWidth;
      this.height = originalHeight;
    } else if (Objects.isNull(this.height)) {
      // 指定了宽，高自动计算
      this.height = this.calculateHeight(this.width, originalWidth, originalHeight);
      if (Objects.nonNull(this.maxHeight)) {
        if (this.height > this.maxHeight) {
          // 高度超过最大高度，缩小宽度
          this.height = this.maxHeight;
          this.width = this.calculateWidth(this.height, originalWidth, originalHeight);
        } else if (this.height < this.minHeight) {
          // 高度小于最小高度，放大宽度
          this.height = this.minHeight;
          this.width = this.calculateWidth(this.height, originalWidth, originalHeight);
        }
      }
    } else if (Objects.isNull(this.width)) {
      // 指定了高，宽自动计算
      this.width = this.calculateWidth(this.height, originalWidth, originalHeight);
      if (Objects.nonNull(this.maxWidth)) {
        if (this.width > this.maxWidth) {
          // 宽度超过最大宽度，缩小高度
          this.width = this.maxWidth;
          this.height = this.calculateHeight(this.width, originalWidth, originalHeight);
        } else if (this.width < this.minWidth) {
          // 宽度小于最小宽度，放大高度
          this.width = this.minWidth;
          this.height = this.calculateHeight(this.width, originalWidth, originalHeight);
        }
      }
    }
  }

  protected int calculateWidth(int height, int originalWidth, int originalHeight) {
    return height * originalWidth / originalHeight;
  }

  protected int calculateHeight(int width, int originalWidth, int originalHeight) {
    return width * originalHeight / originalWidth;
  }
}
