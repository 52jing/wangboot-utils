package com.wangboot.core.auth.context;

import com.wangboot.core.auth.authorization.IAuthorizer;
import com.wangboot.core.auth.frontend.IFrontendModel;
import com.wangboot.core.auth.user.IUserModel;
import java.time.OffsetDateTime;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * 当前登录用户上下文接口
 *
 * @author wwtg99
 */
public interface ILoginUser extends IAuthentication {

  /** 用户模型 */
  @NonNull
  IUserModel getUser();

  /** 来源前端模型 */
  @NonNull
  IFrontendModel getFrontend();

  /** 授权验证者 */
  @NonNull
  IAuthorizer getAuthorizer();

  /** 是否已登录 */
  default boolean isLogin() {
    return StringUtils.hasText(getUser().getUserId());
  }

  /** 是否是超级用户 */
  default boolean isSuperuser() {
    return getUser().checkSuperuser();
  }

  /** 是否是内部用户 */
  default boolean isStaff() {
    return getUser().checkStaff();
  }

  /** 用户是否有效 */
  default boolean isUserValid() {
    return getUser().checkEnabled()
        && !getUser().checkLocked()
        && (Objects.isNull(getUser().getExpiredTime())
            || getUser().getExpiredTime().isAfter(OffsetDateTime.now()));
  }

  @Override
  default Object getCredentials() {
    return getUser().getPassword();
  }

  @Override
  default Object getPrincipal() {
    return getUser();
  }

  @Override
  default String getName() {
    return this.getUser().getUsername();
  }

  @Override
  default boolean isAuthenticated() {
    return StringUtils.hasText(getUser().getUserId());
  }
}
