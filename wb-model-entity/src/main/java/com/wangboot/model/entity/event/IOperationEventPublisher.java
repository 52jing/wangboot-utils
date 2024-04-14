package com.wangboot.model.entity.event;

import com.wangboot.core.web.event.IEventPublisher;
import com.wangboot.model.entity.utils.EntityUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

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
      @NonNull Class<?> entityClass, String eventType, String resourceId, @Nullable Object obj) {
    if (EntityUtils.isOperationLogEnabled(entityClass)) {
      EnableOperationLog annotation = EntityUtils.getOperationLogAnnotation(entityClass);
      String resource =
          StringUtils.hasText(annotation.value())
              ? annotation.value()
              : entityClass.getSimpleName();
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
  }

  default void publishOperationEvent(
      @NonNull Class<?> entityClass, String eventType, @Nullable Object obj) {
    publishOperationEvent(entityClass, eventType, "", obj);
  }
}
