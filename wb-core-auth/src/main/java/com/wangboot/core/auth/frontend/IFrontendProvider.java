package com.wangboot.core.auth.frontend;

import com.wangboot.core.auth.model.IFrontendBody;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 前端验证提供者接口
 *
 * @author wwtg99
 */
public interface IFrontendProvider {

  /**
   * 验证前端<br>
   * 验证成功返回 IFrontendModel 实例<br>
   * 验证失败抛出 RuntimeException 异常或返回 null
   */
  @Nullable
  IFrontendModel validate(@NonNull IFrontendBody model);
}
