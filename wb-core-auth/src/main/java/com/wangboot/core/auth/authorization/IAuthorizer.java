package com.wangboot.core.auth.authorization;

import java.io.Serializable;
import java.util.Collection;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 授权验证者接口
 *
 * @author wwtg99
 */
public interface IAuthorizer extends Serializable {

  /** 获取权限集合 */
  @NonNull
  Collection<? extends IAuthorizationResource> getAuthorities();

  /** 是否授权访问 */
  boolean authorize(@Nullable IAuthorizationResource resource);

  /** 是否授权访问任一资源 */
  boolean authorizeAny(IAuthorizationResource... resources);

  /** 是否授权访问所有资源 */
  boolean authorizeAll(IAuthorizationResource... resources);
}
