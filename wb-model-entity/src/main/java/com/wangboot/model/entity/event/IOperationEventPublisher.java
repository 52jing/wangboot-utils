package com.wangboot.model.entity.event;

import com.wangboot.core.web.event.IEventPublisher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;

/**
 * 操作事件发布者
 *
 * @author wwtg99
 */
public interface IOperationEventPublisher extends IEventPublisher {
  /** 获取请求对象 */
  @Nullable
  HttpServletRequest getRequest();

  /** 获取用户ID */
  String getUserId();

  /** 发布操作事件 */
  default void publishOperationEvent(
      String eventType, String resource, String resourceId, @Nullable Object obj) {
    OperationLog operationLog =
        OperationLog.builder()
            .event(eventType)
            .userId(getUserId())
            .resource(resource)
            .resourceId(resourceId)
            .obj(obj)
            .build();
    this.publishEvent(new OperationEvent(operationLog, getRequest()));
  }

  default void publishOperationEvent(String eventType, String resource, @Nullable Object obj) {
    publishOperationEvent(eventType, resource, "", obj);
  }
}
