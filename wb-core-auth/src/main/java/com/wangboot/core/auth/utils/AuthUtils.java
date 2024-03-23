package com.wangboot.core.auth.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.wangboot.core.auth.AuthFlow;
import com.wangboot.core.auth.authorization.IAuthorizationResource;
import com.wangboot.core.auth.authorization.IAuthorizer;
import com.wangboot.core.auth.authorization.IAuthorizerService;
import com.wangboot.core.auth.authorization.authorizer.GrantAllAuthorizer;
import com.wangboot.core.auth.context.*;
import com.wangboot.core.auth.exception.PermissionDeniedException;
import com.wangboot.core.auth.frontend.IFrontendModel;
import com.wangboot.core.auth.frontend.IFrontendService;
import com.wangboot.core.auth.frontend.impl.MockFrontend;
import com.wangboot.core.auth.user.IUserModel;
import com.wangboot.core.auth.user.IUserService;
import com.wangboot.core.auth.user.impl.MockUser;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 认证工具类
 *
 * @author wwtg99
 */
@Generated
@Slf4j
public class AuthUtils {

  /** 匿名用户 */
  public static final IUserModel ANONYMOUS_USER =
      new MockUser("", "Anonymous", "", false, false, false, false, null);

  /** 未知前端 */
  public static final IFrontendModel UNKNOWN_FRONTEND =
      new MockFrontend("", "Unknown", "", false, false);

  /** 允许所有权限 */
  public static final IAuthorizer ALLOW_ALL = new GrantAllAuthorizer(true);

  /** 拒绝所有权限 */
  public static final IAuthorizer DENY_ALL = new GrantAllAuthorizer(false);

  /** 匿名登录 */
  public static final ILoginUser ANONYMOUS =
      new LoginUser(ANONYMOUS_USER, UNKNOWN_FRONTEND, DENY_ALL);

  private static final String JOIN_SEP = ",";

  private AuthUtils() {}

  @NonNull
  public static AuthFlow getAuthFlow() {
    return Optional.ofNullable(SpringUtil.getBean(AuthFlow.class))
        .orElseThrow(() -> new NoSuchBeanDefinitionException("No AuthFlow is found!"));
  }

  @NonNull
  public static IUserService getUserService() {
    return Optional.ofNullable(SpringUtil.getBean(IUserService.class))
        .orElseThrow(() -> new NoSuchBeanDefinitionException("No IUserService is found!"));
  }

  @NonNull
  public static IFrontendService getFrontendService() {
    return Optional.ofNullable(SpringUtil.getBean(IFrontendService.class))
        .orElseThrow(() -> new NoSuchBeanDefinitionException("No IFrontendService is found!"));
  }

  @NonNull
  public static IAuthorizerService getAuthorizerService() {
    return Optional.ofNullable(SpringUtil.getBean(IAuthorizerService.class))
        .orElseThrow(() -> new NoSuchBeanDefinitionException("No IAuthorizerService is found!"));
  }

  @Nullable
  public static IAuthContext getAuthContext() {
    return AuthContextHolder.getContext();
  }

  @Nullable
  public static IAuthentication getAuthentication() {
    IAuthContext context = getAuthContext();
    if (Objects.nonNull(context)) {
      return context.getAuthentication();
    }
    return null;
  }

  @Nullable
  public static ILoginUser getLoginUser() {
    IAuthentication authentication = getAuthentication();
    if (authentication instanceof ILoginUser) {
      return (ILoginUser) authentication;
    }
    return null;
  }

  public static boolean isLogin() {
    IAuthentication authentication = getAuthentication();
    if (Objects.nonNull(authentication)) {
      return authentication.isAuthenticated();
    }
    return false;
  }

  @Nullable
  public static IUserModel getUserModel() {
    ILoginUser loginUser = getLoginUser();
    return Objects.nonNull(loginUser) ? loginUser.getUser() : null;
  }

  public static boolean isSuperuser() {
    ILoginUser loginUser = getLoginUser();
    return Objects.nonNull(loginUser) && loginUser.isSuperuser();
  }

  public static boolean isStaff() {
    ILoginUser loginUser = getLoginUser();
    return Objects.nonNull(loginUser) && loginUser.isStaff();
  }

  public static String getUserId() {
    IUserModel userModel = getUserModel();
    return Objects.nonNull(userModel) ? userModel.getUserId() : "";
  }

  public static String getUsername() {
    IUserModel userModel = getUserModel();
    return Objects.nonNull(userModel) ? userModel.getUsername() : "";
  }

  @Nullable
  public static IFrontendModel getFrontendModel() {
    ILoginUser loginUser = getLoginUser();
    return Objects.nonNull(loginUser) ? loginUser.getFrontend() : null;
  }

  @Nullable
  public static IAuthorizer getAuthorizer() {
    ILoginUser loginUser = getLoginUser();
    return Objects.nonNull(loginUser) ? loginUser.getAuthorizer() : null;
  }

  /** 当前用户是否有权限，无权限则抛出异常 */
  public static void checkAuthority(@Nullable IAuthorizationResource resource) {
    IAuthorizer authorizer = getAuthorizer();
    if (Objects.isNull(authorizer)) {
      log.warn("无授权验证者");
      throw new PermissionDeniedException();
    }
    if (!authorizer.authorize(resource)) {
      log.warn(
          "权限不满足：ID {} 缺少权限，需要权限 {}",
          getUserId(),
          Objects.nonNull(resource) ? resource.getResourceName() : "");
      throw new PermissionDeniedException();
    }
  }

  /** 当前用户是否具有任一权限，无权限则抛出异常 */
  public static void checkAnyAuthorities(IAuthorizationResource... resources) {
    IAuthorizer authorizer = getAuthorizer();
    if (Objects.isNull(authorizer)) {
      log.warn("无授权验证者");
      throw new PermissionDeniedException();
    }
    if (!authorizer.authorizeAny(resources)) {
      log.warn(
          "权限不满足：ID {} 缺少权限，需要任一权限 {}",
          getUserId(),
          Arrays.stream(resources)
              .map(IAuthorizationResource::getResourceName)
              .collect(Collectors.joining(JOIN_SEP)));
      throw new PermissionDeniedException();
    }
  }

  /** 当前用户是否具有所有权限，无权限则抛出异常 */
  public static void checkAllAuthorities(IAuthorizationResource... resources) {
    IAuthorizer authorizer = getAuthorizer();
    if (Objects.isNull(authorizer)) {
      log.warn("无授权验证者");
      throw new PermissionDeniedException();
    }
    if (!authorizer.authorizeAll(resources)) {
      log.warn(
          "权限不满足：ID {} 缺少权限，需要所有权限 {}",
          getUserId(),
          Arrays.stream(resources)
              .map(IAuthorizationResource::getResourceName)
              .collect(Collectors.joining(JOIN_SEP)));
      throw new PermissionDeniedException();
    }
  }
}
