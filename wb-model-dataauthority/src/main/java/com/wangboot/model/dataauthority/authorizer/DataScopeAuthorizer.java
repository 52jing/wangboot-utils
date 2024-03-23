package com.wangboot.model.dataauthority.authorizer;

import cn.hutool.core.bean.DynaBean;
import com.wangboot.core.auth.authorization.IAuthorizationResource;
import com.wangboot.core.auth.user.IUserModel;
import com.wangboot.model.dataauthority.DataAuthority;
import com.wangboot.model.dataauthority.DataAuthorityType;
import com.wangboot.model.dataauthority.datascope.IDataScopeModel;
import com.wangboot.model.dataauthority.utils.DataAuthorityUtils;
import java.util.Collection;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 基于数据范围的数据权限
 *
 * @author wwtg99
 */
@AllArgsConstructor
public class DataScopeAuthorizer implements IDataAuthorizer {

  @Getter @NonNull private final IUserModel userModel;
  @Getter @NonNull private final Collection<? extends IDataScopeModel> dataScopes;

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
      if (!dataAuthority.value().equals(DataAuthorityType.DATA_SCOPE)) {
        // 类型不符
        return false;
      }
      String field = dataAuthority.field();
      String val = DynaBean.create(data).get(field).toString();
      // 权限是否包含
      return this.hasDataScope(val);
    }
    // 不限制数据权限
    return true;
  }

  @Override
  public Collection<? extends IAuthorizationResource> getAuthorities() {
    return this.dataScopes;
  }

  /**
   * 是否包含数据范围内
   *
   * @param name 数据范围值
   * @return boolean
   */
  public boolean hasDataScope(String name) {
    if (!StringUtils.hasText(name)) {
      return false;
    }
    for (IDataScopeModel model : this.dataScopes) {
      if (model.getDataScopeName().equals(name)) {
        return true;
      }
    }
    return false;
  }
}
