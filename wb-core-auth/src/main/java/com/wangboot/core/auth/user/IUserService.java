package com.wangboot.core.auth.user;

import org.springframework.lang.Nullable;

/**
 * 用户服务接口
 *
 * @author wwtg99
 */
public interface IUserService {

  /** 用户名获取用户模型 */
  @Nullable
  IUserModel getUserModelByUsername(String username);

  /** 用户ID获取用户模型 */
  @Nullable
  IUserModel getUserModelById(String id);

  /** 设置用户密码 */
  boolean setPassword(@Nullable IUserModel user, String newPwd);

  /** 验证用户密码 */
  boolean verifyPassword(@Nullable IUserModel userModel, String pwd);

  /** 用户注销 */
  boolean logout(@Nullable IUserModel user);

  /** 激活用户 */
  boolean enableUser(String userId);

  /** 失活用户 */
  boolean disableUser(String userId);

  /** 锁定用户 */
  boolean lockUser(String userId);

  /** 解锁用户 */
  boolean unlockUser(String userId);

  /** 用户是否已锁定 */
  boolean isUserLocked(String userId);
}
