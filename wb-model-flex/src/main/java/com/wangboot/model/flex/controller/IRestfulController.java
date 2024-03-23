package com.wangboot.model.flex.controller;

import com.wangboot.core.auth.utils.AuthUtils;
import com.wangboot.core.web.utils.ServletUtils;
import com.wangboot.model.entity.event.IOperationEventPublisher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;

/** Restful 基础控制器接口 */
public interface IRestfulController extends IOperationEventPublisher {

  /**
   * 获取当前用户ID
   *
   * @return 用户ID
   */
  @Override
  default String getUserId() {
    return AuthUtils.getUserId();
  }

  /**
   * 获取请求
   *
   * @return 请求
   */
  @Override
  @Nullable
  default HttpServletRequest getRequest() {
    return ServletUtils.getRequest();
  }
}
