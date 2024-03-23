package com.wangboot.core.utils.image.impl;

import com.wangboot.core.utils.image.IThumbnail;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * 抽象缩略图基类
 *
 * @author wwtg99
 */
public abstract class AbstractThumbnail implements IThumbnail {

  public static final String DEFAULT_FONT_NAME = "宋体";
  public static final int DEFAULT_FONT_STYLE = Font.PLAIN;
  public static final int DEFAULT_FONT_SIZE = 36;

  /** 水印文字 */
  @Getter @Setter private String watermarkText;

  /** 水印透明度 */
  @Getter @Setter private float watermarkOpacity = 0.5f;

  /** 水印字体 */
  @Setter private Font watermarkFont;

  /** 水印颜色 */
  @Getter @Setter private Color watermarkColor;

  @Override
  public IThumbnail setWatermark(String text, float opacity, Font font, Color color) {
    this.watermarkText = text;
    this.watermarkOpacity = opacity;
    this.watermarkFont = font;
    this.watermarkColor = color;
    return this;
  }

  public Font getWatermarkFont() {
    if (Objects.isNull(this.watermarkFont)) {
      this.watermarkFont = new Font(DEFAULT_FONT_NAME, DEFAULT_FONT_STYLE, DEFAULT_FONT_SIZE);
    }
    return this.watermarkFont;
  }

  protected void autoFont(int width) {
    if (Objects.isNull(this.watermarkFont)) {
      int size;
      if (width < 1000) {
        size = Math.min(width / 20, 30);
      } else {
        size = Math.min(width / 20, 120);
      }
      this.watermarkFont = new Font(DEFAULT_FONT_NAME, DEFAULT_FONT_STYLE, size);
    }
  }

  /** 生成水印图片 */
  protected BufferedImage generateWatermarkImage() {
    int fontSize = this.getWatermarkFont().getSize();
    int fontLength = this.getWatermarkText().length();
    int width = fontSize * fontLength;
    int height = fontSize * 2;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D g = image.createGraphics();
    g.setColor(this.getWatermarkColor());
    g.setFont(this.getWatermarkFont());
    FontMetrics fm = g.getFontMetrics(this.getWatermarkFont());
    int textWidth = fm.stringWidth(this.getWatermarkText());
    int textHeight = fm.getHeight();
    g.drawString(this.getWatermarkText(), (width - textWidth) / 2, height - textHeight);
    g.dispose();
    return image;
  }
}
