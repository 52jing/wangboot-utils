package com.wangboot.core.web.event;

import org.springframework.context.ApplicationEvent;

/**
 * 配置更新事件
 *
 * @author wwtg99
 */
public class ParamUpdatedEvent extends ApplicationEvent {

  public ParamUpdatedEvent(ParamUpdatedRecord paramUpdatedRecord) {
    super(paramUpdatedRecord);
  }

  public ParamUpdatedRecord getRecord() {
    return (ParamUpdatedRecord) getSource();
  }
}
