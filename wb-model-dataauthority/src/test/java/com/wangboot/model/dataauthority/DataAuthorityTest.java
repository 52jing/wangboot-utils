package com.wangboot.model.dataauthority;

import com.wangboot.model.dataauthority.authorizer.*;
import com.wangboot.model.dataauthority.datascope.IDataScopeService;
import com.wangboot.model.dataauthority.datascope.SimpleDataScope;
import com.wangboot.model.dataauthority.factory.AllowAllAuthorizerFactory;
import com.wangboot.model.dataauthority.factory.UserIdAuthorizerFactory;
import com.wangboot.model.dataauthority.utils.DataAuthorityUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("数据权限测试")
public class DataAuthorityTest {

  @DataAuthority(field = "field", factory = AllowAllAuthorizerFactory.class)
  @Data
  @AllArgsConstructor
  static class D1 {
    private String field;
  }

  @DataAuthority(field = "userId", factory = UserIdAuthorizerFactory.class)
  @Data
  @AllArgsConstructor
  static class D2 {
    private String userId;
  }

  @Test
  public void testDataAuthorityUtils() {
    // restrictDataAuthority
    Assertions.assertTrue(DataAuthorityUtils.restrictDataAuthority(D1.class));
    Assertions.assertTrue(DataAuthorityUtils.restrictDataAuthority(D2.class));
    Assertions.assertFalse(DataAuthorityUtils.restrictDataAuthority(SimpleDataScope.class));
    Assertions.assertFalse(DataAuthorityUtils.restrictDataAuthority(IDataScopeService.class));
    // getDataAuthority
    DataAuthority dataAuthority = DataAuthorityUtils.getDataAuthority(D1.class);
    Assertions.assertEquals("field", dataAuthority.field());
    dataAuthority = DataAuthorityUtils.getDataAuthority(D2.class);
    Assertions.assertEquals("userId", dataAuthority.field());
    // getDataAuthorizer
    IDataAuthorizer dataAuthorizer = DataAuthorityUtils.getDataAuthorizer(D1.class);
    Assertions.assertNotNull(dataAuthorizer);
    Assertions.assertEquals(WholeDataAuthorizer.class, dataAuthorizer.getClass());
    dataAuthorizer = DataAuthorityUtils.getDataAuthorizer(D2.class);
    Assertions.assertNotNull(dataAuthorizer);
    Assertions.assertEquals(UserIdDataAuthorizer.class, dataAuthorizer.getClass());
    dataAuthorizer = DataAuthorityUtils.getDataAuthorizer(SimpleDataScope.class);
    Assertions.assertNull(dataAuthorizer);
  }

  //  @Test
  //  public void testDataAuthorizer() {
  //    String userId = "123";
  //    String auth = RandomUtil.randomString(6);
  //    IUserModel userModel = MockUser.builder().id(userId).build();
  //    D1 d1 = new D1();
  //    // NoneDataAuthorizer
  //    IDataAuthorizer dataAuthorizer = new NoneDataAuthorizer(true);
  //    Assertions.assertTrue(dataAuthorizer.hasAllAuthorities());
  //    Assertions.assertTrue(dataAuthorizer.authorize(d1));
  //    Assertions.assertTrue(dataAuthorizer.authorizeAll(Collections.singletonList(d1)));
  //    Assertions.assertEquals(0, dataAuthorizer.getAuthorities().size());
  //    dataAuthorizer = new NoneDataAuthorizer(false);
  //    Assertions.assertFalse(dataAuthorizer.hasAllAuthorities());
  //    Assertions.assertFalse(dataAuthorizer.authorize(d1));
  //    Assertions.assertFalse(dataAuthorizer.authorizeAll(Collections.singletonList(d1)));
  //    Assertions.assertEquals(0, dataAuthorizer.getAuthorities().size());
  //    // DataScopeAuthorizer
  //    String dataScopeName = RandomUtil.randomString(8);
  //    IDataScopeModel dataScopeModel = new SimpleDataScope(dataScopeName);
  //    dataAuthorizer = new DataScopeAuthorizer(userModel,
  // Collections.singletonList(dataScopeModel));
  //    Assertions.assertFalse(dataAuthorizer.hasAllAuthorities());
  //    Assertions.assertEquals(1, dataAuthorizer.getAuthorities().size());
  //    Assertions.assertFalse(dataAuthorizer.authorize(null));
  //    Assertions.assertTrue(dataAuthorizer.authorizeAll(Collections.emptyList()));
  //    Assertions.assertFalse(dataAuthorizer.authorize(d1));
  //    Assertions.assertFalse(dataAuthorizer.authorizeAll(Collections.singletonList(d1)));
  //    D2 d2 = new D2();
  //    IDataAuthorizer finalDataAuthorizer = dataAuthorizer;
  //    Assertions.assertThrows(
  //        IllegalArgumentException.class, () -> finalDataAuthorizer.authorize(d2));
  //    Assertions.assertThrows(
  //        IllegalArgumentException.class,
  //        () -> finalDataAuthorizer.authorizeAll(Collections.singletonList(d2)));
  //    D3 d3 = new D3(dataScopeName);
  //    Assertions.assertTrue(dataAuthorizer.authorize(d3));
  //    Assertions.assertTrue(dataAuthorizer.authorizeAll(Collections.singletonList(d3)));
  //    d3 = new D3(RandomUtil.randomString(9));
  //    Assertions.assertFalse(dataAuthorizer.authorize(d3));
  //    Assertions.assertFalse(dataAuthorizer.authorizeAll(Collections.singletonList(d3)));
  //
  //    // CustomFieldDataAuthorizer
  //    //    List<String> perms = new ArrayList<>();
  //    //    perms.add(auth);
  //    //    dataAuthorizer = new CustomFieldDataAuthorizer(perms, false);
  //    //    Assertions.assertFalse(dataAuthorizer.hasAllAuthorities());
  //    //    Assertions.assertEquals(1, dataAuthorizer.getAuthorities().size());
  //    //    Assertions.assertFalse(dataAuthorizer.authorize(null));
  //    //    Assertions.assertTrue(dataAuthorizer.authorizeAll(Collections.emptyList()));
  //    //    Assertions.assertFalse(dataAuthorizer.authorize(d1));
  //    //    Assertions.assertFalse(dataAuthorizer.authorizeAll(Collections.singletonList(d1)));
  //    //    D5 d5 = new D5();
  //    //    IDataAuthorizer finalDataAuthorizer1 = dataAuthorizer;
  //    //    Assertions.assertThrows(IllegalArgumentException.class, () ->
  //    // finalDataAuthorizer1.authorize(d5));
  //    //    Assertions.assertThrows(IllegalArgumentException.class, () ->
  //    // finalDataAuthorizer1.authorizeAll(Collections.singletonList(d5)));
  //    //    D6 d6 = new D6(auth);
  //    //    Assertions.assertTrue(dataAuthorizer.authorize(d6));
  //    //    Assertions.assertTrue(dataAuthorizer.authorizeAll(Collections.singletonList(d6)));
  //    //    d6 = new D6(RandomUtil.randomString(10));
  //    //    Assertions.assertFalse(dataAuthorizer.authorize(d6));
  //    //    Assertions.assertFalse(dataAuthorizer.authorizeAll(Collections.singletonList(d6)));
  //    //    Assertions.assertTrue(dataAuthorizer.authorize(new Object()));
  //    // UserIdFieldDataAuthorizer
  //    dataAuthorizer = new UserIdFieldDataAuthorizer(userModel);
  //    Assertions.assertFalse(dataAuthorizer.hasAllAuthorities());
  //    Assertions.assertEquals(1, dataAuthorizer.getAuthorities().size());
  //    Assertions.assertFalse(dataAuthorizer.authorize(null));
  //    Assertions.assertTrue(dataAuthorizer.authorizeAll(Collections.emptyList()));
  //    Assertions.assertFalse(dataAuthorizer.authorize(d1));
  //    Assertions.assertFalse(dataAuthorizer.authorizeAll(Collections.singletonList(d1)));
  //    D4 d4 = new D4();
  //    IDataAuthorizer finalDataAuthorizer2 = dataAuthorizer;
  //    Assertions.assertThrows(
  //        IllegalArgumentException.class, () -> finalDataAuthorizer2.authorize(d4));
  //    Assertions.assertThrows(
  //        IllegalArgumentException.class,
  //        () -> finalDataAuthorizer2.authorizeAll(Collections.singletonList(d4)));
  //    D5 d5 = new D5(userId);
  //    Assertions.assertTrue(dataAuthorizer.authorize(d5));
  //    Assertions.assertTrue(dataAuthorizer.authorizeAll(Collections.singletonList(d5)));
  //    d5 = new D5(RandomUtil.randomString(6));
  //    Assertions.assertFalse(dataAuthorizer.authorize(d5));
  //    Assertions.assertFalse(dataAuthorizer.authorizeAll(Collections.singletonList(d5)));
  //    Assertions.assertTrue(dataAuthorizer.authorize(new Object()));
  //  }
}
