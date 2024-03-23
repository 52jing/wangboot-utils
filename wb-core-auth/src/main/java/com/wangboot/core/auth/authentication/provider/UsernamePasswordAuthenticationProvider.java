package com.wangboot.core.auth.authentication.provider;

import com.wangboot.core.auth.model.ILoginBody;
import com.wangboot.core.auth.user.IUserModel;
import com.wangboot.core.auth.user.IUserService;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 用户名密码认证
 *
 * @author wwtg99
 */
public class UsernamePasswordAuthenticationProvider
    extends AbstractUserServiceAuthenticationProvider {

  @NonNull private final PasswordEncoder passwordEncoder;

  public UsernamePasswordAuthenticationProvider(
      @NonNull IUserService userService, @NonNull PasswordEncoder passwordEncoder) {
    super(userService);
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  protected boolean validateUser(@NonNull ILoginBody model, @NonNull IUserModel userModel) {
    return this.passwordEncoder.matches(model.getPassword(), userModel.getPassword());
  }
}
