package com.wangboot.core.web;

import cn.hutool.core.util.RandomUtil;
import com.wangboot.core.web.param.impl.EnvFileParamConfig;
import com.wangboot.core.web.utils.ResponseUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.env.MockEnvironment;

@DisplayName("Web测试")
public class WebTest {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  static class ObjBody {
    private String name;
    private int number;
    private boolean active;

    public String otherMethod(String a) {
      return a;
    }
  }

  @Test
  public void testParamConfig() {
    // EnvFileParamConfig
    String k1 = RandomUtil.randomString(6);
    String v1 = RandomUtil.randomString(9);
    String p1 = RandomUtil.randomString(6);
    String k2 = p1 + EnvFileParamConfig.SEP + k1;
    String v2 = RandomUtil.randomString(10);
    Environment environment = new MockEnvironment().withProperty(k1, v1).withProperty(k2, v2);
    EnvFileParamConfig paramConfig = new EnvFileParamConfig(environment, "");
    Assertions.assertEquals(v1, paramConfig.getParamConfig(k1));
    Assertions.assertNull(paramConfig.getParamConfig(""));
    paramConfig = new EnvFileParamConfig(environment, p1);
    Assertions.assertEquals(v2, paramConfig.getParamConfig(k1));
    // convert
    List<String> out1 = paramConfig.getStrList("a,b,c");
    Assertions.assertEquals(3, out1.size());
    Assertions.assertEquals("a", out1.get(0));
    List<Integer> out2 = paramConfig.getIntList("1,2");
    Assertions.assertEquals(2, out2.size());
    Assertions.assertEquals(1, out2.get(0));
    List<Long> out3 = paramConfig.getLongList("1,2,3");
    Assertions.assertEquals(3, out3.size());
    Assertions.assertEquals(1, out3.get(0));
    List<Float> out4 = paramConfig.getFloatList("1.2,2.5");
    Assertions.assertEquals(2, out4.size());
    Assertions.assertEquals(1.2f, out4.get(0));
    List<Boolean> out5 = paramConfig.getBooleanList("true,false");
    Assertions.assertEquals(2, out5.size());
    Assertions.assertEquals(true, out5.get(0));
  }

  @Test
  public void testResponseUtils() {
    ObjBody body = new ObjBody("a", 1, true);
    String s = RandomUtil.randomString(8);
    ResponseEntity<?> entity = ResponseUtils.success();
    Assertions.assertEquals(200, entity.getStatusCodeValue());
    Assertions.assertNull(entity.getBody());
    entity = ResponseUtils.success(body);
    Assertions.assertEquals(200, entity.getStatusCodeValue());
    Assertions.assertEquals(body, entity.getBody());
    val entity3 = ResponseUtils.created(body);
    Assertions.assertEquals(201, entity3.getStatusCodeValue());
    Assertions.assertNotNull(entity3.getBody());
    Assertions.assertEquals(body, entity3.getBody());
    val entity5 = ResponseUtils.deleted();
    Assertions.assertEquals(204, entity5.getStatusCodeValue());
    Assertions.assertNull(entity5.getBody());
    val entity6 = ResponseUtils.error();
    Assertions.assertEquals(500, entity6.getStatusCodeValue());
    Assertions.assertNull(entity6.getBody());
    String errCode = RandomUtil.randomString(6);
    val entity8 = ResponseUtils.error(body, 502);
    Assertions.assertEquals(502, entity8.getStatusCodeValue());
    Assertions.assertNotNull(entity8.getBody());
    Assertions.assertEquals(body, entity8.getBody());
  }
}
