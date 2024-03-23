package com.wangboot.core.auth.user.impl;

import com.wangboot.core.auth.user.IUserModel;
import java.time.OffsetDateTime;
import lombok.*;

/**
 * 模拟用户
 *
 * @author wwtg99
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MockUser implements IUserModel {

  @Builder.Default private String id = "";
  @Builder.Default private String username = "";
  @Builder.Default private String password = "";
  @Builder.Default private boolean superuser = false;
  @Builder.Default private boolean staff = false;
  @Builder.Default private boolean enabled = true;
  @Builder.Default private boolean locked = false;
  @Builder.Default private OffsetDateTime expiredTime = null;

  public MockUser(String id, String username, String password) {
    this(id, username, password, false, false, true, false, null);
  }

  @Override
  public String getUserId() {
    return this.id;
  }

  @Override
  public boolean checkSuperuser() {
    return this.superuser;
  }

  @Override
  public boolean checkStaff() {
    return this.staff;
  }

  @Override
  public boolean checkEnabled() {
    return this.enabled;
  }

  @Override
  public boolean checkLocked() {
    return this.locked;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof IUserModel) {
      return this.getUserId().equals(((IUserModel) obj).getUserId())
          && this.getUsername().equals(((IUserModel) obj).getUsername());
    }
    return false;
  }

  @Override
  public String toString() {
    return this.getUsername();
  }
}
