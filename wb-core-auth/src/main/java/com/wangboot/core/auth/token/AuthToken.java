package com.wangboot.core.auth.token;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 访问令牌实现类
 *
 * @author wwtg99
 */
@Data
@Builder
@EqualsAndHashCode
public class AuthToken implements IAuthToken {

  private static final long serialVersionUID = 1L;

  @Builder.Default private String string = "";

  @Builder.Default private String tokenType = "";

  @Builder.Default private String userId = "";

  @Builder.Default private String username = "";

  @Builder.Default private String frontendId = "";

  @Builder.Default private String issuer = "";

  @Override
  public String toString() {
    return this.getString();
  }
}
