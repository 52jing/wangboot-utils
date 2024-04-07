package com.wangboot.model.dataauthority.factory;

import com.wangboot.core.auth.utils.AuthUtils;
import com.wangboot.model.dataauthority.authorizer.IDataAuthorizer;
import com.wangboot.model.dataauthority.authorizer.UserIdDataAuthorizer;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public class UserIdAuthorizerFactory implements IDataAuthorizerFactory {
  @Override
  @Nullable
  public IDataAuthorizer getDataAuthorizer(String field) {
    if (!StringUtils.hasText(field)) {
      return null;
    }
    return new UserIdDataAuthorizer(AuthUtils.getUserId(), field);
  }
}
