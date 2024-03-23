package com.wangboot.model.dataauthority.authorizer;

import com.wangboot.core.auth.authorization.IAuthorizationResource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 数据权限管理者
 *
 * @author wwtg99
 */
public interface IDataAuthorizer extends Serializable {

  /** 是否具有所有数据权限 */
  default boolean hasAllAuthorities() {
    return false;
  }

  /** 是否授权访问数据 */
  boolean authorize(@Nullable Object data);

  /** 是否授权访问所有数据 */
  default boolean authorizeAll(@Nullable Collection<?> data) {
    if (this.hasAllAuthorities()) {
      return true;
    }
    if (Objects.isNull(data)) {
      return false;
    }
    if (data.isEmpty()) {
      // 空数据集
      return true;
    }
    for (Object d : data) {
      if (!this.authorize(d)) {
        return false;
      }
    }
    return true;
  }

  /** 获取数据权限集合 */
  @NonNull
  default Collection<? extends IAuthorizationResource> getAuthorities() {
    return Collections.emptyList();
  }
}
