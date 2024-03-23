package com.wangboot.core.test;

import com.wangboot.core.auth.user.IUserModel;
import com.wangboot.core.auth.utils.AuthUtils;
import com.wangboot.core.test.auth.WithAdminUser;
import com.wangboot.core.test.auth.WithAuthContextTestExecutionListener;
import com.wangboot.core.test.auth.WithMockUser;
import com.wangboot.core.test.auth.WithStaffUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestExecutionListeners;

@DisplayName("单元测试组件")
@SpringBootTest
@TestExecutionListeners({WithAuthContextTestExecutionListener.class})
public class CompTest {

  @Test
  @WithAdminUser
  public void testAdminUser() {
    IUserModel userModel = AuthUtils.getUserModel();
    Assertions.assertNotNull(userModel);
    Assertions.assertEquals("1", userModel.getUserId());
    Assertions.assertEquals("admin", userModel.getUsername());
    Assertions.assertTrue(userModel.checkSuperuser());
    Assertions.assertTrue(userModel.checkStaff());
    Assertions.assertTrue(userModel.checkEnabled());
    Assertions.assertFalse(userModel.checkLocked());
  }

  @Test
  @WithStaffUser
  public void testStaffUser() {
    IUserModel userModel = AuthUtils.getUserModel();
    Assertions.assertNotNull(userModel);
    Assertions.assertEquals("2", userModel.getUserId());
    Assertions.assertEquals("staff", userModel.getUsername());
    Assertions.assertFalse(userModel.checkSuperuser());
    Assertions.assertTrue(userModel.checkStaff());
    Assertions.assertTrue(userModel.checkEnabled());
    Assertions.assertFalse(userModel.checkLocked());
  }

  @Test
  @WithMockUser(id = "3", username = "user1")
  public void testMockUser() {
    IUserModel userModel = AuthUtils.getUserModel();
    Assertions.assertNotNull(userModel);
    Assertions.assertEquals("3", userModel.getUserId());
    Assertions.assertEquals("user1", userModel.getUsername());
    Assertions.assertFalse(userModel.checkSuperuser());
    Assertions.assertFalse(userModel.checkStaff());
    Assertions.assertTrue(userModel.checkEnabled());
    Assertions.assertFalse(userModel.checkLocked());
  }

  @SpringBootApplication(
      exclude = {
        DataSourceAutoConfiguration.class,
        SecurityAutoConfiguration.class,
      })
  @ComponentScan("com.wangboot.**")
  public static class Application {
    public static void main(String[] args) {
      SpringApplication.run(CompTest.class, args);
    }
  }
}
