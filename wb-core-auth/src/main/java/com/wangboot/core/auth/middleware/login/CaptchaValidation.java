package com.wangboot.core.auth.middleware.login;

import com.wangboot.core.auth.exception.CaptchaMismatchException;
import com.wangboot.core.auth.middleware.ILoginMiddleware;
import com.wangboot.core.auth.model.ILoginBody;
import com.wangboot.core.captcha.CaptchaProcessorHolder;
import java.util.Arrays;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * 验证码检查
 *
 * @author wwtg99
 */
@Data
@AllArgsConstructor
public class CaptchaValidation implements ILoginMiddleware {

  /** 支持验证的类型 */
  private String[] checkTypes;

  @Override
  public ILoginBody beforeLogin(@NonNull ILoginBody body) {
    if (!this.checkCaptcha(body.getCaptchaType(), body.getCaptcha(), body.getUuid())) {
      throw new CaptchaMismatchException();
    }
    return body;
  }

  private boolean checkCaptcha(String captchaType, String captcha, String uuid) {
    if (!StringUtils.hasText(captchaType)
        || !StringUtils.hasText(captcha)
        || !StringUtils.hasText(uuid)) {
      // 未提供验证码类型或验证码或uuid则不通过
      return false;
    }
    if (Objects.nonNull(this.checkTypes)
        && Arrays.stream(this.checkTypes).anyMatch(t -> t.equalsIgnoreCase(captchaType))) {
      return CaptchaProcessorHolder.verifyCaptcha(captchaType, captcha, uuid);
    } else {
      return true;
    }
  }
}
