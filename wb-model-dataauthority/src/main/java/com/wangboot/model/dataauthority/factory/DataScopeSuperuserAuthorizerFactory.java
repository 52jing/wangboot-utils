package com.wangboot.model.dataauthority.factory;

import com.wangboot.core.auth.utils.AuthUtils;
import com.wangboot.model.dataauthority.authorizer.IDataAuthorizer;
import com.wangboot.model.dataauthority.authorizer.WholeDataAuthorizer;
import org.springframework.lang.Nullable;

public class DataScopeSuperuserAuthorizerFactory extends DataScopeAuthorizerFactory {

  @Override
  @Nullable
  public IDataAuthorizer getDataAuthorizer(String field) {
    if (AuthUtils.isSuperuser()) {
      return new WholeDataAuthorizer(true);
    }
    return super.getDataAuthorizer(field);
  }
}
