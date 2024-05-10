package com.wangboot.model.dataauthority;

import com.wangboot.core.auth.authorization.resource.ApiResource;
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

import java.util.Collections;

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
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      DataAuthorityUtils.getDataAuthority(SimpleDataScope.class);
    });
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

  @Test
  public void testDataAuthorizer() {
    D1 d11 = new D1("g:n:a");
    D1 d12 = new D1("p2");
    D2 d21 = new D2("1");
    D2 d22 = new D2("2");
    // WholeDataAuthorizer
    WholeDataAuthorizer authorizer1 = new WholeDataAuthorizer(true);
    Assertions.assertTrue(authorizer1.hasDataAuthority(null));
    Assertions.assertTrue(authorizer1.hasDataAuthority(d11));
    Assertions.assertTrue(authorizer1.hasDataAuthorities(Collections.singleton(d11)));
    Assertions.assertEquals("", authorizer1.getField());
    // UserIdDataAuthorizer
    UserIdDataAuthorizer authorizer2 = new UserIdDataAuthorizer("1", "userId");
    Assertions.assertTrue(authorizer2.hasDataAuthority(d21));
    Assertions.assertFalse(authorizer2.hasDataAuthority(d22));
    Assertions.assertEquals(1, authorizer2.getAuthorities().size());
    // DataScopeAuthorizer
    DataScopeAuthorizer authorizer3 = new DataScopeAuthorizer("field", Collections.singletonList(new ApiResource("g", "n", "a")));
    Assertions.assertFalse(authorizer3.hasDataAuthority(null));
    Assertions.assertTrue(authorizer3.hasDataAuthority(d11));
    Assertions.assertFalse(authorizer3.hasDataAuthority(d12));
    Assertions.assertEquals(1, authorizer3.getAuthorities().size());
  }

}
