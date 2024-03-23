package com.wangboot.core.auth.token;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.lang.Nullable;

/**
 * 令牌对
 *
 * @author wwtg99
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TokenPair implements Serializable {
  public static final String ACCESS_TOKEN_TYPE = "access";
  public static final String REFRESH_TOKEN_TYPE = "refresh";

  @Nullable private IAuthToken accessToken;
  @Nullable private IAuthToken refreshToken;
}
