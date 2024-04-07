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

  /** 是否授权访问数据 */
  boolean hasDataAuthority(@Nullable Object object);

  /** 获取字段 */
  String getField();

  /** 获取数据权限集合 */
  @NonNull
  default Collection<? extends IAuthorizationResource> getAuthorities() {
    return Collections.emptyList();
  }

  /** 是否授权访问数据 */
  default boolean hasDataAuthorities(@Nullable Collection<Object> objects) {
    if (Objects.isNull(objects)) {
      return false;
    }
    return objects.stream().allMatch(this::hasDataAuthority);
  }
}
