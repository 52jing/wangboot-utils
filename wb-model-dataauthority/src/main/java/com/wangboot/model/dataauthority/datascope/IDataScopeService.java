package com.wangboot.model.dataauthority.datascope;

import java.util.Collection;
import org.springframework.lang.Nullable;

/**
 * 用户权限范围服务接口
 *
 * @author wwtg99
 */
public interface IDataScopeService {

  /** 根据用户 ID 获取数据权限 */
  @Nullable
  Collection<? extends IDataScopeModel> getDataScopes(String userId);
}
