package com.wangboot.model.dataauthority.authorizer;

import cn.hutool.core.bean.DynaBean;
import com.wangboot.core.auth.authorization.IAuthorizationResource;
import com.wangboot.core.auth.user.IUserModel;
import com.wangboot.model.dataauthority.DataAuthority;
import com.wangboot.model.dataauthority.DataAuthorityType;
import com.wangboot.model.dataauthority.datascope.SimpleDataScope;
import com.wangboot.model.dataauthority.utils.DataAuthorityUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 当前用户数据权限
 *
 * @author wwtg99
 */
@AllArgsConstructor
public class UserIdFieldDataAuthorizer implements IDataAuthorizer {

  @Getter @NonNull private final IUserModel userModel;

  @Override
  public boolean hasAllAuthorities() {
    return this.userModel.checkSuperuser();
  }

  @Override
  public boolean authorize(@Nullable Object data) {
    if (this.hasAllAuthorities()) {
      return true;
    }
    if (Objects.isNull(data)) {
      return false;
    }
    if (DataAuthorityUtils.restrictDataAuthority(data.getClass())) {
      DataAuthority dataAuthority = DataAuthorityUtils.getDataAuthority(data.getClass());
      if (!dataAuthority.value().equals(DataAuthorityType.USER_ID_FIELD)) {
        // 类型不符
        return false;
      }
      String field = dataAuthority.field();
      String val = DynaBean.create(data).get(field).toString();
      // 根据用户 ID 判断是否一致
      return this.userModel.getUserId().equals(val);
    }
    // 不限制数据权限
    return true;
  }

  @Override
  public Collection<? extends IAuthorizationResource> getAuthorities() {
    return Collections.singleton(new SimpleDataScope(this.getUserModel().getUserId()));
  }
}
