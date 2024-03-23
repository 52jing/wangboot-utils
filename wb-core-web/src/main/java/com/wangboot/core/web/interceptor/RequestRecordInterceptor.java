package com.wangboot.core.web.interceptor;

import com.wangboot.core.web.event.IEventPublisher;
import com.wangboot.core.web.event.RequestEvent;
import com.wangboot.core.web.event.RequestRecord;
import com.wangboot.core.web.utils.ServletUtils;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 请求记录器
 *
 * @author wwtg99
 */
@Slf4j
@Generated
public class RequestRecordInterceptor implements HandlerInterceptor, IEventPublisher {

  private static final String ATTR_START_TS = "start_ts";

  @Getter @Setter private ApplicationEventPublisher applicationEventPublisher;

  @Override
  public boolean preHandle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler)
      throws Exception {
    request.setAttribute(ATTR_START_TS, System.currentTimeMillis());
    ServletUtils.generateRequestId(request);
    return true;
  }

  @Override
  public void afterCompletion(
      @NonNull HttpServletRequest request,
      HttpServletResponse response,
      @NonNull Object handler,
      Exception ex)
      throws Exception {
    String requestId = ServletUtils.getRequestId(request);
    long startTs = (long) request.getAttribute(ATTR_START_TS);
    long duration = System.currentTimeMillis() - startTs;
    RequestRecord ro =
        RequestRecord.builder()
            .requestId(requestId)
            .duration(duration)
            .remoteIp(ServletUtils.getRemoteIp())
            .status(response.getStatus())
            .build();
    if (Objects.nonNull(ex)) {
      log.info(
          "# Request(ID: {}) {} {}{} From: {} ===> Throws exception: {} {} Duration: {} ms",
          requestId,
          request.getMethod(),
          request.getRequestURL(),
          StringUtils.hasText(request.getQueryString()) ? "?" + request.getQueryString() : "",
          ro.getRemoteIp(),
          ex.getClass().getName(),
          StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "",
          duration);
    } else {
      log.info(
          "# Request(ID: {}) {} {}{} From: {} ===> Response Status: {} Duration: {} ms",
          requestId,
          request.getMethod(),
          request.getRequestURL(),
          StringUtils.hasText(request.getQueryString()) ? "?" + request.getQueryString() : "",
          ro.getRemoteIp(),
          response.getStatus(),
          duration);
    }
    // 发布事件
    this.publishEvent(new RequestEvent(ro, request, response, ex));
  }
}
