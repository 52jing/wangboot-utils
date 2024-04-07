package com.wangboot.model.dataauthority.authorizer;

import java.util.Collection;
import org.springframework.lang.Nullable;

/**
 * 所有数据权限管理者
 *
 * @author wwtg99
 */
public class WholeDataAuthorizer implements IDataAuthorizer {

  private final boolean all;

  public WholeDataAuthorizer(boolean all) {
    this.all = all;
  }

  @Override
  public boolean hasDataAuthority(@Nullable Object object) {
    return this.all;
  }

  @Override
  public boolean hasDataAuthorities(@Nullable Collection<Object> objects) {
    return this.all;
  }

  @Override
  public String getField() {
    return "";
  }
}
