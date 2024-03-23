package com.wangboot.model.dataauthority.authorizer;

import com.wangboot.core.auth.user.IUserModel;
import org.springframework.lang.Nullable;

/**
 * 数据权限授权服务
 *
 * @author wwtg99
 */
public interface IDataAuthorizerService {

  @Nullable
  <T> IDataAuthorizer getDataAuthorizer(
      @Nullable IUserModel userModel, @Nullable Class<T> entityClass);
}
