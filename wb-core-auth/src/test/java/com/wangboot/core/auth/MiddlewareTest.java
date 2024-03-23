package com.wangboot.core.auth;

import cn.hutool.core.util.RandomUtil;
import com.wangboot.core.auth.context.ILoginUser;
import com.wangboot.core.auth.context.LoginUser;
import com.wangboot.core.auth.exception.*;
import com.wangboot.core.auth.frontend.impl.MockFrontend;
import com.wangboot.core.auth.middleware.filter.AccessTokenTypeFilter;
import com.wangboot.core.auth.middleware.filter.BlacklistFilter;
import com.wangboot.core.auth.middleware.filter.LoginRestrictionFilter;
import com.wangboot.core.auth.middleware.filter.UserStatusFilter;
import com.wangboot.core.auth.middleware.generatetoken.LoginRestrictionGuard;
import com.wangboot.core.auth.middleware.login.CaptchaValidation;
import com.wangboot.core.auth.middleware.login.LoginFailedLock;
import com.wangboot.core.auth.middleware.login.StaffOnlyCheck;
import com.wangboot.core.auth.middleware.logout.BlacklistHandler;
import com.wangboot.core.auth.middleware.logout.UserTokenValidation;
import com.wangboot.core.auth.middleware.refreshtoken.BlacklistValidation;
import com.wangboot.core.auth.middleware.refreshtoken.RefreshTokenTypeCheck;
import com.wangboot.core.auth.model.*;
import com.wangboot.core.auth.security.LoginRestriction;
import com.wangboot.core.auth.security.LoginRestrictionStrategy;
import com.wangboot.core.auth.token.IAuthToken;
import com.wangboot.core.auth.token.ITokenManager;
import com.wangboot.core.auth.token.TokenPair;
import com.wangboot.core.auth.token.session.SessionTokenManager;
import com.wangboot.core.auth.user.impl.MockUser;
import com.wangboot.core.auth.utils.AuthUtils;
import com.wangboot.core.reliability.blacklist.CacheBlacklistHolder;
import com.wangboot.core.reliability.blacklist.IBlacklistHolder;
import com.wangboot.core.reliability.countlimit.CacheCountLimit;
import com.wangboot.core.reliability.countlimit.ICountLimit;
import com.wangboot.core.reliability.loginlock.CacheFailedLock;
import com.wangboot.core.reliability.loginlock.IFailedLock;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("中间件测试")
public class MiddlewareTest {

  @Test
  @SneakyThrows
  public void testFilter() {
    ITokenManager tokenManager = new SessionTokenManager("issuer", 10, 10);
    String token = RandomUtil.randomString(10);
    ILoginUser loginUser =
        new LoginUser(
            new MockUser("1", RandomUtil.randomString(6), RandomUtil.randomString(6)),
            AuthUtils.UNKNOWN_FRONTEND,
            AuthUtils.ALLOW_ALL);
    // AccessTokenTypeFilter
    AccessTokenTypeFilter accessTokenTypeFilter = new AccessTokenTypeFilter();
    IAuthToken authToken = tokenManager.generateToken(TokenPair.ACCESS_TOKEN_TYPE, loginUser, 10);
    Assertions.assertNotNull(authToken);
    boolean ret = accessTokenTypeFilter.afterParseToken(token, authToken);
    Assertions.assertTrue(ret);
    authToken = tokenManager.generateToken(TokenPair.REFRESH_TOKEN_TYPE, loginUser, 10);
    Assertions.assertNotNull(authToken);
    ret = accessTokenTypeFilter.afterParseToken(token, authToken);
    Assertions.assertFalse(ret);
    // UserStatusFilter
    UserStatusFilter userStatusFilter = new UserStatusFilter();
    loginUser =
        new LoginUser(
            new MockUser(
                "1",
                RandomUtil.randomString(6),
                RandomUtil.randomString(6),
                false,
                false,
                true,
                true,
                null),
            AuthUtils.UNKNOWN_FRONTEND,
            AuthUtils.ALLOW_ALL);
    ret = userStatusFilter.afterAuthentication(token, authToken, loginUser);
    Assertions.assertFalse(ret);
    loginUser =
        new LoginUser(
            new MockUser(
                "1",
                RandomUtil.randomString(6),
                RandomUtil.randomString(6),
                false,
                false,
                true,
                false,
                null),
            AuthUtils.UNKNOWN_FRONTEND,
            AuthUtils.ALLOW_ALL);
    ret = userStatusFilter.afterAuthentication(token, authToken, loginUser);
    Assertions.assertTrue(ret);
    // BlacklistFilter
    BlacklistFilter blacklistFilter = new BlacklistFilter(null);
    Assertions.assertEquals("", blacklistFilter.beforeParseToken(token));
    IBlacklistHolder blacklistHolder = new CacheBlacklistHolder(RandomUtil.randomString(6), 1000);
    blacklistFilter = new BlacklistFilter(blacklistHolder);
    Assertions.assertEquals(token, blacklistFilter.beforeParseToken(token));
    blacklistHolder.addBlacklist(token, 1000);
    Assertions.assertEquals("", blacklistFilter.beforeParseToken(token));
    Thread.sleep(1001);
    Assertions.assertEquals(token, blacklistFilter.beforeParseToken(token));
    // LoginRestriction
    LoginRestriction loginRestriction = new LoginRestriction();
    LoginRestrictionFilter loginRestrictionFilter =
        new LoginRestrictionFilter(LoginRestrictionStrategy.ONLY_ONE, loginRestriction);
    loginUser =
        new LoginUser(
            new MockUser("2", RandomUtil.randomString(6), RandomUtil.randomString(6)),
            AuthUtils.UNKNOWN_FRONTEND,
            AuthUtils.ALLOW_ALL);
    ret = loginRestrictionFilter.afterAuthentication(token, authToken, loginUser);
    Assertions.assertTrue(ret);
    loginRestriction.acquireGuard(LoginRestrictionStrategy.ONLY_ONE, loginUser, token);
    ret = loginRestrictionFilter.afterAuthentication(token, authToken, loginUser);
    Assertions.assertTrue(ret);
    final ILoginUser loginUser1 = loginUser;
    final IAuthToken authToken1 = authToken;
    Assertions.assertThrows(
        KickedOutException.class,
        () ->
            loginRestrictionFilter.afterAuthentication(
                RandomUtil.randomString(12), authToken1, loginUser1));
  }

  @Test
  public void testLogout() {
    // BlacklistHandler
    ITokenManager tokenManager = new SessionTokenManager("issuer", 10, 10);
    BlacklistHandler blacklistHandler = new BlacklistHandler(null);
    ILogoutBody logoutBody = new LogoutBody("", "");
    ILoginUser loginUser =
        new LoginUser(
            new MockUser("99", RandomUtil.randomString(6), RandomUtil.randomString(6)),
            AuthUtils.UNKNOWN_FRONTEND,
            AuthUtils.ALLOW_ALL);
    Assertions.assertTrue(blacklistHandler.afterLogout(logoutBody, loginUser));
    IBlacklistHolder blacklistHolder = new CacheBlacklistHolder(RandomUtil.randomString(6), 1000);
    blacklistHandler = new BlacklistHandler(blacklistHolder);
    Assertions.assertTrue(blacklistHandler.afterLogout(logoutBody, loginUser));
    // UserTokenValidation
    UserTokenValidation userTokenValidation = new UserTokenValidation(tokenManager);
    // empty not match
    Assertions.assertThrows(
        LogoutFailedException.class, () -> userTokenValidation.beforeLogout(logoutBody, loginUser));
    // match
    IAuthToken authToken = tokenManager.generateToken(TokenPair.ACCESS_TOKEN_TYPE, loginUser, 10);
    Assertions.assertNotNull(authToken);
    logoutBody.setAccessToken(authToken.getString());
    ILogoutBody logoutBody1 = userTokenValidation.beforeLogout(logoutBody, loginUser);
    Assertions.assertEquals(logoutBody, logoutBody1);
    // user_id not match
    ILoginUser loginUser1 =
        new LoginUser(
            new MockUser("1", "admin", RandomUtil.randomString(6)),
            AuthUtils.UNKNOWN_FRONTEND,
            AuthUtils.ALLOW_ALL);
    Assertions.assertThrows(
        LogoutFailedException.class,
        () -> userTokenValidation.beforeLogout(logoutBody, loginUser1));
  }

  @Test
  public void testRefreshToken() {
    // BlacklistValidation
    ITokenManager tokenManager = new SessionTokenManager("issuer", 10, 10);
    BlacklistValidation blacklistValidation = new BlacklistValidation(null, 10);
    IRefreshTokenBody body = new RefreshTokenBody("", "");
    IRefreshTokenBody body1 = blacklistValidation.beforeRefreshToken(body);
    Assertions.assertEquals(body, body1);
    ILoginUser loginUser =
        new LoginUser(
            new MockUser(
                "99",
                RandomUtil.randomString(6),
                RandomUtil.randomString(6),
                false,
                false,
                true,
                false,
                null),
            AuthUtils.UNKNOWN_FRONTEND,
            AuthUtils.ALLOW_ALL);
    IBlacklistHolder blacklistHolder = new CacheBlacklistHolder(RandomUtil.randomString(6), 1000);
    blacklistValidation = new BlacklistValidation(blacklistHolder, 10);
    TokenPair tokenPair = tokenManager.generateTokenPair(loginUser);
    Assertions.assertNotNull(tokenPair.getAccessToken());
    Assertions.assertNotNull(tokenPair.getRefreshToken());
    body.setAccessToken(tokenPair.getAccessToken().getString());
    body.setRefreshToken(tokenPair.getRefreshToken().getString());
    body1 = blacklistValidation.beforeRefreshToken(body);
    Assertions.assertEquals(body, body1);
    Assertions.assertTrue(blacklistValidation.afterRefreshToken(body, loginUser));
    Assertions.assertNull(blacklistValidation.beforeRefreshToken(body));
    // RefreshTokenTypeCheck
    RefreshTokenTypeCheck refreshTokenTypeCheck = new RefreshTokenTypeCheck(tokenManager);
    body.setAccessToken("");
    body.setRefreshToken("");
    Assertions.assertNull(refreshTokenTypeCheck.beforeRefreshToken(body));
    body.setRefreshToken(tokenPair.getAccessToken().getString());
    Assertions.assertNull(refreshTokenTypeCheck.beforeRefreshToken(body));
    body.setRefreshToken(tokenPair.getRefreshToken().getString());
    Assertions.assertEquals(body, refreshTokenTypeCheck.beforeRefreshToken(body));
  }

  @Test
  public void testLogin() {
    LoginBody loginBody = new LoginBody();
    // LoginFailedLock
    ICountLimit countLimit = new CacheCountLimit(5, 1);
    IBlacklistHolder blacklistHolder = new CacheBlacklistHolder(RandomUtil.randomString(6), 1000);
    IFailedLock failedLock = new CacheFailedLock(countLimit, blacklistHolder, 1);
    LoginFailedLock loginFailedLock = new LoginFailedLock(failedLock);
    String username = RandomUtil.randomString(6);
    loginBody.setUsername(username);
    Assertions.assertEquals(loginBody, loginFailedLock.beforeLogin(loginBody));
    failedLock.lock(username);
    Assertions.assertThrows(
        LoginLockedException.class, () -> loginFailedLock.beforeLogin(loginBody));
    // StaffOnlyCheck
    StaffOnlyCheck staffOnlyCheck = new StaffOnlyCheck();
    final ILoginUser loginUser1 =
        new LoginUser(
            new MockUser(
                "99",
                RandomUtil.randomString(6),
                RandomUtil.randomString(6),
                false,
                false,
                true,
                false,
                null),
            new MockFrontend("1", "web", "web", true, false),
            AuthUtils.ALLOW_ALL);
    Assertions.assertThrows(
        LoginFailedException.class, () -> staffOnlyCheck.afterLogin(loginBody, loginUser1));
    final ILoginUser loginUser2 =
        new LoginUser(
            new MockUser(
                "99",
                RandomUtil.randomString(6),
                RandomUtil.randomString(6),
                false,
                true,
                true,
                false,
                null),
            new MockFrontend("1", "web", "web", true, false),
            AuthUtils.ALLOW_ALL);
    Assertions.assertTrue(staffOnlyCheck.afterLogin(loginBody, loginUser2));
    // CaptchaValidation
    CaptchaValidation captchaValidation = new CaptchaValidation(new String[] {"image"});
    Assertions.assertEquals(loginBody, captchaValidation.beforeLogin(loginBody));
    loginBody.setCaptchaType("image");
    loginBody.setCaptcha(RandomUtil.randomString(6));
    Assertions.assertThrows(
        CaptchaMismatchException.class, () -> captchaValidation.beforeLogin(loginBody));
    loginBody.setCaptchaType("sms");
    Assertions.assertEquals(loginBody, captchaValidation.beforeLogin(loginBody));
  }

  @Test
  public void testGenerateToken() {
    // LoginRestrictionGuard
    LoginRestriction loginRestriction = new LoginRestriction();
    LoginRestrictionGuard loginRestrictionGuard =
        new LoginRestrictionGuard(LoginRestrictionStrategy.ONLY_ONE, loginRestriction);
    ILoginUser loginUser =
        new LoginUser(
            new MockUser(
                "99",
                RandomUtil.randomString(6),
                RandomUtil.randomString(6),
                false,
                false,
                true,
                false,
                null),
            AuthUtils.UNKNOWN_FRONTEND,
            AuthUtils.ALLOW_ALL);
    ITokenManager tokenManager = new SessionTokenManager("issuer", 10, 10);
    TokenPair tokenPair = tokenManager.generateTokenPair(loginUser);
    Assertions.assertTrue(loginRestrictionGuard.afterTokenGeneration(loginUser, tokenPair));
  }
}
