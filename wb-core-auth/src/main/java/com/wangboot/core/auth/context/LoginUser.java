package com.wangboot.core.auth.context;

import com.wangboot.core.auth.authorization.IAuthorizer;
import com.wangboot.core.auth.frontend.IFrontendModel;
import com.wangboot.core.auth.user.IUserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

/**
 * 登录用户实现
 *
 * @author wwtg99
 */
@Data
@AllArgsConstructor
public class LoginUser implements ILoginUser {

  @NonNull private IUserModel user;

  @NonNull private IFrontendModel frontend;

  private IAuthorizer authorizer;
}
