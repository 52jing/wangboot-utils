package com.wangboot.core.auth;

import cn.hutool.core.util.RandomUtil;
import com.wangboot.core.auth.authentication.AuthenticationManager;
import com.wangboot.core.auth.authentication.IAuthenticationProvider;
import com.wangboot.core.auth.authentication.IAuthenticator;
import com.wangboot.core.auth.authentication.authenticator.TokenAuthenticator;
import com.wangboot.core.auth.authentication.provider.UsernamePasswordAuthenticationProvider;
import com.wangboot.core.auth.authorization.IAuthorizerService;
import com.wangboot.core.auth.authorization.authorizer.GrantAllAuthorizer;
import com.wangboot.core.auth.authorization.authorizer.SimpleAuthorizer;
import com.wangboot.core.auth.authorization.resource.ApiResource;
import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.context.LoginUser;
import com.wangboot.core.auth.context.RequestAuthContext;
import com.wangboot.core.auth.exception.*;
import com.wangboot.core.auth.frontend.FrontendManager;
import com.wangboot.core.auth.frontend.IFrontendModel;
import com.wangboot.core.auth.frontend.IFrontendService;
import com.wangboot.core.auth.frontend.impl.MockFrontend;
import com.wangboot.core.auth.frontend.provider.SimpleFrontendServiceProvider;
import com.wangboot.core.auth.middleware.filter.AccessTokenTypeFilter;
import com.wangboot.core.auth.middleware.generatetoken.LoginRestrictionGuard;
import com.wangboot.core.auth.middleware.login.StaffOnlyCheck;
import com.wangboot.core.auth.middleware.logout.UserTokenValidation;
import com.wangboot.core.auth.middleware.refreshtoken.RefreshTokenTypeCheck;
import com.wangboot.core.auth.model.*;
import com.wangboot.core.auth.security.LoginRestriction;
import com.wangboot.core.auth.security.LoginRestrictionStrategy;
import com.wangboot.core.auth.testcom.PlainPasswordEncoder;
import com.wangboot.core.auth.testcom.TestFrontendService;
import com.wangboot.core.auth.testcom.TestUserService;
import com.wangboot.core.auth.token.IAuthToken;
import com.wangboot.core.auth.token.TokenPair;
import com.wangboot.core.auth.token.jwt.JwtTokenManager;
import com.wangboot.core.auth.token.session.SessionTokenManager;
import com.wangboot.core.auth.user.IUserModel;
import com.wangboot.core.auth.user.IUserService;
import com.wangboot.core.auth.user.impl.MockUser;
import com.wangboot.core.auth.utils.AuthUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@DisplayName("认证测试")
public class AuthTest {

  @Test
  public void testContext() {
    String userId = "1";
    String username = RandomUtil.randomString(8);
    String frontendId = "99";
    String frontendName = RandomUtil.randomString(6);
    IUserModel u1 = new MockUser(userId, username, RandomUtil.randomString(6));
    IUserModel u2 = new MockUser(userId, username, RandomUtil.randomString(6));
    IUserModel u3 = new MockUser(userId, RandomUtil.randomString(6), RandomUtil.randomString(6));
    Assertions.assertEquals(u1, u2);
    Assertions.assertNotEquals(u1, u3);
    Assertions.assertEquals(username, u1.toString());
    IFrontendModel f1 =
        new MockFrontend(frontendId, frontendName, RandomUtil.randomString(4), false, false);
    IFrontendModel f2 =
        new MockFrontend(frontendId, frontendName, RandomUtil.randomString(4), false, false);
    IFrontendModel f3 =
        new MockFrontend(
            frontendId, RandomUtil.randomString(6), RandomUtil.randomString(4), false, false);
    Assertions.assertEquals(f1, f2);
    Assertions.assertNotEquals(f1, f3);
    Assertions.assertEquals(frontendName, f1.toString());
    ILoginUser loginUser =
        new LoginUser(
            new MockUser(userId, username, ""),
            new MockFrontend(frontendId, RandomUtil.randomString(6), "WEB", false, false),
            AuthUtils.ALLOW_ALL);
    Assertions.assertEquals("", loginUser.getCredentials());
    Assertions.assertEquals(username, loginUser.getPrincipal().toString());
    Assertions.assertEquals(username, loginUser.getName());
    Assertions.assertFalse(loginUser.isSuperuser());
    Assertions.assertFalse(loginUser.isStaff());
    Assertions.assertTrue(loginUser.isLogin());
    Assertions.assertTrue(loginUser.isUserValid());
    RequestAuthContext context = new RequestAuthContext();
    Assertions.assertNull(context.getAuthentication());
  }

  @Test
  public void testSessionToken() {
    String issuer = RandomUtil.randomString(6);
    SessionTokenManager tokenManager = new SessionTokenManager(issuer, 1, 1);
    String userId = "1";
    String username = RandomUtil.randomString(8);
    String frontendId = "99";
    ILoginUser loginUser =
        new LoginUser(
            new MockUser(userId, username, ""),
            new MockFrontend(frontendId, RandomUtil.randomString(6), "WEB", false, false),
            AuthUtils.ALLOW_ALL);
    Assertions.assertNull(tokenManager.generateToken("", loginUser, 1));
    IAuthToken accessToken = tokenManager.generateAccessToken(loginUser);
    Assertions.assertNotNull(accessToken);
    Assertions.assertTrue(StringUtils.hasText(accessToken.toString()));
    Assertions.assertEquals(userId, accessToken.getUserId());
    Assertions.assertEquals(frontendId, accessToken.getFrontendId());
    Assertions.assertEquals(issuer, accessToken.getIssuer());
    Assertions.assertEquals(TokenPair.ACCESS_TOKEN_TYPE, accessToken.getTokenType());
    Assertions.assertTrue(tokenManager.validate(accessToken));
    IAuthToken accessToken2 = tokenManager.parse(accessToken.getString());
    Assertions.assertEquals(accessToken, accessToken2);
    IAuthToken refreshToken = tokenManager.generateRefreshToken(loginUser);
    Assertions.assertNotNull(refreshToken);
    Assertions.assertTrue(StringUtils.hasText(refreshToken.toString()));
    Assertions.assertEquals(userId, refreshToken.getUserId());
    Assertions.assertEquals(frontendId, refreshToken.getFrontendId());
    Assertions.assertEquals(issuer, refreshToken.getIssuer());
    Assertions.assertEquals(TokenPair.REFRESH_TOKEN_TYPE, refreshToken.getTokenType());
    Assertions.assertTrue(tokenManager.validate(refreshToken));
    Assertions.assertFalse(tokenManager.validate(null));
    IAuthToken refreshToken2 = tokenManager.parse(refreshToken.getString());
    Assertions.assertEquals(refreshToken, refreshToken2);
    Assertions.assertNull(tokenManager.parse(""));
  }

  @Test
  public void testJwtToken() {
    String issuer = RandomUtil.randomString(6);
    String secret = RandomUtil.randomString(32);
    JwtTokenManager tokenManager = new JwtTokenManager(secret, issuer, 1, 1);
    String userId = "1";
    String username = RandomUtil.randomString(8);
    String frontendId = "99";
    ILoginUser loginUser =
        new LoginUser(
            new MockUser(userId, username, ""),
            new MockFrontend(frontendId, RandomUtil.randomString(6), "WEB", false, false),
            AuthUtils.ALLOW_ALL);
    Assertions.assertNull(tokenManager.generateToken("", loginUser, 1));
    IAuthToken accessToken = tokenManager.generateAccessToken(loginUser);
    Assertions.assertNotNull(accessToken);
    Assertions.assertTrue(StringUtils.hasText(accessToken.toString()));
    Assertions.assertEquals(userId, accessToken.getUserId());
    Assertions.assertEquals(frontendId, accessToken.getFrontendId());
    Assertions.assertEquals(issuer, accessToken.getIssuer());
    Assertions.assertEquals(TokenPair.ACCESS_TOKEN_TYPE, accessToken.getTokenType());
    Assertions.assertTrue(tokenManager.validate(accessToken));
    IAuthToken accessToken2 = tokenManager.parse(accessToken.getString());
    Assertions.assertEquals(accessToken, accessToken2);
    IAuthToken refreshToken = tokenManager.generateRefreshToken(loginUser);
    Assertions.assertNotNull(refreshToken);
    Assertions.assertTrue(StringUtils.hasText(refreshToken.toString()));
    Assertions.assertEquals(userId, refreshToken.getUserId());
    Assertions.assertEquals(frontendId, refreshToken.getFrontendId());
    Assertions.assertEquals(issuer, refreshToken.getIssuer());
    Assertions.assertEquals(TokenPair.REFRESH_TOKEN_TYPE, refreshToken.getTokenType());
    Assertions.assertTrue(tokenManager.validate(refreshToken));
    Assertions.assertFalse(tokenManager.validate(null));
    IAuthToken refreshToken2 = tokenManager.parse(refreshToken.getString());
    Assertions.assertEquals(refreshToken, refreshToken2);
    Assertions.assertNull(tokenManager.parse(""));
  }

  @Test
  @SneakyThrows
  public void testAuthentication() {
    IUserService userService = new TestUserService();
    PasswordEncoder passwordEncoder = new PlainPasswordEncoder();
    IAuthenticationProvider provider =
        new UsernamePasswordAuthenticationProvider(userService, passwordEncoder);
    AuthenticationManager manager = new AuthenticationManager();
    String usernamePasswordProvider = "username_password";
    Assertions.assertNull(manager.getProvider(usernamePasswordProvider));
    manager.addProvider(usernamePasswordProvider, provider);
    Assertions.assertEquals(provider, manager.getProvider(usernamePasswordProvider));
    Assertions.assertNull(manager.getProvider(""));
    Assertions.assertTrue(manager.getProviderList().size() > 0);
    ILoginBody loginBody = new LoginBody();
    Assertions.assertThrows(
        UsernamePasswordMismatchException.class, () -> provider.authenticate(loginBody));
    loginBody.setUsername("not_exist");
    loginBody.setPassword("111");
    Assertions.assertThrows(
        NonExistsAccountException.class, () -> provider.authenticate(loginBody));
    loginBody.setUsername("expired");
    Assertions.assertThrows(ExpiredAccountException.class, () -> provider.authenticate(loginBody));
    loginBody.setUsername("locked");
    Assertions.assertThrows(LockedAccountException.class, () -> provider.authenticate(loginBody));
    loginBody.setUsername("blocked");
    Assertions.assertThrows(InvalidAccountException.class, () -> provider.authenticate(loginBody));
    loginBody.setUsername("user1");
    Assertions.assertThrows(
        UsernamePasswordMismatchException.class, () -> provider.authenticate(loginBody));
    loginBody.setPassword("123456");
    IUserModel userModel = provider.authenticate(loginBody);
    Assertions.assertNotNull(userModel);
    Assertions.assertFalse(userModel.checkSuperuser());
    Assertions.assertFalse(userModel.checkStaff());
    loginBody.setLoginType(usernamePasswordProvider);
    userModel = manager.authenticate(loginBody);
    Assertions.assertNotNull(userModel);
    loginBody.setLoginType("non_exist");
    Assertions.assertThrows(LoginFailedException.class, () -> manager.authenticate(loginBody));
  }

  @Test
  public void testAuthorization() {
    String userId = "1";
    String username = RandomUtil.randomString(8);
    // 具有权限 1、2、3，没有权限 4、5、6
    List<ApiResource> authorities = new ArrayList<>();
    ApiResource resource1 =
        new ApiResource(
            RandomUtil.randomString(6), RandomUtil.randomString(5), RandomUtil.randomString(4));
    ApiResource resource2 =
        new ApiResource(
            RandomUtil.randomString(6), RandomUtil.randomString(6), RandomUtil.randomString(4));
    ApiResource resource3 =
        new ApiResource(
            RandomUtil.randomString(6), RandomUtil.randomString(7), RandomUtil.randomString(4));
    authorities.add(resource1);
    authorities.add(resource2);
    authorities.add(resource3);
    ApiResource resource4 =
        new ApiResource(
            RandomUtil.randomString(7), RandomUtil.randomString(6), RandomUtil.randomString(5));
    ApiResource resource5 =
        new ApiResource(
            RandomUtil.randomString(8), RandomUtil.randomString(6), RandomUtil.randomString(5));
    ApiResource resource6 =
        new ApiResource(
            RandomUtil.randomString(9), RandomUtil.randomString(6), RandomUtil.randomString(5));
    GrantAllAuthorizer authorizer1 = new GrantAllAuthorizer(false);
    Assertions.assertFalse(authorizer1.authorize(resource1));
    Assertions.assertFalse(authorizer1.authorize(resource2));
    Assertions.assertFalse(authorizer1.authorize(resource3));
    Assertions.assertFalse(authorizer1.authorize(resource4));
    Assertions.assertFalse(authorizer1.authorize(resource5));
    Assertions.assertFalse(authorizer1.authorize(resource6));
    Assertions.assertFalse(authorizer1.authorizeAny(resource1));
    Assertions.assertFalse(authorizer1.authorizeAny(resource6));
    Assertions.assertFalse(authorizer1.authorizeAll(resource1));
    Assertions.assertFalse(authorizer1.authorizeAll(resource6));
    Assertions.assertTrue(authorizer1.getAuthorities().isEmpty());
    GrantAllAuthorizer authorizer2 = new GrantAllAuthorizer(true);
    Assertions.assertTrue(authorizer2.authorize(resource1));
    Assertions.assertTrue(authorizer2.authorize(resource2));
    Assertions.assertTrue(authorizer2.authorize(resource3));
    Assertions.assertTrue(authorizer2.authorize(resource4));
    Assertions.assertTrue(authorizer2.authorize(resource5));
    Assertions.assertTrue(authorizer2.authorize(resource6));
    Assertions.assertTrue(authorizer2.authorizeAny(resource1));
    Assertions.assertTrue(authorizer2.authorizeAny(resource6));
    Assertions.assertTrue(authorizer2.authorizeAll(resource1));
    Assertions.assertTrue(authorizer2.authorizeAll(resource6));
    Assertions.assertTrue(authorizer2.getAuthorities().isEmpty());
    IUserModel userModel = new MockUser(userId, username, "");
    SimpleAuthorizer authorizer3 = new SimpleAuthorizer(userModel, authorities);
    Assertions.assertFalse(authorizer3.authorize(null));
    Assertions.assertTrue(authorizer3.authorize(resource1));
    Assertions.assertTrue(authorizer3.authorize(resource2));
    Assertions.assertTrue(authorizer3.authorize(resource3));
    Assertions.assertFalse(authorizer3.authorize(resource4));
    Assertions.assertFalse(authorizer3.authorize(resource5));
    Assertions.assertFalse(authorizer3.authorize(resource6));
    Assertions.assertTrue(authorizer3.authorizeAny(resource1, resource2));
    Assertions.assertTrue(authorizer3.authorizeAny(resource1, resource6));
    Assertions.assertTrue(authorizer3.authorizeAll(resource1, resource2));
    Assertions.assertFalse(authorizer3.authorizeAll(resource1, resource6));
  }

  @Test
  public void testFrontend() {
    FrontendManager manager = new FrontendManager();
    IFrontendService frontendService = new TestFrontendService();
    manager.addProvider(new SimpleFrontendServiceProvider(frontendService));
    IFrontendBody body = new LoginBody();
    body.setFrontendId("");
    Assertions.assertThrows(NonExistsFrontendException.class, () -> manager.validate(body));
    body.setFrontendId(RandomUtil.randomString(6));
    Assertions.assertThrows(NonExistsFrontendException.class, () -> manager.validate(body));
    String fid = "1";
    body.setFrontendId(fid);
    IFrontendModel model = manager.validate(body);
    Assertions.assertNotNull(model);
    Assertions.assertEquals(fid, model.getId());
    Assertions.assertFalse(model.staffOnly());
    Assertions.assertTrue(model.allowRegister());
  }

  @Test
  @SneakyThrows
  public void testAuthFlow() {
    IUserService userService = new TestUserService();
    IFrontendService frontendService = new TestFrontendService();
    IAuthorizerService authorizerService = new TestUserService();
    PasswordEncoder passwordEncoder = new PlainPasswordEncoder();
    AuthenticationManager authenticationManager = new AuthenticationManager();
    String usernamePasswordProvider = "username_password";
    authenticationManager.addProvider(
        usernamePasswordProvider,
        new UsernamePasswordAuthenticationProvider(userService, passwordEncoder));
    FrontendManager frontendManager = new FrontendManager();
    frontendManager.addProvider(new SimpleFrontendServiceProvider(frontendService));
    String issuer = RandomUtil.randomString(6);
    SessionTokenManager tokenManager = new SessionTokenManager(issuer, 1, 1);
    IAuthenticator authenticator =
        new TokenAuthenticator(userService, frontendService, authorizerService);
    AuthFlow authFlow =
        new AuthFlow(
            authenticationManager,
            frontendManager,
            tokenManager,
            userService,
            frontendService,
            authorizerService,
            authenticator);
    Assertions.assertNull(authFlow.getAuthentication());
    // login
    authFlow.addLoginMiddleware(new StaffOnlyCheck());
    LoginRestriction loginRestriction = new LoginRestriction();
    authFlow.addGenerateTokenMiddleware(
        new LoginRestrictionGuard(LoginRestrictionStrategy.NO_RESTRICTION, loginRestriction));
    LoginBody loginRequestBody = new LoginBody();
    loginRequestBody.setUsername(RandomUtil.randomString(6));
    Assertions.assertThrows(
        UsernamePasswordMismatchException.class, () -> authFlow.login(loginRequestBody));
    loginRequestBody.setUsername("admin");
    loginRequestBody.setPassword("123456");
    loginRequestBody.setFrontendId("1");
    TokenPair tokenPair = authFlow.loginAndGenerateToken(loginRequestBody);
    Assertions.assertNotNull(tokenPair.getAccessToken());
    Assertions.assertNotNull(tokenPair.getRefreshToken());
    // filter
    authFlow.addFilterMiddleware(new AccessTokenTypeFilter());
    Assertions.assertTrue(authFlow.authenticate(tokenPair.getAccessToken().getString()));
    Assertions.assertFalse(authFlow.authenticate(tokenPair.getRefreshToken().getString()));
    // refresh
    authFlow.addRefreshTokenMiddleware(new RefreshTokenTypeCheck(tokenManager));
    IRefreshTokenBody refreshTokenBody = new RefreshTokenBody();
    refreshTokenBody.setRefreshToken(tokenPair.getRefreshToken().getString());
    TokenPair tokenPair1 = authFlow.refreshAndGenerateToken(refreshTokenBody);
    Assertions.assertNotNull(tokenPair1.getAccessToken());
    Assertions.assertNotNull(tokenPair1.getRefreshToken());
    // logout
    authFlow.addLogoutMiddleware(new UserTokenValidation(tokenManager));
    ILogoutBody logoutBody = new LogoutBody();
    logoutBody.setAccessToken(tokenPair1.getAccessToken().getString());
    logoutBody.setRefreshToken(tokenPair1.getRefreshToken().getString());
    ILoginUser loginUser = authFlow.login(loginRequestBody);
    Assertions.assertTrue(authFlow.logout(logoutBody, loginUser));
    // clear
    authFlow.clearFilterMiddleware();
    authFlow.clearLoginMiddleware();
    authFlow.clearGenerateTokenMiddleware();
    authFlow.clearLogoutMiddleware();
    authFlow.clearRefreshTokenMiddleware();
  }
}
