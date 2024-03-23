package com.wangboot.core.auth.frontend;

import com.wangboot.core.auth.model.IFrontendBody;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 前端验证管理器
 *
 * @author wwtg99
 */
public class FrontendManager {
  private final List<IFrontendProvider> providerList = new ArrayList<>();

  public void addProvider(@NonNull IFrontendProvider provider) {
    this.providerList.add(provider);
  }

  @NonNull
  public List<IFrontendProvider> getProviderList() {
    return this.providerList;
  }

  @Nullable
  public IFrontendModel validate(@Nullable IFrontendBody body) {
    if (Objects.isNull(body)) {
      return null;
    }
    RuntimeException lastEx = null;
    IFrontendModel frontendModel = null;
    for (IFrontendProvider provider : this.getProviderList()) {
      // 遍历所有前端认证提供者
      // 任一通过即可
      try {
        frontendModel = provider.validate(body);
        if (Objects.nonNull(frontendModel)) {
          // 验证通过，结束遍历
          break;
        }
      } catch (RuntimeException ex) {
        lastEx = ex;
      }
    }
    if (Objects.nonNull(frontendModel)) {
      // 认证成功
      return frontendModel;
    } else if (Objects.nonNull(lastEx)) {
      // 认证失败，捕获异常
      throw lastEx;
    } else {
      // 认证失败，未捕获异常
      return null;
    }
  }
}
