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

  /** 校验超级管理员 */
  boolean checkSuperuser();

  /** 校验内部用户 */
  boolean checkStaff();

  /** 校验用户是否启用 */
  boolean checkEnabled();

  /** 校验用户是否锁定 */
  boolean checkLocked();

  @Nullable
  OffsetDateTime getExpiredTime();
}
