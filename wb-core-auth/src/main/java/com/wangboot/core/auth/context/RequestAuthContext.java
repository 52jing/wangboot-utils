package com.wangboot.core.auth.context;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 基于请求的认证上下文<br>
 * 将认证信息保存于每个请求属性中
 *
 * @author wwtg99
 */
public class RequestAuthContext implements IAuthContext {

  private static final String AUTHENTICATION_ATTR = "authentication";

  @Override
  @Nullable
  public IAuthentication getAuthentication() {
    HttpServletRequest request = RequestAuthContext.getRequest();
    if (Objects.nonNull(request)) {
      Object obj = request.getAttribute(AUTHENTICATION_ATTR);
      if (obj instanceof IAuthentication) {
        return (IAuthentication) obj;
      }
    }
    return null;
  }

  @Override
  public void setAuthentication(@Nullable IAuthentication authentication) {
    HttpServletRequest request = RequestAuthContext.getRequest();
    if (Objects.nonNull(request)) {
      request.setAttribute(AUTHENTICATION_ATTR, authentication);
    }
  }

  @Nullable
  private static HttpServletRequest getRequest() {
    RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
    return Objects.isNull(attributes) ? null : ((ServletRequestAttributes) attributes).getRequest();
  }
}
