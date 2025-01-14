package com.wangboot.model.dataauthority.authorizer;

import cn.hutool.core.bean.DynaBean;
import com.wangboot.core.auth.authorization.IAuthorizationResource;
import com.wangboot.core.utils.StrUtils;
import java.util.Collection;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@AllArgsConstructor
public class DataScopeAuthorizer implements IDataAuthorizer {
  @Getter private final String field;

  @NonNull @Getter private final Collection<? extends IAuthorizationResource> authorities;

  @Override
  public boolean hasDataAuthority(@Nullable Object object) {
    if (Objects.isNull(object)) {
      return false;
    }
    String val =
        DynaBean.create(object).get(StrUtils.toCamelCase(this.getField(), false)).toString();
    return this.getAuthorities().stream().anyMatch(d -> d.getResourceName().equals(val));
  }
}
