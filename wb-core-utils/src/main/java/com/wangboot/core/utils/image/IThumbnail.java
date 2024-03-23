package com.wangboot.core.utils.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import lombok.Generated;

/**
 * 缩略图接口
 *
 * @author wwtg99
 */
public interface IThumbnail {

  /** 设置水印 */
  IThumbnail setWatermark(String text, float opacity, Font font, Color fontColor);

  /** 设置水印 */
  default IThumbnail setWatermark(String text, float opacity, Color fontColor) {
    return setWatermark(text, opacity, null, fontColor);
  }

  /** 设置水印 */
  default IThumbnail setWatermark(String text, float opacity) {
    return setWatermark(text, opacity, null, Color.WHITE);
  }

  /** 设置水印 */
  default IThumbnail setWatermark(String text) {
    return setWatermark(text, 1f, null, Color.WHITE);
  }

  /** 创建缩略图 */
  BufferedImage createThumb(BufferedImage image) throws IOException;

  /** 创建缩略图 */
  @Generated
  default BufferedImage createThumb(InputStream input) throws IOException {
    return createThumb(ImageIO.read(input));
  }

  /** 创建缩略图 */
  @Generated
  default BufferedImage createThumb(File input) throws IOException {
    return createThumb(ImageIO.read(input));
  }

  /** 创建缩略图 */
  @Generated
  default BufferedImage createThumb(ImageInputStream input) throws IOException {
    return createThumb(ImageIO.read(input));
  }

  /** 创建缩略图 */
  @Generated
  default BufferedImage createThumb(URL input) throws IOException {
    return createThumb(ImageIO.read(input));
  }
}
