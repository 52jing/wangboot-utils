package com.wangboot.core.captcha;

import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.RandomUtil;
import com.wangboot.core.captcha.exception.InvalidUidException;
import com.wangboot.core.captcha.image.ImageCaptcha;
import com.wangboot.core.captcha.image.ImageCaptchaConfig;
import com.wangboot.core.captcha.image.ImageCaptchaProcessor;
import com.wangboot.core.captcha.repository.CacheCaptchaRepository;
import com.wangboot.core.captcha.repository.MapCaptchaRepository;
import com.wangboot.core.captcha.slide.SlideCaptcha;
import com.wangboot.core.captcha.slide.SlideCaptchaConfig;
import com.wangboot.core.captcha.slide.SlideCaptchaProcessor;
import java.awt.image.BufferedImage;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("验证码测试")
public class CaptchaTest {

  @Test
  public void testImageCaptcha() {
    ImageCaptchaConfig property = new ImageCaptchaConfig();
    property.setType("circle");
    String uid = RandomUtil.randomString(8);
    ImageCaptcha captcha = new ImageCaptcha(property, uid, null);
    Assertions.assertEquals(uid, captcha.getUid());
    Assertions.assertEquals(property.getLength(), captcha.getCode().length());
    ImageCaptcha captcha1 = new ImageCaptcha(captcha.getUid(), captcha.getCode());
    Assertions.assertTrue(captcha.match(captcha1));
    Assertions.assertFalse(captcha.match(null));
    Assertions.assertThrows(
        InvalidUidException.class, () -> new ImageCaptcha("", captcha.getCode()));
    ImageCaptcha captcha2 = new ImageCaptcha(uid, RandomUtil.randomString(4));
    Assertions.assertFalse(captcha.match(captcha2));
    property.setType("shear");
    ImageCaptcha captcha3 = new ImageCaptcha(property, uid, new RandomGenerator(4));
    ImageCaptcha captcha4 = new ImageCaptcha(captcha3.getUid(), captcha3.getCode());
    Assertions.assertTrue(captcha3.match(captcha4));
    property.setType("gif");
    ImageCaptcha captcha5 = new ImageCaptcha(property, uid, new RandomGenerator(4));
    ImageCaptcha captcha6 = new ImageCaptcha(captcha5.getUid(), captcha5.getCode());
    Assertions.assertTrue(captcha5.match(captcha6));
    property.setType("line");
    ImageCaptcha captcha7 = new ImageCaptcha(property, uid, new RandomGenerator(4));
    ImageCaptcha captcha8 = new ImageCaptcha(captcha7.getUid(), captcha7.getCode());
    Assertions.assertTrue(captcha7.match(captcha8));
  }

  @Test
  public void testSlideCaptcha() {
    SlideCaptchaConfig property = new SlideCaptchaConfig();
    String uid = RandomUtil.randomString(6);
    int x = RandomUtil.randomInt();
    int y = RandomUtil.randomInt();
    BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    Assertions.assertThrows(
        InvalidUidException.class, () -> new SlideCaptcha(property, "", x, y, image, image));
    SlideCaptcha captcha = new SlideCaptcha(property, uid, x, y, image, image);
    Assertions.assertEquals(uid, captcha.getUid());
    SlideCaptcha captcha1 = new SlideCaptcha(uid, x);
    Assertions.assertTrue(captcha.match(captcha1));
    Assertions.assertThrows(InvalidUidException.class, () -> new SlideCaptcha("", x));
    Assertions.assertFalse(captcha.match(null));
    SlideCaptcha captcha2 = new SlideCaptcha(uid, RandomUtil.randomInt());
    Assertions.assertFalse(captcha.match(captcha2));
  }

  @Test
  @SneakyThrows
  public void testRepository() {
    // MapCaptchaRepository
    MapCaptchaRepository repository1 = new MapCaptchaRepository();
    String uid = RandomUtil.randomString(6);
    String code = RandomUtil.randomString(6);
    Assertions.assertNull(repository1.get(uid));
    repository1.save(new ImageCaptcha(uid, code));
    ICaptchaData data1 = repository1.get(uid);
    Assertions.assertNotNull(data1);
    Assertions.assertEquals(uid, data1.getUid());
    Assertions.assertEquals(code, data1.getCode());
    repository1.remove(uid);
    Assertions.assertNull(repository1.get(uid));
    // CacheCaptchaRepository
    CacheCaptchaRepository repository2 = new CacheCaptchaRepository(1000);
    Assertions.assertNull(repository2.get(uid));
    repository2.save(new ImageCaptcha(uid, code));
    ICaptchaData data2 = repository2.get(uid);
    Assertions.assertNotNull(data2);
    Assertions.assertEquals(uid, data2.getUid());
    Assertions.assertEquals(code, data2.getCode());
    repository1.remove(uid);
    Assertions.assertNull(repository1.get(uid));
    repository2.save(new ImageCaptcha(uid, code));
    Thread.sleep(1001);
    Assertions.assertNull(repository1.get(uid));
    repository2.save(new ImageCaptcha(uid, code));
    Assertions.assertNotNull(repository2.get(uid));
    repository2.remove(uid);
    Assertions.assertNull(repository2.get(uid));
  }

  @Test
  public void testImageCaptchaProcessor() {
    // generate
    ImageCaptchaConfig config = new ImageCaptchaConfig();
    ICaptchaRepository repository = new MapCaptchaRepository();
    ImageCaptchaProcessor p = new ImageCaptchaProcessor(config, repository, null);
    Assertions.assertNotNull(p);
    Assertions.assertThrows(InvalidUidException.class, () -> p.generateCaptcha(""));
    String uid = RandomUtil.randomString(8);
    ICaptchaData captchaData = p.generate(uid);
    Assertions.assertTrue(captchaData.getUid().length() > 0);
    Assertions.assertTrue(captchaData.getCode().length() > 0);
    Map<String, String> map = p.send(captchaData);
    Assertions.assertTrue(map.containsKey(ImageCaptchaProcessor.BASE64_DATA_KEY));
    // verify
    String uid2 = captchaData.getUid();
    String code = captchaData.getCode();
    ImageCaptchaProcessor processor1 = new ImageCaptchaProcessor(config, repository, null);
    Assertions.assertThrows(InvalidUidException.class, () -> processor1.fromCode("", ""));
    ICaptchaData captchaData1 = processor1.fromCode(uid2, code);
    Assertions.assertTrue(processor1.validate(captchaData1, true));
    Assertions.assertFalse(processor1.validate(null, false));
    // remove if not match
    ICaptchaData captchaData2 = processor1.fromCode(uid2, RandomUtil.randomString(4));
    Assertions.assertFalse(processor1.validate(captchaData2, true));
    Assertions.assertFalse(processor1.validate(captchaData1, true));
  }

  @Test
  public void testSlideCaptchaProcessor() {
    // generate
    SlideCaptchaConfig config = new SlideCaptchaConfig();
    ICaptchaRepository repository = new MapCaptchaRepository();
    SlideCaptchaProcessor processor = new SlideCaptchaProcessor(config, repository);
    Assertions.assertNotNull(processor.getRepository());
    String uid = RandomUtil.randomString(6);
    ICaptchaData captchaData = processor.generate(uid);
    Assertions.assertEquals(uid, captchaData.getUid());
    Assertions.assertTrue(captchaData.getCode().length() > 0);
    Map<String, String> map = processor.send(captchaData);
    Assertions.assertTrue(map.containsKey(SlideCaptchaProcessor.Y_KEY));
    // verify
    SlideCaptchaProcessor processor1 = new SlideCaptchaProcessor(config, repository);
    ICaptchaData captchaData1 = processor1.fromCode(uid, captchaData.getCode());
    Assertions.assertTrue(processor1.validate(captchaData1, true));
  }

  @Test
  public void testCaptchaProcessorHolder() {
    ImageCaptchaConfig config = new ImageCaptchaConfig();
    ICaptchaRepository repository = new MapCaptchaRepository();
    ImageCaptchaProcessor p = new ImageCaptchaProcessor(config, repository, null);
    String type = "image";
    CaptchaProcessorHolder.addProcessor(type, p);
    Assertions.assertNotNull(CaptchaProcessorHolder.getProcessor(type));
    Assertions.assertNull(CaptchaProcessorHolder.getProcessor(""));
    Assertions.assertNull(CaptchaProcessorHolder.getProcessor(RandomUtil.randomString(3)));
    String uid = RandomUtil.randomString(6);
    ICaptchaData captchaData = CaptchaProcessorHolder.generateCaptcha(type, uid);
    Assertions.assertNotNull(captchaData);
    Assertions.assertNull(CaptchaProcessorHolder.generateCaptcha("", uid));
    Map<String, String> map = CaptchaProcessorHolder.send(type, captchaData);
    Assertions.assertTrue(map.containsKey(ImageCaptchaProcessor.BASE64_DATA_KEY));
    Assertions.assertTrue(CaptchaProcessorHolder.send("", captchaData).isEmpty());
    Assertions.assertTrue(CaptchaProcessorHolder.verifyCaptcha(type, captchaData.getCode(), uid));
    Assertions.assertFalse(CaptchaProcessorHolder.verifyCaptcha("", captchaData.getCode(), uid));
    map = CaptchaProcessorHolder.generateAndSend(type, uid);
    Assertions.assertTrue(map.containsKey(ImageCaptchaProcessor.BASE64_DATA_KEY));
  }
}
