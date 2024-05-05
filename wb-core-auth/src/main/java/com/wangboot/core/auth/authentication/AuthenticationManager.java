package com.wangboot.core.auth.authentication;

import com.wangboot.core.auth.exception.LoginFailedException;
import com.wangboot.core.auth.model.ILoginBody;
import com.wangboot.core.auth.user.IUserModel;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 登录验证管理器<br>
 * 注册认证提供者，根据认证提供者判断用户是否认证成功。
 *
 * @author wwtg99
 */
public class AuthenticationManager {
  private final List<String> providerList = new ArrayList<>();
  private final Map<String, IAuthenticationProvider> providerMap = new HashMap<>();

  /**
   * 注册认证提供者
   *
   * @param name 名称
   * @param provider 认证提供者
   */
  public void addProvider(String name, @NonNull IAuthenticationProvider provider) {
    if (StringUtils.hasText(name)) {
      this.providerMap.put(name, provider);
      this.providerList.add(name);
    }
  }

  /**
   * 根据名称获取认证提供者
   *
   * @param name 名称
   * @return 认证提供者
   */
  @Nullable
  public IAuthenticationProvider getProvider(String name) {
    if (StringUtils.hasText(name)) {
      return this.providerMap.get(name);
    }
    return null;
  }

  /**
   * 获取所有认证提供者
   *
   * @return 认证提供者列表
   */
  @NonNull
  public List<IAuthenticationProvider> getProviderList() {
    return this.providerList.stream().map(providerMap::get).collect(Collectors.toList());
  }

  /** 根据登录类型或逐个验证认证提供者 */
  @NonNull
  public IUserModel authenticate(@NonNull ILoginBody loginBody) {
    if (StringUtils.hasText(loginBody.getLoginType())) {
      // 如果指定了认证提供者，则只检查特定认证器
      IAuthenticationProvider provider = this.getProvider(loginBody.getLoginType());
      if (Objects.nonNull(provider)) {
        IUserModel userModel = provider.authenticate(loginBody);
        if (Objects.isNull(userModel)) {
          throw new LoginFailedException();
        }
        return userModel;
      } else {
        // 无效的登录类型
        throw new LoginFailedException("invalid login type");
      }
    } else {
      RuntimeException lastEx = null;
      IUserModel userModel = null;
      for (IAuthenticationProvider provider : this.getProviderList()) {
        // 不指定认证提供者，遍历所有认证提供者
        // 任一通过即可
        try {
          userModel = provider.authenticate(loginBody);
          if (Objects.nonNull(userModel)) {
            // 验证通过，结束遍历
            break;
          }
        } catch (RuntimeException ex) {
          lastEx = ex;
        }
      }
      if (Objects.nonNull(userModel)) {
        // 认证成功
        return userModel;
      } else if (Objects.nonNull(lastEx)) {
        // 认证失败，捕获异常
        throw lastEx;
      } else {
        // 认证失败，未捕获异常
        throw new LoginFailedException();
      }
    }
  }
}
