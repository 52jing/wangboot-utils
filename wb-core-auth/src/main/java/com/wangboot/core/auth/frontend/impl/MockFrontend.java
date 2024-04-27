package com.wangboot.core.auth.frontend.impl;

import com.wangboot.core.auth.frontend.IFrontendModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

/**
 * 模拟前端
 *
 * @author wwtg99
 */
@Data
@AllArgsConstructor
public class MockFrontend implements IFrontendModel {

  @NonNull private String id;
  @NonNull private String name;
  @NonNull private String type;
  private boolean staffOnly;
  private boolean allowRegister;

  public boolean staffOnly() {
    return this.staffOnly;
  }

  @Override
  public boolean allowRegister() {
    return this.allowRegister;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof IFrontendModel) {
      return this.getId().equals(((IFrontendModel) obj).getId())
          && this.getName().equals(((IFrontendModel) obj).getName());
    }
    return false;
  }

  @Override
  public String toString() {
    return this.getName();
  }
}
