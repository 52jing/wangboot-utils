package com.wangboot.core.auth.user;

import java.io.Serializable;
import java.time.OffsetDateTime;
import org.springframework.lang.Nullable;

/**
 * 用户模型接口
 *
 * @author wwtg99
 */
public interface IUserModel extends Serializable {

  String getUserId();

  String getUsername();

  String getPassword();

  boolean checkSuperuser();

  boolean checkStaff();

  boolean checkEnabled();

  boolean checkLocked();

  @Nullable
  OffsetDateTime getExpiredTime();
}
