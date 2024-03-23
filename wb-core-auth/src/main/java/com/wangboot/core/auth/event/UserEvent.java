package com.wangboot.core.auth.event;

import com.wangboot.core.web.event.BaseRequestEvent;
import javax.servlet.http.HttpServletRequest;
import lombok.Generated;
import org.springframework.lang.Nullable;

/**
 * 用户事件
 *
 * @author wwtg99
 */
@Generated
public class UserEvent extends BaseRequestEvent<UserEventLog> {

  public UserEvent(UserEventLog source, @Nullable HttpServletRequest request) {
    super(source, request);
  }
}
