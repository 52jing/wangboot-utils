package com.wangboot.model.dataauthority.authorizer;

import cn.hutool.core.bean.DynaBean;
import com.wangboot.core.auth.authorization.IAuthorizationResource;
import com.wangboot.core.utils.StrUtils;
import com.wangboot.model.dataauthority.datascope.SimpleDataScope;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

@AllArgsConstructor
public class UserIdDataAuthorizer implements IDataAuthorizer {

  @Getter private final String userId;
  @Getter private final String field;

  @Override
  public boolean hasDataAuthority(@Nullable Object object) {
    if (Objects.isNull(object)) {
      return false;
    }
    String fd = this.getField().contains("_") ? StrUtils.toCamelCase(this.getField(), false) : this.getField();
    String val =
        DynaBean.create(object).get(fd).toString();
    return StringUtils.hasText(this.userId) && this.userId.equals(val);
  }

  @Override
  public Collection<? extends IAuthorizationResource> getAuthorities() {
    return Collections.singletonList(new SimpleDataScope(this.userId));
  }
}
