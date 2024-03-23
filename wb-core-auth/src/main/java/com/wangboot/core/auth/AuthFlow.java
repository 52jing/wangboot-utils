package com.wangboot.core.auth;

import com.wangboot.core.auth.authentication.AuthenticationManager;
import com.wangboot.core.auth.authentication.IAuthenticator;
import com.wangboot.core.auth.authorization.IAuthorizer;
import com.wangboot.core.auth.authorization.IAuthorizerService;
import com.wangboot.core.auth.context.*;
import com.wangboot.core.auth.event.LogStatus;
import com.wangboot.core.auth.event.UserEvent;
import com.wangboot.core.auth.event.UserEventLog;
import com.wangboot.core.auth.exception.InvalidTokenException;
import com.wangboot.core.auth.exception.LoginFailedException;
import com.wangboot.core.auth.exception.LogoutFailedException;
import com.wangboot.core.auth.exception.RefreshTokenFailedException;
import com.wangboot.core.auth.frontend.FrontendManager;
import com.wangboot.core.auth.frontend.IFrontendModel;
import com.wangboot.core.auth.frontend.IFrontendService;
import com.wangboot.core.auth.middleware.*;
import com.wangboot.core.auth.model.ILoginBody;
import com.wangboot.core.auth.model.ILogoutBody;
import com.wangboot.core.auth.model.IRefreshTokenBody;
import com.wangboot.core.auth.token.IAuthToken;
import com.wangboot.core.auth.token.ITokenManager;
import com.wangboot.core.auth.token.TokenPair;
import com.wangboot.core.auth.user.IUserModel;
import com.wangboot.core.auth.user.IUserService;
import com.wangboot.core.auth.utils.AuthUtils;
import com.wangboot.core.web.event.IEventPublisher;
import com.wangboot.core.web.utils.ServletUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 认证授权流程
 *
 * @author wwtg99
 */
@RequiredArgsConstructor
public class AuthFlow implements IEventPublisher {

  public static final String EVENT_LOGIN = "login";
  public static final String EVENT_LOGOUT = "logout";

  @NonNull private final AuthenticationManager authenticationManager;
  @NonNull private final FrontendManager frontendManager;
  @NonNull private final ITokenManager tokenManager;
  @NonNull private final IUserService userService;
  @NonNull private final IFrontendService frontendService;
  @NonNull private final IAuthorizerService authorizerService;
  @NonNull private final IAuthenticator authenticator;
  @NonNull private final List<ILoginMiddleware> loginMiddlewares;
  @NonNull private final List<IGenerateTokenMiddleware> generateTokenMiddlewares;
  @NonNull private final List<ILogoutMiddleware> logoutMiddlewares;
  @NonNull private final List<IRefreshTokenMiddleware> refreshTokenMiddlewares;
  @NonNull private final List<IFilterMiddleware> filterMiddlewares;

  @Getter @Setter private ApplicationEventPublisher applicationEventPublisher;

  public AuthFlow(
      @NonNull AuthenticationManager authenticationManager,
      @NonNull FrontendManager frontendManager,
      @NonNull ITokenManager tokenManager,
      @NonNull IUserService userService,
      @NonNull IFrontendService frontendService,
      @NonNull IAuthorizerService authorizerService,
      @NonNull IAuthenticator authenticator) {
    this(
        authenticationManager,
        frontendManager,
        tokenManager,
        userService,
        frontendService,
        authorizerService,
        authenticator,
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>());
  }

  public void addLoginMiddleware(@NonNull ILoginMiddleware middleware) {
    this.loginMiddlewares.add(middleware);
  }

  public void clearLoginMiddleware() {
    this.loginMiddlewares.clear();
  }

  public void addGenerateTokenMiddleware(@NonNull IGenerateTokenMiddleware middleware) {
    this.generateTokenMiddlewares.add(middleware);
  }

  public void clearGenerateTokenMiddleware() {
    this.generateTokenMiddlewares.clear();
  }

  public void addLogoutMiddleware(@NonNull ILogoutMiddleware middleware) {
    this.logoutMiddlewares.add(middleware);
  }

  public void clearLogoutMiddleware() {
    this.logoutMiddlewares.clear();
  }

  public void addRefreshTokenMiddleware(@NonNull IRefreshTokenMiddleware middleware) {
    this.refreshTokenMiddlewares.add(middleware);
  }

  public void clearRefreshTokenMiddleware() {
    this.refreshTokenMiddlewares.clear();
  }

  public void addFilterMiddleware(@NonNull IFilterMiddleware middleware) {
    this.filterMiddlewares.add(middleware);
  }

  public void clearFilterMiddleware() {
    this.filterMiddlewares.clear();
  }

  @Nullable
  public IAuthentication getAuthentication() {
    return Optional.ofNullable(AuthContextHolder.getContext())
        .flatMap(d -> Optional.ofNullable(d.getAuthentication()))
        .orElse(null);
  }

  public void setAuthentication(@Nullable IAuthentication authentication) {
    IAuthContext context = AuthContextHolder.getContext();
    if (Objects.nonNull(context)) {
      context.setAuthentication(authentication);
    }
  }

  @NonNull
  public ILoginUser getLoginUser() {
    IAuthentication authentication = this.getAuthentication();
    if (Objects.nonNull(authentication) && authentication instanceof ILoginUser) {
      return (ILoginUser) authentication;
    }
    return AuthUtils.ANONYMOUS;
  }

  public String getUserId() {
    return this.getLoginUser().getUser().getUserId();
  }

  /** 登录流程 */
  @NonNull
  public ILoginUser login(@NonNull ILoginBody body) {
    // 认证验证前中间件检查
    ILoginBody loginBody = body;
    for (ILoginMiddleware middleware : this.loginMiddlewares) {
      try {
        loginBody = middleware.beforeLogin(loginBody);
      } catch (RuntimeException e) {
        // 中间件抛出异常
        this.publishUserEvent(LogStatus.FAILED, EVENT_LOGIN, null, body, e.getMessage());
        throw e;
      }
      if (Objects.isNull(loginBody)) {
        // 中间件返回空
        this.publishUserEvent(
            LogStatus.FAILED,
            EVENT_LOGIN,
            null,
            body,
            middleware.getClass().getSimpleName() + " before authentication failed");
        throw new LoginFailedException();
      }
    }
    // 认证验证
    IUserModel userModel;
    try {
      userModel = this.authenticationManager.authenticate(loginBody);
    } catch (RuntimeException e) {
      // 认证抛出异常
      this.publishUserEvent(LogStatus.FAILED, EVENT_LOGIN, null, body, e.getMessage());
      throw e;
    }
    // 前端验证
    IFrontendModel frontendModel;
    try {
      frontendModel = this.frontendManager.validate(loginBody);
    } catch (RuntimeException e) {
      // 认证抛出异常
      this.publishUserEvent(LogStatus.FAILED, EVENT_LOGIN, null, body, e.getMessage());
      throw e;
    }
    if (Objects.isNull(frontendModel)) {
      frontendModel = AuthUtils.UNKNOWN_FRONTEND;
    }
    // 获取授权验证者
    IAuthorizer authorizer = this.authorizerService.getAuthorizer(userModel);
    // 生成认证上下文
    ILoginUser loginUser = new LoginUser(userModel, frontendModel, authorizer);
    // 认证验证后中间件检查
    for (ILoginMiddleware middleware : this.loginMiddlewares) {
      boolean ret;
      try {
        ret = middleware.afterLogin(loginBody, loginUser);
      } catch (RuntimeException e) {
        // 中间件抛出异常
        this.publishUserEvent(LogStatus.FAILED, EVENT_LOGIN, null, body, e.getMessage());
        throw e;
      }
      if (!ret) {
        // 中间件返回false
        this.publishUserEvent(
            LogStatus.FAILED,
            EVENT_LOGIN,
            null,
            body,
            middleware.getClass().getSimpleName() + " after authentication failed");
        throw new LoginFailedException();
      }
    }
    return loginUser;
  }

  /** 生成令牌对流程 */
  @Nullable
  public TokenPair generateTokenPair(@NonNull ILoginUser loginUser) {
    // 生成令牌对
    TokenPair tokenPair = this.tokenManager.generateTokenPair(loginUser);
    // 生成令牌对后中间件检查
    for (IGenerateTokenMiddleware middleware : this.generateTokenMiddlewares) {
      boolean ret = middleware.afterTokenGeneration(loginUser, tokenPair);
      if (!ret) {
        return null;
      }
    }
    return tokenPair;
  }

  /** 登录并生成令牌对流程 */
  @NonNull
  public TokenPair loginAndGenerateToken(@NonNull ILoginBody body) {
    ILoginUser loginUser = this.login(body);
    TokenPair tokenPair = this.generateTokenPair(loginUser);
    if (Objects.nonNull(tokenPair)) {
      // 认证成功
      this.publishUserEvent(LogStatus.SUCCESS, EVENT_LOGIN, loginUser, body, "");
      return tokenPair;
    } else {
      // 中间件返回false
      this.publishUserEvent(LogStatus.FAILED, EVENT_LOGIN, null, body, "token generation failed");
      throw new LoginFailedException();
    }
  }

  /** 登出流程 */
  public boolean logout(@NonNull ILogoutBody body, @NonNull ILoginUser loginUser) {
    ILogoutBody logoutBody = body;
    // 登出前中间件检查
    for (ILogoutMiddleware middleware : this.logoutMiddlewares) {
      try {
        logoutBody = middleware.beforeLogout(logoutBody, loginUser);
      } catch (RuntimeException e) {
        // 中间件抛出异常
        this.publishUserEvent(LogStatus.FAILED, EVENT_LOGOUT, loginUser, body, e.getMessage());
        throw e;
      }
      if (Objects.isNull(logoutBody)) {
        // 中间件返回空
        this.publishUserEvent(
            LogStatus.FAILED,
            EVENT_LOGOUT,
            loginUser,
            body,
            middleware.getClass().getSimpleName() + " before logout failed");
        throw new LogoutFailedException();
      }
    }
    // 登出
    boolean result = this.userService.logout(loginUser.getUser());
    if (!result) {
      this.publishUserEvent(LogStatus.FAILED, EVENT_LOGOUT, loginUser, body, "logout failed");
      throw new LogoutFailedException();
    }
    // 登出后中间件检查
    for (ILogoutMiddleware middleware : this.logoutMiddlewares) {
      boolean ret;
      try {
        ret = middleware.afterLogout(logoutBody, loginUser);
      } catch (RuntimeException e) {
        // 中间件抛出异常
        this.publishUserEvent(LogStatus.FAILED, EVENT_LOGOUT, loginUser, body, e.getMessage());
        throw e;
      }
      if (!ret) {
        // 中间件返回false
        this.publishUserEvent(
            LogStatus.FAILED,
            EVENT_LOGOUT,
            loginUser,
            body,
            middleware.getClass().getSimpleName() + " after logout failed");
        throw new LogoutFailedException();
      }
    }
    // 登出成功
    this.publishUserEvent(LogStatus.SUCCESS, EVENT_LOGOUT, loginUser, body, "");
    return true;
  }

  /** 刷新令牌流程 */
  @NonNull
  public ILoginUser refreshToken(@NonNull IRefreshTokenBody body) {
    IRefreshTokenBody refreshTokenBody = body;
    // 刷新令牌前中间件检查
    for (IRefreshTokenMiddleware middleware : this.refreshTokenMiddlewares) {
      refreshTokenBody = middleware.beforeRefreshToken(refreshTokenBody);
      if (Objects.isNull(refreshTokenBody)) {
        throw new RefreshTokenFailedException();
      }
    }
    // 提取刷新令牌
    IAuthToken refreshToken = this.tokenManager.parse(refreshTokenBody.getRefreshToken());
    if (Objects.isNull(refreshToken)) {
      throw new InvalidTokenException();
    }
    // 获取用户
    IUserModel userModel = this.userService.getUserModelById(refreshToken.getUserId());
    if (Objects.isNull(userModel)) {
      throw new InvalidTokenException();
    }
    // 获取前端
    IFrontendModel frontendModel =
        this.frontendService.getFrontendModelById(refreshToken.getFrontendId());
    if (Objects.isNull(frontendModel)) {
      throw new InvalidTokenException();
    }
    // 获取授权验证者
    IAuthorizer authorizer = this.authorizerService.getAuthorizer(userModel);
    // 重新生成认证上下文
    ILoginUser loginUser = new LoginUser(userModel, frontendModel, authorizer);
    // 刷新令牌后中间件检查
    for (IRefreshTokenMiddleware middleware : this.refreshTokenMiddlewares) {
      boolean ret = middleware.afterRefreshToken(refreshTokenBody, loginUser);
      if (!ret) {
        throw new RefreshTokenFailedException();
      }
    }
    return loginUser;
  }

  /** 刷新并生成令牌流程 */
  public TokenPair refreshAndGenerateToken(@NonNull IRefreshTokenBody body) {
    ILoginUser loginUser = this.refreshToken(body);
    TokenPair tokenPair = this.generateTokenPair(loginUser);
    if (Objects.nonNull(tokenPair)) {
      // 刷新成功
      return tokenPair;
    } else {
      // 中间件返回false
      throw new RefreshTokenFailedException();
    }
  }

  /** 认证流程 */
  public boolean authenticate(String token) {
    String token1 = token;
    // 认证提取令牌前处理
    for (IFilterMiddleware middleware : this.filterMiddlewares) {
      token1 = middleware.beforeParseToken(token1);
      if (!StringUtils.hasText(token1)) {
        // 中间件返回空
        return false;
      }
    }
    // 提取令牌
    IAuthToken authToken = this.tokenManager.parse(token1);
    if (Objects.isNull(authToken)) {
      return false;
    }
    // 认证提取令牌后处理
    for (IFilterMiddleware middleware : this.filterMiddlewares) {
      boolean ret = middleware.afterParseToken(token1, authToken);
      if (!ret) {
        return false;
      }
    }
    // 认证
    ILoginUser loginUser = this.authenticator.authenticate(authToken);
    if (Objects.isNull(loginUser)) {
      return false;
    }
    // 认证后处理
    for (IFilterMiddleware middleware : this.filterMiddlewares) {
      boolean ret = middleware.afterAuthentication(token1, authToken, loginUser);
      if (!ret) {
        return false;
      }
    }
    this.setAuthentication(loginUser);
    return true;
  }

  @Nullable
  public HttpServletRequest getRequest() {
    return ServletUtils.getRequest();
  }

  /** 发布用户事件 */
  public void publishUserEvent(
      @NonNull LogStatus status,
      String event,
      @Nullable ILoginUser loginUser,
      @Nullable Object body,
      String message) {
    this.publishEvent(
        new UserEvent(
            UserEventLog.builder()
                .status(status)
                .event(Optional.ofNullable(event).orElse(""))
                .loginUser(loginUser)
                .body(body)
                .message(Optional.ofNullable(message).orElse(""))
                .ip(ServletUtils.getRemoteIp(getRequest()))
                .ua(ServletUtils.getUserAgent(getRequest()))
                .createdBy(getUserId())
                .build(),
            getRequest()));
  }
}
