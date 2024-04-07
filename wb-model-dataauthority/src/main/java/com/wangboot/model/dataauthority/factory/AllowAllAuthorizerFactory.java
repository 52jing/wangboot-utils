package com.wangboot.model.dataauthority.factory;

import com.wangboot.model.dataauthority.authorizer.IDataAuthorizer;
import com.wangboot.model.dataauthority.authorizer.WholeDataAuthorizer;
import org.springframework.lang.Nullable;

public class AllowAllAuthorizerFactory implements IDataAuthorizerFactory {
  @Override
  @Nullable
  public IDataAuthorizer getDataAuthorizer(String field) {
    return new WholeDataAuthorizer(true);
  }
}
