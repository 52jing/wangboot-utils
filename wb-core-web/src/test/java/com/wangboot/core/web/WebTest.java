package com.wangboot.core.web;

import cn.hutool.core.util.RandomUtil;
import com.wangboot.core.web.param.ParamUtils;
import com.wangboot.core.web.param.impl.EnvFileParamConfig;
import com.wangboot.core.web.utils.ResponseUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.assertj.core.util.Lists;
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
    // ParamUtils
    String out = ParamUtils.convertStrListToParamValue(Lists.list("a", "b", "c"));
    Assertions.assertEquals("a,b,c", out);
    List<String> lsstr = ParamUtils.getStrList(out);
    Assertions.assertEquals(3, lsstr.size());
    out = ParamUtils.convertIntListToParamValue(Lists.list(1L, 2L, 3L));
    Assertions.assertEquals("1,2,3", out);
    List<Long> lslong = ParamUtils.getIntList(out);
    Assertions.assertEquals(3, lslong.size());
    out = ParamUtils.convertFloatListToParamValue(Lists.list(1.1f, 2.1f));
    Assertions.assertEquals("1.1,2.1", out);
    List<Float> lsfloat = ParamUtils.getFloatList(out);
    Assertions.assertEquals(2, lsfloat.size());
    out = ParamUtils.convertBoolListToParamValue(Lists.list(true, false));
    Assertions.assertEquals("true,false", out);
    List<Boolean> lsbool = ParamUtils.getBoolList(out);
    Assertions.assertEquals(2, lsbool.size());
    Assertions.assertNull(ParamUtils.getInt(null, 1));
    Assertions.assertEquals(1, ParamUtils.getInt("1", 0));
    Assertions.assertEquals(2, ParamUtils.getInt("2"));
    Assertions.assertEquals(3, ParamUtils.getInt("a", 3));
    Assertions.assertNull(ParamUtils.getInt("a"));
    Assertions.assertEquals(1, ParamUtils.getIntPrimitive("1", 0));
    Assertions.assertEquals(0, ParamUtils.getIntPrimitive("a", 0));
    Assertions.assertNull(ParamUtils.getInt(null, 1));
    Assertions.assertEquals(1, ParamUtils.getLong("1", 0L));
    Assertions.assertEquals(2, ParamUtils.getLong("2"));
    Assertions.assertEquals(3, ParamUtils.getLong("a", 3L));
    Assertions.assertNull(ParamUtils.getLong("a"));
    Assertions.assertEquals(1, ParamUtils.getLongPrimitive("1", 0L));
    Assertions.assertEquals(0, ParamUtils.getLongPrimitive("a", 0L));
    Assertions.assertNull(ParamUtils.getLong(null, 0L));
    Assertions.assertEquals(1.1f, ParamUtils.getFloat("1.1", 0f));
    Assertions.assertEquals(2.2f, ParamUtils.getFloat("2.2"));
    Assertions.assertEquals(3f, ParamUtils.getFloat("a", 3f));
    Assertions.assertNull(ParamUtils.getFloat("a"));
    Assertions.assertEquals(1.0f, ParamUtils.getFloatPrimitive("1.0", 0.1f));
    Assertions.assertEquals(0.1f, ParamUtils.getFloatPrimitive("a", 0.1f));
    Assertions.assertNull(ParamUtils.getFloat(null, 0f));
    Assertions.assertEquals(true, ParamUtils.getBoolean("true", false));
    Assertions.assertEquals(false, ParamUtils.getBoolean("false"));
    Assertions.assertEquals(false, ParamUtils.getBoolean("a", false));
    Assertions.assertNull(ParamUtils.getBoolean(null));
    Assertions.assertTrue(ParamUtils.getBooleanPrimitive("true", false));
    Assertions.assertFalse(ParamUtils.getBooleanPrimitive("a", false));
    Assertions.assertNull(ParamUtils.getBoolean(null, false));
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
