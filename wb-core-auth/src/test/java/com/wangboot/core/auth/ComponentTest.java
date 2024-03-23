package com.wangboot.core.auth;

import com.wangboot.core.auth.authorization.resource.ApiResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("组件测试")
public class ComponentTest {

  @Test
  public void testResource() {
    String[][] tests1 =
        new String[][] {
          new String[] {"aa:bb:cc", "aa", "bb", "cc"},
          new String[] {"1:2:3", "1", "2", "3"},
          new String[] {"a:b:c:d", "a:b", "c", "d"},
          new String[] {"a:b", "", "a", "b"},
          new String[] {"aaa", "", "aaa", ""},
        };
    for (String[] ss : tests1) {
      ApiResource resource = ApiResource.of(ss[0]);
      Assertions.assertNotNull(resource);
      Assertions.assertEquals(ss[0], resource.getResourceName());
      Assertions.assertEquals(ss[1], resource.getGroup());
      Assertions.assertEquals(ss[2], resource.getName());
      Assertions.assertEquals(ss[3], resource.getAction());
    }
    Assertions.assertNull(ApiResource.of(null));
    Assertions.assertNull(ApiResource.of(""));
  }

  //  @Test
  //  @SneakyThrows
  //  public void testLoginLock() {
  //    IUserService userService = new TestUserService();
  //    LoginLock loginLock = new LoginLock(userService, 3, 3, 1, false);
  //    Assertions.assertEquals(0, loginLock.getCount(""));
  //    Assertions.assertFalse(loginLock.isLocked(""));
  //    Assertions.assertEquals(0, loginLock.logFailed(""));
  //    String username1 = RandomUtil.randomString(6);
  //    String username2 = RandomUtil.randomString(6);
  //    Assertions.assertFalse(loginLock.isLocked(username1));
  //    Assertions.assertFalse(loginLock.isLocked(username2));
  //    int count = loginLock.getCount(username1);
  //    Assertions.assertEquals(0, count);
  //    Assertions.assertFalse(loginLock.isLocked(username1));
  //    count = loginLock.logFailed(username1);
  //    Assertions.assertEquals(1, count);
  //    Assertions.assertFalse(loginLock.isLocked(username1));
  //    count = loginLock.logFailed(username1);
  //    Assertions.assertEquals(2, count);
  //    Assertions.assertFalse(loginLock.isLocked(username1));
  //    count = loginLock.logFailed(username1);
  //    Assertions.assertEquals(3, count);
  //    Assertions.assertTrue(loginLock.isLocked(username1));
  //    count = loginLock.getCount(username2);
  //    Assertions.assertEquals(0, count);
  //    Assertions.assertFalse(loginLock.isLocked(username2));
  //    Thread.sleep(3100);
  //    Assertions.assertFalse(loginLock.isLocked(username1));
  //    count = loginLock.getCount(username1);
  //    Assertions.assertEquals(0, count);
  //    loginLock = new LoginLock(userService, 2, 3, 1, true);
  //    Assertions.assertFalse(loginLock.isLocked(username1));
  //    count = loginLock.logFailed(username1);
  //    Assertions.assertEquals(1, count);
  //    Assertions.assertFalse(loginLock.isLocked(username1));
  //    count = loginLock.logFailed(username1);
  //    Assertions.assertEquals(2, count);
  //    Assertions.assertTrue(loginLock.isLocked(username1));
  //    String userAdmin = "admin";
  //    Assertions.assertFalse(loginLock.isLocked(userAdmin));
  //    count = loginLock.logFailed(userAdmin);
  //    Assertions.assertEquals(1, count);
  //    Assertions.assertFalse(loginLock.isLocked(userAdmin));
  //    count = loginLock.logFailed(userAdmin);
  //    Assertions.assertEquals(2, count);
  //    Assertions.assertFalse(loginLock.isLocked(userAdmin));
  //  }

  //  @Test
  //  @SneakyThrows
  //  public void testLoginRestriction() {
  //    LoginRestriction loginRestriction = new LoginRestriction();
  //    String userId = "1";
  //    String username = RandomUtil.randomString(10);
  //    IUserModel user1 =
  //        new MockUser(
  //            userId,
  //            username,
  //            RandomUtil.randomString(11),
  //            RandomUtil.randomString(8),
  //            RandomUtil.randomString(8));
  //    IFrontendModel frontend1 = new MockFrontend("1", "web1", "WEB", false, false);
  //    IFrontendModel frontend2 = new MockFrontend("2", "app", "APP", false, false);
  //    IFrontendModel frontend3 = new MockFrontend("3", "web2", "WEB", false, false);
  //    ILoginUser loginUser1 = new LoginUser(user1, frontend1);
  //    ILoginUser loginUser2 = new LoginUser(user1, frontend2);
  //    ILoginUser loginUser3 = new LoginUser(user1, frontend3);
  //    String token1 = RandomUtil.randomString(12);
  //    String token2 = RandomUtil.randomString(12);
  //    String token3 = RandomUtil.randomString(12);
  //    String token4 = RandomUtil.randomString(12);
  //    // 未添加守卫
  //    Assertions.assertFalse(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.NO_RESTRICTION, loginUser1, ""));
  //    Assertions.assertTrue(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.NO_RESTRICTION, loginUser1,
  // token1));
  //    Assertions.assertTrue(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.PER_TYPE, loginUser1, token1));
  //    Assertions.assertTrue(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.PER_FRONTEND, loginUser1, token1));
  //    Assertions.assertTrue(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.ONLY_ONE, loginUser1, token1));
  //    // 添加守卫
  //    // NO_RESTRICTION
  //    loginRestriction.acquireGuard(LoginRestrictionStrategy.NO_RESTRICTION, loginUser1, token1);
  //    Assertions.assertTrue(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.NO_RESTRICTION, loginUser1,
  // token1));
  //    Assertions.assertTrue(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.NO_RESTRICTION, loginUser2,
  // token2));
  //    Assertions.assertTrue(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.NO_RESTRICTION, loginUser3,
  // token3));
  //    Assertions.assertTrue(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.NO_RESTRICTION, loginUser1,
  // token4));
  //    // PER_FRONTEND
  //    loginRestriction.acquireGuard(LoginRestrictionStrategy.PER_FRONTEND, loginUser1, token1);
  //    Assertions.assertTrue(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.PER_FRONTEND, loginUser1, token1));
  //    Assertions.assertTrue(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.PER_FRONTEND, loginUser2, token2));
  //    Assertions.assertTrue(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.PER_FRONTEND, loginUser3, token3));
  //    Assertions.assertFalse(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.PER_FRONTEND, loginUser1, token4));
  //    // PER_TYPE
  //    loginRestriction.acquireGuard(LoginRestrictionStrategy.PER_TYPE, loginUser1, token1);
  //    Assertions.assertTrue(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.PER_TYPE, loginUser1, token1));
  //    Assertions.assertTrue(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.PER_TYPE, loginUser2, token2));
  //    Assertions.assertFalse(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.PER_TYPE, loginUser3, token3));
  //    Assertions.assertFalse(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.PER_TYPE, loginUser1, token4));
  //    // ONLY_ONE
  //    loginRestriction.acquireGuard(LoginRestrictionStrategy.ONLY_ONE, loginUser1, token1);
  //    Assertions.assertTrue(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.ONLY_ONE, loginUser1, token1));
  //    Assertions.assertFalse(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.ONLY_ONE, loginUser2, token2));
  //    Assertions.assertFalse(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.ONLY_ONE, loginUser3, token3));
  //    Assertions.assertFalse(
  //        loginRestriction.passGuard(LoginRestrictionStrategy.ONLY_ONE, loginUser1, token4));
  //  }
}
