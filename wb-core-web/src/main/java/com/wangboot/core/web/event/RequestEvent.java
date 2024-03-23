package com.wangboot.core.web.event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Generated;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 请求事件
 *
 * @author wwtg99
 */
@Generated
public class RequestEvent extends BaseRequestEvent<RequestRecord> {

  @Getter private final HttpServletResponse response;

  @Getter private final Exception ex;

  public RequestEvent(
      RequestRecord source,
      @Nullable HttpServletRequest request,
      @Nullable HttpServletResponse response,
      @Nullable Exception ex) {
    super(source, request);
    this.response = response;
    this.ex = ex;
  }
}
