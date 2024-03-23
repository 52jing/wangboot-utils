package com.wangboot.core.auth.frontend;

import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 前端服务接口
 *
 * @author wwtg99
 */
public interface IFrontendService {

  @Nullable
  IFrontendModel getFrontendModelById(String id);

  @Nullable
  IFrontendModel getFrontendModelByName(String name);

  @NonNull
  List<? extends IFrontendModel> getFrontendModelsByType(String type);
}
