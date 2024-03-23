package com.wangboot.core.auth.authorization;

import com.wangboot.core.auth.user.IUserModel;
import org.springframework.lang.Nullable;

/**
 * 权限管理服务接口
 *
 * @author wwtg99
 */
public interface IAuthorizerService {

  /**
   * 获取授权验证者
   *
   * @param userModel 用户
   * @return 授权验证者接口
   */
  @Nullable
  IAuthorizer getAuthorizer(@Nullable IUserModel userModel);
}
