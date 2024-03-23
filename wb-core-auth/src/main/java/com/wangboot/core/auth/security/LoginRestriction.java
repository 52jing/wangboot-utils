package com.wangboot.core.auth.security;

import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.cache.CacheUtil;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * 登录限制
 *
 * @author wwtg99
 */
public class LoginRestriction {

  private static final String KEY_DEL = "_";

  /**
   * 登录限制守卫 用于登录后踢出其他令牌
   *
   * @param strategy 策略
   * @param loginUser 登录用户
   * @param token 令牌
   */
  public void acquireGuard(
      @NonNull LoginRestrictionStrategy strategy, @NonNull ILoginUser loginUser, String token) {
    if (StringUtils.hasText(token)) {
      String key = this.getCacheKey(strategy, loginUser);
      if (StringUtils.hasText(key)) {
        CacheUtil.put(key, token);
      }
    }
  }

  /**
   * 令牌是否能通过守卫
   *
   * @param strategy 策略
   * @param loginUser 登录用户
   * @param token 令牌
   * @return boolean
   */
  public boolean passGuard(
      @NonNull LoginRestrictionStrategy strategy, @NonNull ILoginUser loginUser, String token) {
    if (!StringUtils.hasText(token)) {
      return false;
    }
    String key = this.getCacheKey(strategy, loginUser);
    if (StringUtils.hasText(key)) {
      Object obj = CacheUtil.get(key);
      if (Objects.nonNull(obj)) {
        return token.equals(obj.toString());
      }
    }
    return true;
  }

  private String getCacheKey(
      @NonNull LoginRestrictionStrategy strategy, @NonNull ILoginUser loginUser) {
    switch (strategy) {
      case PER_TYPE:
        return loginUser.getUser().getUserId() + KEY_DEL + loginUser.getFrontend().getType();
      case PER_FRONTEND:
        return loginUser.getUser().getUserId() + KEY_DEL + loginUser.getFrontend().getId();
      case ONLY_ONE:
        return loginUser.getUser().getUserId();
      default:
        return "";
    }
  }
}
