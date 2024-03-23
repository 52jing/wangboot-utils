package com.wangboot.core.auth.interceptor;

import cn.hutool.extra.spring.SpringUtil;
import com.wangboot.core.auth.annotation.*;
import com.wangboot.core.auth.authorization.resource.ApiResource;
import com.wangboot.core.auth.event.UserAuthDenyEvent;
import com.wangboot.core.auth.event.UserAuthDenyEventLog;
import com.wangboot.core.auth.exception.PermissionDeniedException;
import com.wangboot.core.auth.utils.AuthUtils;
import com.wangboot.core.web.event.IEventPublisher;
import com.wangboot.core.web.utils.ServletUtils;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 权限拦截器
 *
 * @author wwtg99
 */
@Slf4j
@Generated
public class PermissionInterceptor implements HandlerInterceptor, IEventPublisher {

  private static final String DEL = ",";

  @Getter @Setter private ApplicationEventPublisher applicationEventPublisher;

  @Override
  public boolean preHandle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler)
      throws Exception {
    if (!AuthUtils.isSuperuser() && handler instanceof HandlerMethod) {
      HandlerMethod handlerMethod = (HandlerMethod) handler;
      Object controller;
      if (handlerMethod.getBean() instanceof String) {
        controller = SpringUtil.getBean(handlerMethod.getBean().toString());
      } else {
        controller = handlerMethod.getBean();
      }
      if (Objects.isNull(controller)) {
        return false;
      }
      this.checkSuperuser(request, handlerMethod, controller);
      this.checkStaff(request, handlerMethod, controller);
      this.checkAuthority(request, handlerMethod, controller);
      this.checkRestPermission(request, handlerMethod, controller);
    }
    return true;
  }

  private void checkSuperuser(
      @NonNull HttpServletRequest request,
      @NonNull HandlerMethod handlerMethod,
      @NonNull Object controller) {
    RequireSuperuser anno = handlerMethod.getMethodAnnotation(RequireSuperuser.class);
    if (Objects.isNull(anno)) {
      // 方法不存在注解则查找类
      anno = controller.getClass().getAnnotation(RequireSuperuser.class);
      if (Objects.isNull(anno)) {
        // 不存在注解
        return;
      }
    }
    // 要求超级管理员
    if (anno.value() && !AuthUtils.isSuperuser()) {
      log.warn("权限不满足：非超级管理员 (ID: {})", AuthUtils.getUserId());
      String msg = "Superuser is required";
      this.publishEvent(
          new UserAuthDenyEvent(
              UserAuthDenyEventLog.builder()
                  .method(request.getMethod())
                  .path(request.getRequestURI())
                  .ip(ServletUtils.getRemoteIp(request))
                  .message(msg)
                  .build(),
              request));
      throw new PermissionDeniedException(msg);
    }
  }

  private void checkStaff(
      @NonNull HttpServletRequest request,
      @NonNull HandlerMethod handlerMethod,
      @NonNull Object controller) {
    RequireStaff anno = handlerMethod.getMethodAnnotation(RequireStaff.class);
    if (Objects.isNull(anno)) {
      // 方法不存在注解则查找类
      anno = controller.getClass().getAnnotation(RequireStaff.class);
      if (Objects.isNull(anno)) {
        // 不存在注解
        return;
      }
    }
    // 要求内部用户
    if (anno.value() && !AuthUtils.isStaff()) {
      log.warn("权限不满足：非内部用户 (ID: {})", AuthUtils.getUserId());
      String msg = "Staff is required";
      this.publishEvent(
          new UserAuthDenyEvent(
              UserAuthDenyEventLog.builder()
                  .method(request.getMethod())
                  .path(request.getRequestURI())
                  .ip(ServletUtils.getRemoteIp(request))
                  .message(msg)
                  .build(),
              request));
      throw new PermissionDeniedException(msg);
    }
  }

  private void checkAuthority(
      @NonNull HttpServletRequest request,
      @NonNull HandlerMethod handlerMethod,
      @NonNull Object controller) {
    RequireAuthority anno = handlerMethod.getMethodAnnotation(RequireAuthority.class);
    if (Objects.isNull(anno)) {
      // 方法不存在注解则查找类
      anno = controller.getClass().getAnnotation(RequireAuthority.class);
      if (Objects.isNull(anno)) {
        // 不存在注解
        return;
      }
    }
    if (anno.value().length > 0) {
      ApiResource[] resources =
          Arrays.stream(anno.value())
              .map(ApiResource::of)
              .filter(Objects::nonNull)
              .collect(Collectors.toList())
              .toArray(new ApiResource[] {});
      if (anno.all()) {
        // 要求所有权限
        if (!AuthUtils.getAuthorizer().authorizeAll(resources)) {
          log.warn("权限不满足：ID {} 缺少权限，需要所有权限 {}", AuthUtils.getUserId(), anno.value());
          String msg = "Require all authorities" + String.join(DEL, anno.value());
          this.publishEvent(
              new UserAuthDenyEvent(
                  UserAuthDenyEventLog.builder()
                      .method(request.getMethod())
                      .path(request.getRequestURI())
                      .ip(ServletUtils.getRemoteIp(request))
                      .message(msg)
                      .build(),
                  request));
          throw new PermissionDeniedException(msg);
        }
      } else {
        // 要求任一权限
        if (!AuthUtils.getAuthorizer().authorizeAny(resources)) {
          log.warn("权限不满足：ID {} 缺少权限，要求任一权限 {}", AuthUtils.getUserId(), anno.value());
          String msg = "Require any authorities " + String.join(DEL, anno.value());
          this.publishEvent(
              new UserAuthDenyEvent(
                  UserAuthDenyEventLog.builder()
                      .method(request.getMethod())
                      .path(request.getRequestURI())
                      .ip(ServletUtils.getRemoteIp(request))
                      .message(msg)
                      .build(),
                  request));
          throw new PermissionDeniedException(msg);
        }
      }
    }
  }

  private void checkRestPermission(
      @NonNull HttpServletRequest request,
      @NonNull HandlerMethod handlerMethod,
      @NonNull Object controller) {
    RestPermissionPrefix prefix = controller.getClass().getAnnotation(RestPermissionPrefix.class);
    RestPermissionAction action = handlerMethod.getMethodAnnotation(RestPermissionAction.class);
    if (Objects.nonNull(prefix) && Objects.nonNull(action)) {
      // 类和方法上的注解都存在才生效
      if (StringUtils.hasText(prefix.name()) && StringUtils.hasText(action.value())) {
        // 指定了 name 和 action 才有效
        ApiResource resource = new ApiResource(prefix.group(), prefix.name(), action.value());
        if (!AuthUtils.getAuthorizer().authorize(resource)) {
          log.warn(
              "权限不满足：ID {} 缺少权限，需要 Rest 权限 {}", AuthUtils.getUserId(), resource.getResourceName());
          String msg = "Require rest authority " + resource.getResourceName();
          this.publishEvent(
              new UserAuthDenyEvent(
                  UserAuthDenyEventLog.builder()
                      .method(request.getMethod())
                      .path(request.getRequestURI())
                      .ip(ServletUtils.getRemoteIp(request))
                      .message(msg)
                      .build(),
                  request));
          throw new PermissionDeniedException(msg);
        }
      }
    }
  }
}
