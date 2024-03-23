package com.wangboot.core.auth.frontend.provider;

import com.wangboot.core.auth.exception.NonExistsFrontendException;
import com.wangboot.core.auth.frontend.IFrontendModel;
import com.wangboot.core.auth.frontend.IFrontendProvider;
import com.wangboot.core.auth.frontend.IFrontendService;
import com.wangboot.core.auth.model.IFrontendBody;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * 基于前端服务的简单验证器
 *
 * @author wwtg99
 */
@RequiredArgsConstructor
public class SimpleFrontendServiceProvider implements IFrontendProvider {

  @NonNull private final IFrontendService frontendService;

  @Override
  @NonNull
  public IFrontendModel validate(@NonNull IFrontendBody model) {
    return Optional.ofNullable(this.frontendService.getFrontendModelById(model.getFrontendId()))
        .orElseThrow(
            // 前端不存在
            () -> new NonExistsFrontendException(model.getFrontendId()));
  }
}
