package com.wangboot.core.auth.authentication.provider;

import com.wangboot.core.auth.authentication.IAuthenticationProvider;
import com.wangboot.core.auth.exception.*;
import com.wangboot.core.auth.model.ILoginBody;
import com.wangboot.core.auth.user.IUserModel;
import com.wangboot.core.auth.user.IUserService;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * 基于用户服务的认证提供者
 *
 * @author wwtg99
 */
@RequiredArgsConstructor
public abstract class AbstractUserServiceAuthenticationProvider implements IAuthenticationProvider {

  /** 用户服务 */
  @Getter @NonNull private final IUserService userService;

  @Override
  public IUserModel authenticate(@NonNull ILoginBody model) {
    if (!StringUtils.hasText(model.getUsername()) || !StringUtils.hasText(model.getPassword())) {
      throw new UsernamePasswordMismatchException(model.getUsername());
    }
    // 获取用户模型
    IUserModel userModel =
        Optional.ofNullable(this.userService.getUserModelByUsername(model.getUsername()))
            .orElseThrow(
                // 用户不存在
                () -> new NonExistsAccountException(model.getUsername()));
    if (Objects.nonNull(userModel.getExpiredTime())
        && userModel.getExpiredTime().isBefore(OffsetDateTime.now())) {
      // 用户已失效
      throw new ExpiredAccountException(model.getUsername());
    } else if (userModel.checkLocked()) {
      // 用户已锁定
      throw new LockedAccountException(model.getUsername());
    } else if (!userModel.checkEnabled()) {
      // 用户未激活
      throw new InvalidAccountException(model.getUsername());
    } else {
      // 验证用户
      if (this.validateUser(model, userModel)) {
        return userModel;
      } else {
        // 密码验证失败
        throw new UsernamePasswordMismatchException(model.getUsername());
      }
    }
  }

  /** 验证用户 */
  protected abstract boolean validateUser(@NonNull ILoginBody model, @NonNull IUserModel userModel);
}
