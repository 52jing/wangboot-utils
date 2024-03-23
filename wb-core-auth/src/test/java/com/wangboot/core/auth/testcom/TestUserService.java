package com.wangboot.core.auth.testcom;

import com.wangboot.core.auth.authorization.IAuthorizer;
import com.wangboot.core.auth.authorization.IAuthorizerService;
import com.wangboot.core.auth.authorization.authorizer.SimpleAuthorizer;
import com.wangboot.core.auth.user.IUserModel;
import com.wangboot.core.auth.user.IUserService;
import com.wangboot.core.auth.user.impl.MockUser;
import com.wangboot.core.auth.utils.AuthUtils;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Objects;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public class TestUserService implements IUserService, IAuthorizerService {

  private IUserModel getAdmin() {
    return new MockUser("1", "admin", "123456", true, true, true, false, null);
  }

  private IUserModel getStaff() {
    return new MockUser("2", "staff", "123456", false, true, true, false, null);
  }

  private IUserModel getUser1() {
    return new MockUser("3", "user1", "123456");
  }

  private IUserModel getBlocked() {
    return new MockUser("4", "blocked", "", false, false, false, false, null);
  }

  private IUserModel getExpired() {
    return new MockUser(
        "5", "expired", "", false, false, true, false, OffsetDateTime.now().minusDays(1));
  }

  private IUserModel getLocked() {
    return new MockUser("6", "locked", "", false, false, true, true, null);
  }

  @Nullable
  @Override
  public IUserModel getUserModelByUsername(String username) {
    switch (username) {
      case "admin":
        return getAdmin();
      case "staff":
        return getStaff();
      case "user1":
        return getUser1();
      case "blocked":
        return getBlocked();
      case "expired":
        return getExpired();
      case "locked":
        return getLocked();
      default:
        return null;
    }
  }

  @Nullable
  @Override
  public IUserModel getUserModelById(String id) {
    switch (id) {
      case "1":
        return getAdmin();
      case "2":
        return getStaff();
      case "3":
        return getUser1();
      case "4":
        return getBlocked();
      case "5":
        return getExpired();
      case "6":
        return getLocked();
      default:
        return null;
    }
  }

  @Override
  public boolean setPassword(IUserModel user, String newPassword) {
    return true;
  }

  @Override
  public boolean verifyPassword(@Nullable IUserModel userModel, String pwd) {
    if (Objects.isNull(userModel) || !StringUtils.hasText(pwd)) {
      return false;
    }
    return pwd.equals(userModel.getPassword());
  }

  @Override
  public boolean logout(IUserModel user) {
    return true;
  }

  @Override
  public boolean enableUser(String userId) {
    return true;
  }

  @Override
  public boolean disableUser(String userId) {
    return true;
  }

  @Override
  public boolean lockUser(String userId) {
    return true;
  }

  @Override
  public boolean unlockUser(String userId) {
    return true;
  }

  @Override
  public boolean isUserLocked(String userId) {
    return false;
  }

  @Override
  public IAuthorizer getAuthorizer(@Nullable IUserModel userModel) {
    if (Objects.isNull(userModel)) {
      return AuthUtils.DENY_ALL;
    }
    if (userModel.checkSuperuser()) {
      return AuthUtils.ALLOW_ALL;
    }
    return new SimpleAuthorizer(userModel, Collections.emptyList());
  }
}
