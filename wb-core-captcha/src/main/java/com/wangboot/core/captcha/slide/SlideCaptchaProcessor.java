package com.wangboot.core.captcha.slide;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.RandomUtil;
import com.wangboot.core.captcha.ICaptchaData;
import com.wangboot.core.captcha.ICaptchaProcessor;
import com.wangboot.core.captcha.ICaptchaRepository;
import com.wangboot.core.captcha.exception.InvalidCaptchaImageException;
import com.wangboot.core.captcha.exception.InvalidUidException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/** 滑块验证码处理器 */
public class SlideCaptchaProcessor implements ICaptchaProcessor {

  public static final String SLIDE_IMAGE_KEY = "slide";
  public static final String BACK_IMAGE_KEY = "back";
  public static final String Y_KEY = "y";

  @NonNull private final SlideCaptchaConfig config;

  @Getter @NonNull private final ICaptchaRepository repository;

  public SlideCaptchaProcessor(
      @NonNull SlideCaptchaConfig config, @NonNull ICaptchaRepository repository) {
    this.config = config;
    this.repository = repository;
  }

  @Override
  @NonNull
  public ICaptchaData generateCaptcha(String uid) {
    if (!StringUtils.hasText(uid)) {
      throw new InvalidUidException();
    }
    InputStream inputStream = this.getFile();
    try {
      BufferedImage originalImage = ImageIO.read(inputStream);
      // 随机选取滑块坐标
      int[] xy = this.generateXY(originalImage);
      // 创建滑块
      BufferedImage cutoutImage =
          new BufferedImage(
              this.config.getSlideHeight(),
              this.config.getSlideWidth(),
              BufferedImage.TYPE_4BYTE_ABGR);
      // 获取滑块轮廓
      int[][] data = this.getBlockData();
      // 滑块填充，原图抠图
      this.cutByTemplate(originalImage, cutoutImage, data, xy[0], xy[1]);
      // 生成验证码对象
      return new SlideCaptcha(this.config, uid, xy[0], xy[1], cutoutImage, originalImage);
    } catch (IOException e) {
      throw new InvalidCaptchaImageException(e.getMessage(), e);
    }
  }

  @Override
  @NonNull
  public ICaptchaData fromCode(String uid, String code) {
    return new SlideCaptcha(uid, Integer.parseInt(code));
  }

  @Override
  public Map<String, String> send(ICaptchaData captcha) {
    Map<String, String> map = new HashMap<>();
    if (captcha instanceof SlideCaptcha) {
      map.put(SLIDE_IMAGE_KEY, ((SlideCaptcha) captcha).slideToBase64());
      map.put(BACK_IMAGE_KEY, ((SlideCaptcha) captcha).backToBase64());
      map.put(Y_KEY, ((SlideCaptcha) captcha).getY().toString());
    }
    return map;
  }

  /** 获取图片 */
  @NonNull
  private InputStream getFile() {
    String imgDir = this.config.getImageDir();
    String contentFile = this.config.getContentFile();
    String contents =
        ResourceUtil.getResourceObj(imgDir + File.separator + contentFile).readUtf8Str();
    List<String> fs =
        Arrays.stream(contents.split("\n"))
            .map(String::trim)
            .filter(StringUtils::hasText)
            .collect(Collectors.toList());
    if (fs.isEmpty()) {
      throw new InvalidCaptchaImageException("Image list is empty");
    }
    int picNum = fs.size();
    int selectIndex = RandomUtil.randomInt(0, picNum);
    return ResourceUtil.getStream(imgDir + File.separator + fs.get(selectIndex));
  }

  /** 生成滑块图轮廓 */
  @NonNull
  private int[][] getBlockData() {
    int[][] data = new int[this.config.getSlideHeight()][this.config.getSlideWidth()];
    double x2 = 1.0 * this.config.getSlideHeight() - this.config.getCircleR();
    double h1 =
        this.config.getCircleR()
            + RandomUtil.randomDouble()
                * (this.config.getSlideWidth()
                    - 3 * this.config.getCircleR()
                    - this.config.getR1());
    double po = 1.0 * this.config.getCircleR() * this.config.getCircleR();
    double xbegin =
        1.0 * this.config.getSlideHeight() - this.config.getCircleR() - this.config.getR1();
    double ybegin =
        1.0 * this.config.getSlideWidth() - this.config.getCircleR() - this.config.getR1();
    for (int i = 0; i < this.config.getSlideHeight(); i++) {
      for (int j = 0; j < this.config.getSlideWidth(); j++) {
        double d3 = Math.pow(i - x2, 2) + Math.pow(j - h1, 2);
        double d2 = Math.pow(j - 2.0, 2) + Math.pow(i - h1, 2);
        if ((j <= ybegin && d2 <= po) || (i >= xbegin && d3 >= po)) {
          data[i][j] = 0;
        } else {
          data[i][j] = 1;
        }
      }
    }
    return data;
  }

  /** 图片剪切 */
  private void cutByTemplate(
      BufferedImage oriImage, BufferedImage targetImage, int[][] templateImage, int x, int y) {
    for (int i = 0; i < this.config.getSlideHeight(); i++) {
      for (int j = 0; j < this.config.getSlideWidth(); j++) {
        int rgb = templateImage[i][j];
        // 原图中对应位置变色处理
        int rgbOri = oriImage.getRGB(x + i, y + j);
        if (rgb == 1) {
          // 抠图上复制对应颜色值
          targetImage.setRGB(i, j, rgbOri);
          // 原图对应位置颜色变化
          oriImage.setRGB(x + i, y + j, rgbOri & 0x363636);
        } else {
          // 这里把背景设为透明
          targetImage.setRGB(i, j, rgbOri & 0x00ffffff);
        }
      }
    }
  }

  /** 随机X与Y坐标 */
  @NonNull
  private int[] generateXY(BufferedImage originalImage) {
    // 原图宽度
    int width = originalImage.getWidth();
    // 原图高度
    int height = originalImage.getHeight();
    int x =
        RandomUtil.randomInt(width - this.config.getSlideWidth())
                % (width - this.config.getSlideWidth() - this.config.getSlideWidth() + 1)
            + this.config.getSlideWidth();
    int y =
        RandomUtil.randomInt(height - this.config.getSlideHeight())
                % (height - this.config.getSlideHeight() - this.config.getSlideHeight() + 1)
            + this.config.getSlideHeight();
    return new int[] {x, y};
  }
}
