package com.wangboot.core.auth.security;

import com.wangboot.core.auth.user.IUserModel;
import com.wangboot.core.auth.user.IUserService;
import com.wangboot.core.cache.CacheUtil;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * 登录失败锁定
 *
 * @author wwtg99
 */
@Deprecated
@RequiredArgsConstructor
public class LoginLock {
  public static final String CACHE_CHECK_PREFIX = "login_failed_";
  public static final String CACHE_LOCK_PREFIX = "login_locked_";

  @NonNull private final IUserService userService;

  /** 失败次数阈值 */
  @Getter @Setter private int failedThreshold = 0;

  /** 检查缓存时间（秒） */
  @Getter @Setter private long cacheSeconds = 300L;

  /** 锁定时间（秒） */
  @Getter @Setter private long lockSeconds = 1800L;

  /** 是否需要手动解锁 */
  @Getter @Setter private boolean manualUnlock = false;

  public LoginLock(
      @NonNull IUserService userService,
      int failedThreshold,
      long cacheSeconds,
      long lockSeconds,
      boolean manualUnlock) {
    this.userService = userService;
    this.failedThreshold = failedThreshold;
    this.cacheSeconds = cacheSeconds;
    this.lockSeconds = lockSeconds;
    this.manualUnlock = manualUnlock;
  }

  /**
   * 获取失败次数
   *
   * @param username 用户名
   * @return 失败次数
   */
  public int getCount(String username) {
    if (!StringUtils.hasText(username)) {
      return 0;
    }
    Object data = CacheUtil.get(this.getCacheKey(username), 0);
    int count = 0;
    if (Objects.nonNull(data)) {
      count = Integer.parseInt(data.toString());
    }
    return count;
  }

  /**
   * 用户名是否在缓存中已锁定
   *
   * @param username 用户名
   * @return 是否锁定
   */
  public boolean isLocked(String username) {
    if (!StringUtils.hasText(username)) {
      return false;
    }
    return CacheUtil.has(this.getLockKey(username));
  }

  /**
   * 记录一次失败
   *
   * @param username 用户名
   * @return 失败次数
   */
  public int logFailed(String username) {
    if (!StringUtils.hasText(username)) {
      return 0;
    }
    // 增加失败次数
    int count = this.getCount(username);
    count += 1;
    CacheUtil.put(this.getCacheKey(username), count, this.cacheSeconds * 1000);
    // 大于阈值锁定用户
    if (this.failedThreshold > 0 && count >= this.failedThreshold) {
      this.lock(username);
    }
    return count;
  }

  /**
   * 锁定用户
   *
   * @param username 用户名
   */
  public void lock(String username) {
    if (StringUtils.hasText(username)) {
      if (this.manualUnlock) {
        // 需要手动解锁
        IUserModel userModel = this.userService.getUserModelByUsername(username);
        if (Objects.nonNull(userModel)) {
          // 用户存在，使用服务锁定
          this.userService.lockUser(userModel.getUserId());
        } else {
          // 用户不存在，使用缓存锁定
          CacheUtil.put(this.getLockKey(username), 1, this.lockSeconds * 1000);
        }
      } else {
        CacheUtil.put(this.getLockKey(username), 1, this.lockSeconds * 1000);
      }
    }
  }

  private String getCacheKey(String username) {
    return CACHE_CHECK_PREFIX + username;
  }

  private String getLockKey(String username) {
    return CACHE_LOCK_PREFIX + username;
  }
}
