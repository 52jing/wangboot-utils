package com.wangboot.model.dataauthority.authorizer;

import java.util.Collection;
import org.springframework.lang.Nullable;

/**
 * 无数据权限实现
 *
 * @author wwtg99
 */
public class NoneDataAuthorizer implements IDataAuthorizer {

  private final boolean all;

  public NoneDataAuthorizer(boolean all) {
    this.all = all;
  }

  @Override
  public boolean hasAllAuthorities() {
    return this.all;
  }

  @Override
  public boolean authorize(@Nullable Object data) {
    return this.all;
  }

  @Override
  public boolean authorizeAll(@Nullable Collection<?> data) {
    return this.all;
  }
}
