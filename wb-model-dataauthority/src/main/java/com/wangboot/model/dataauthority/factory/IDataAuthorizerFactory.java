package com.wangboot.model.dataauthority.factory;

import com.wangboot.model.dataauthority.authorizer.IDataAuthorizer;
import org.springframework.lang.Nullable;

public interface IDataAuthorizerFactory {

  @Nullable
  IDataAuthorizer getDataAuthorizer(String field);
}
