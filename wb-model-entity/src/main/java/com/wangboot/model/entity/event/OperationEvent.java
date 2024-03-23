package com.wangboot.model.entity.event;

import com.wangboot.core.web.event.BaseRequestEvent;
import javax.servlet.http.HttpServletRequest;
import lombok.Generated;
import org.springframework.lang.Nullable;

/**
 * 操作事件
 *
 * @author wwtg99
 */
@Generated
public class OperationEvent extends BaseRequestEvent<OperationLog> {
  public OperationEvent(OperationLog source, @Nullable HttpServletRequest request) {
    super(source, request);
  }
}
