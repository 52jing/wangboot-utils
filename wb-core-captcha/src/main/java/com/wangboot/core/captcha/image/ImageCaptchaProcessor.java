package com.wangboot.core.captcha.image;

import cn.hutool.captcha.generator.CodeGenerator;
import com.wangboot.core.captcha.ICaptchaData;
import com.wangboot.core.captcha.ICaptchaProcessor;
import com.wangboot.core.captcha.ICaptchaRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 图片验证码处理器
 *
 * @author wwtg99
 */
public class ImageCaptchaProcessor implements ICaptchaProcessor {

  public static final String BASE64_DATA_KEY = "base64_data";

  @NonNull private final ImageCaptchaConfig config;

  @NonNull @Getter private final ICaptchaRepository repository;

  @Nullable private final CodeGenerator codeGenerator;

  public ImageCaptchaProcessor(
      @NonNull ImageCaptchaConfig config,
      @NonNull ICaptchaRepository repository,
      @Nullable CodeGenerator codeGenerator) {
    this.config = config;
    this.repository = repository;
    this.codeGenerator = codeGenerator;
  }

  @Override
  @NonNull
  public ICaptchaData generateCaptcha(String uid) {
    return new ImageCaptcha(this.config, uid, this.codeGenerator);
  }

  @Override
  @NonNull
  public ICaptchaData fromCode(String uid, String code) {
    return new ImageCaptcha(uid, code);
  }

  @Override
  @NonNull
  public Map<String, String> send(@NonNull ICaptchaData captcha) {
    Map<String, String> map = new HashMap<>();
    if (captcha instanceof ImageCaptcha) {
      String b64 = ((ImageCaptcha) captcha).toBase64();
      map.put(BASE64_DATA_KEY, b64);
    }
    return map;
  }
}
