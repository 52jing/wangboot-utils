package com.wangboot.core.auth.interceptor;

import com.wangboot.core.auth.AuthFlow;
import com.wangboot.core.auth.event.UserAuthDenyEvent;
import com.wangboot.core.auth.event.UserAuthDenyEventLog;
import com.wangboot.core.auth.exception.NotAuthenticatedException;
import com.wangboot.core.auth.utils.AuthUtils;
import com.wangboot.core.web.event.IEventPublisher;
import com.wangboot.core.web.utils.ServletUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 认证拦截器
 *
 * @author wwtg99
 */
@Slf4j
@RequiredArgsConstructor
@Generated
public class AuthenticationInterceptor implements HandlerInterceptor, IEventPublisher {

  public static final String AUTHENTICATION_HEADER = "Authorization";

  private final AuthFlow authFlow;

  @Getter @Setter private String tokenType = "Bearer";

  @Getter @Setter private ApplicationEventPublisher applicationEventPublisher;

  @Override
  public boolean preHandle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler)
      throws Exception {
    if (HttpMethod.OPTIONS.matches(request.getMethod())) {
      // 放行 OPTIONS
      return true;
    } else if (HttpMethod.TRACE.matches(request.getMethod())) {
      // 拒绝 TRACE
      this.publishEvent(
          new UserAuthDenyEvent(
              UserAuthDenyEventLog.builder()
                  .method(request.getMethod())
                  .path(request.getRequestURI())
                  .ip(ServletUtils.getRemoteIp(request))
                  .build(),
              request));
      return false;
    }
    if (AuthUtils.isLogin()) {
      log.info(
          "已认证用户 ID: {} {} 请求 {} {}",
          AuthUtils.getUserId(),
          AuthUtils.getUsername(),
          request.getMethod(),
          request.getRequestURI());
      return true;
    }
    String authHeader = request.getHeader(AUTHENTICATION_HEADER);
    if (!StringUtils.hasText(authHeader)) {
      throw new NotAuthenticatedException();
    }
    String token = authHeader.substring(this.tokenType.length() + 1);
    if (this.authFlow.authenticate(token)) {
      log.info(
          "已认证用户 ID: {} {} 请求 {} {}",
          AuthUtils.getUserId(),
          AuthUtils.getUsername(),
          request.getMethod(),
          request.getRequestURI());
      return true;
    }
    this.publishEvent(
        new UserAuthDenyEvent(
            UserAuthDenyEventLog.builder()
                .method(request.getMethod())
                .path(request.getRequestURI())
                .ip(ServletUtils.getRemoteIp(request))
                .message("not authenticated")
                .build(),
            request));
    throw new NotAuthenticatedException();
  }
}
