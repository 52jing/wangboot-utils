package com.wangboot.model.entity.controller;

import cn.hutool.core.annotation.AnnotationUtil;
import java.util.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Restful 控制器基类
 *
 * @author wwtg99
 */
public abstract class RestfulBaseController {

  protected final Set<ControllerApi> enabledApis;

  public RestfulBaseController() {
    enabledApis = this.getEnabledApis();
  }

  protected boolean isEnabledApi(ControllerApi api) {
    return enabledApis.contains(api);
  }

  @Nullable
  protected EnableApi getEnableApiAnnotation() {
    return AnnotationUtil.getAnnotation(this.getClass(), EnableApi.class);
  }

  /**
   * 从 EnableApi 注解获取启用的 API 功能
   *
   * @return 启用的 API 集合
   */
  @NonNull
  protected Set<ControllerApi> getEnabledApis() {
    EnableApi enableApi = this.getEnableApiAnnotation();
    if (Objects.isNull(enableApi)) {
      return new HashSet<>();
    }
    Set<ControllerApi> apis = new HashSet<>();
    ControllerApiGroup group = enableApi.value();
    switch (group) {
      case FULL:
        apis.add(ControllerApi.LIST);
        apis.add(ControllerApi.DETAIL);
        apis.add(ControllerApi.CREATE);
        apis.add(ControllerApi.UPDATE);
        apis.add(ControllerApi.REMOVE);
        apis.add(ControllerApi.BATCH_REMOVE);
        break;
      case READ_ONLY:
        apis.add(ControllerApi.LIST);
        apis.add(ControllerApi.DETAIL);
        break;
      case WRITE_ONLY:
      case TREE_WRITE_ONLY:
        apis.add(ControllerApi.CREATE);
        apis.add(ControllerApi.UPDATE);
        apis.add(ControllerApi.REMOVE);
        apis.add(ControllerApi.BATCH_REMOVE);
        break;
      case TREE_FULL:
        apis.add(ControllerApi.LIST);
        apis.add(ControllerApi.DETAIL);
        apis.add(ControllerApi.CREATE);
        apis.add(ControllerApi.UPDATE);
        apis.add(ControllerApi.REMOVE);
        apis.add(ControllerApi.BATCH_REMOVE);
        apis.add(ControllerApi.LIST_ROOT);
        apis.add(ControllerApi.LIST_CHILDREN);
        break;
      case TREE_READ_ONLY:
        apis.add(ControllerApi.LIST);
        apis.add(ControllerApi.DETAIL);
        apis.add(ControllerApi.LIST_ROOT);
        apis.add(ControllerApi.LIST_CHILDREN);
        break;
      default:
        break;
    }
    if (enableApi.includes().length > 0) {
      apis.addAll(Arrays.asList(enableApi.includes()));
    }
    if (enableApi.excludes().length > 0) {
      Arrays.asList(enableApi.excludes()).forEach(apis::remove);
    }
    return apis;
  }
}
