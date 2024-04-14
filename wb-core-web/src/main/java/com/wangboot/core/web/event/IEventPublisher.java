package com.wangboot.core.web.event;

import java.util.Objects;
import lombok.Generated;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.Nullable;

/**
 * 应用事件发布者
 *
 * @author wwtg99
 */
@Generated
public interface IEventPublisher extends ApplicationEventPublisherAware {
  /** 获取应用事件发布器 */
  ApplicationEventPublisher getApplicationEventPublisher();

  /** 是否支持事件 */
  default boolean supportEvent() {
    return Objects.nonNull(this.getApplicationEventPublisher());
  }

  /**
   * 发布事件
   *
   * @param event 事件
   */
  default void publishEvent(@Nullable ApplicationEvent event) {
    if (this.supportEvent() && Objects.nonNull(event)) {
      this.getApplicationEventPublisher().publishEvent(event);
    }
  }
}
