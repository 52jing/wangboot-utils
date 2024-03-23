package com.wangboot.core.web.event;

import java.util.Objects;
import lombok.Generated;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

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
  default void publishEvent(ApplicationEvent event) {
    if (this.supportEvent()) {
      this.getApplicationEventPublisher().publishEvent(event);
    }
  }
}
