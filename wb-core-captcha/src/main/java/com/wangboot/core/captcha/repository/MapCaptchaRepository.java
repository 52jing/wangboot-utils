package com.wangboot.core.captcha.repository;

import com.wangboot.core.captcha.ICaptchaData;
import com.wangboot.core.captcha.ICaptchaRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.lang.Nullable;

/**
 * 基于内存 Map 的存取器实现
 *
 * @author wwtg99
 */
public class MapCaptchaRepository implements ICaptchaRepository {

  private final Map<String, ICaptchaData> map;

  public MapCaptchaRepository() {
    this.map = new ConcurrentHashMap<>();
  }

  @Override
  public void save(ICaptchaData captcha) {
    this.map.put(captcha.getUid(), captcha);
  }

  @Override
  @Nullable
  public ICaptchaData get(String uid) {
    if (this.map.containsKey(uid)) {
      return this.map.get(uid);
    }
    return null;
  }

  @Override
  public void remove(String uid) {
    this.map.remove(uid);
  }
}
