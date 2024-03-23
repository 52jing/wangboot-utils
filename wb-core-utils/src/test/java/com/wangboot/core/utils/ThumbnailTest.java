package com.wangboot.core.utils;

import cn.hutool.core.io.resource.ResourceUtil;
import com.wangboot.core.utils.image.IThumbnail;
import com.wangboot.core.utils.image.impl.CompressThumbnail;
import com.wangboot.core.utils.image.impl.ScaleThumbnail;
import com.wangboot.core.utils.image.impl.SizeThumbnail;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("缩略图测试")
public class ThumbnailTest {
  @Test
  @SneakyThrows
  public void testThumbnail() {
    File outDir = new File("tmp");
    if (!outDir.exists()) {
      Assertions.assertTrue(outDir.mkdirs());
    }
    int originWidth = 1920;
    int originHeight = 1339;

    // 按比例缩放
    float scale = 0.5f;
    InputStream inputStream = this.getTestImage();
    IThumbnail thumbnail1 = new ScaleThumbnail(scale);
    BufferedImage image1 = thumbnail1.createThumb(inputStream);
    Assertions.assertEquals(originWidth * scale, image1.getWidth());
    inputStream.close();
    // 加水印
    String text = "wangboot";
    inputStream = this.getTestImage();
    thumbnail1 = thumbnail1.setWatermark(text, 0.5f, null, Color.WHITE);
    thumbnail1 = thumbnail1.setWatermark(text, 0.5f, Color.WHITE);
    thumbnail1 = thumbnail1.setWatermark(text, 0.8f);
    image1 = thumbnail1.setWatermark(text).createThumb(inputStream);
    Assertions.assertNotNull(image1);
    //    OutputStream outputStream = new FileOutputStream("tmp/out1.png");
    //    ImageIO.write(image1, "png", outputStream);
    inputStream.close();
    // 指定大小缩放
    int width = 600;
    int height = 400;
    int maxWidth = 300;
    int maxHeight = 300;
    int minWidth = 300;
    int minHeight = 300;
    // 指定宽度
    inputStream = this.getTestImage();
    IThumbnail thumbnail2 = new SizeThumbnail(width, null);
    BufferedImage image2 = thumbnail2.createThumb(inputStream);
    Assertions.assertEquals(width, image2.getWidth());
    inputStream.close();
    // 指定高度
    inputStream = this.getTestImage();
    thumbnail2 = new SizeThumbnail(null, height);
    image2 = thumbnail2.createThumb(inputStream);
    Assertions.assertEquals(height, image2.getHeight());
    inputStream.close();
    // 指定宽度和最大高度
    inputStream = this.getTestImage();
    thumbnail2 = new SizeThumbnail(width, null, null, maxHeight, null, null);
    image2 = thumbnail2.createThumb(inputStream);
    Assertions.assertTrue(image2.getWidth() < width);
    Assertions.assertEquals(maxHeight, image2.getHeight());
    inputStream.close();
    // 指定宽度和最小高度
    inputStream = this.getTestImage();
    thumbnail2 = new SizeThumbnail(width, null, null, null, null, minHeight);
    image2 = thumbnail2.createThumb(inputStream);
    Assertions.assertEquals(width, image2.getWidth());
    Assertions.assertTrue(image2.getHeight() > minHeight);
    inputStream.close();
    // 指定了高度和最大宽度
    inputStream = this.getTestImage();
    thumbnail2 = new SizeThumbnail(null, height, maxWidth, null, null, null);
    image2 = thumbnail2.createThumb(inputStream);
    Assertions.assertEquals(maxWidth, image2.getWidth());
    Assertions.assertTrue(image2.getHeight() < height);
    inputStream.close();
    // 指定了高度和最小宽度
    inputStream = this.getTestImage();
    thumbnail2 = new SizeThumbnail(null, height, null, null, minWidth, null);
    image2 = thumbnail2.createThumb(inputStream);
    Assertions.assertTrue(image2.getWidth() > minWidth);
    Assertions.assertEquals(height, image2.getHeight());
    inputStream.close();
    // 加水印
    inputStream = this.getTestImage();
    thumbnail2 = new SizeThumbnail(600, 500);
    image2 = thumbnail2.setWatermark(text, 0.5f, Color.WHITE).createThumb(inputStream);
    Assertions.assertNotNull(image2);
    //    outputStream = new FileOutputStream("tmp/out2.png");
    //    ImageIO.write(image2, "png", outputStream);
    inputStream.close();
    // 压缩缩放
    inputStream = this.getTestImage();
    IThumbnail thumbnail3 = new CompressThumbnail();
    BufferedImage image3 = thumbnail3.createThumb(inputStream);
    Assertions.assertEquals(originWidth, image3.getWidth());
    Assertions.assertEquals(originHeight, image3.getHeight());
    inputStream.close();
    // 加水印
    inputStream = this.getTestImage();
    image3 = thumbnail3.setWatermark(text, 0.3f, Color.WHITE).createThumb(inputStream);
    Assertions.assertNotNull(image3);
    //    outputStream = new FileOutputStream("tmp/out3.png");
    //    ImageIO.write(image3, "jpg", outputStream);
    inputStream.close();
  }

  private InputStream getTestImage() {
    String filename = "1.png";
    return ResourceUtil.getResourceObj(filename).getStream();
  }
}
