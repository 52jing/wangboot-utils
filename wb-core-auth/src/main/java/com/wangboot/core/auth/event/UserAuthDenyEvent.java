package com.wangboot.core.auth.event;

import com.wangboot.core.web.event.BaseRequestEvent;
import javax.servlet.http.HttpServletRequest;
import lombok.Generated;
import org.springframework.lang.Nullable;

/**
 * 用户认证授权拒绝事件
 *
 * @author wwtg99
 */
@Generated
public class UserAuthDenyEvent extends BaseRequestEvent<UserAuthDenyEventLog> {
  public UserAuthDenyEvent(UserAuthDenyEventLog source, @Nullable HttpServletRequest request) {
    super(source, request);
  }
}
