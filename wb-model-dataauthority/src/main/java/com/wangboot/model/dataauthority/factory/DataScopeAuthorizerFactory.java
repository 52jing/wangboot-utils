package com.wangboot.model.dataauthority.factory;

import cn.hutool.extra.spring.SpringUtil;
import com.wangboot.core.auth.utils.AuthUtils;
import com.wangboot.model.dataauthority.authorizer.DataScopeAuthorizer;
import com.wangboot.model.dataauthority.authorizer.IDataAuthorizer;
import com.wangboot.model.dataauthority.datascope.IDataScopeService;
import java.util.Objects;
import lombok.val;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public class DataScopeAuthorizerFactory implements IDataAuthorizerFactory {

  @Override
  @Nullable
  public IDataAuthorizer getDataAuthorizer(String field) {
    if (!StringUtils.hasText(field)) {
      return null;
    }
    IDataScopeService dataScopeService = SpringUtil.getBean(IDataScopeService.class);
    if (Objects.isNull(dataScopeService)) {
      return null;
    }
    val scopes = dataScopeService.getDataScopes(AuthUtils.getUserId());
    if (Objects.isNull(scopes)) {
      return null;
    }
    return new DataScopeAuthorizer(field, scopes);
  }
}
