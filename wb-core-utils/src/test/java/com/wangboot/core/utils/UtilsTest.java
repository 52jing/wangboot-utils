package com.wangboot.core.utils;

import cn.hutool.core.util.RandomUtil;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Utils测试")
public class UtilsTest {

  interface Interface1 {}

  interface Interface2 extends Interface1 {}

  static class Clazz1 implements Interface1 {}

  static class Clazz2 implements Interface2 {}

  static class Clazz3 extends ClassValue<String> {

    @Override
    protected String computeValue(Class<?> type) {
      return null;
    }
  }

  @Test
  public void testObjectUtils() {
    Assertions.assertTrue(ObjectUtils.hasInterface(Clazz1.class, Interface1.class));
    Assertions.assertFalse(ObjectUtils.hasInterface(Clazz1.class, Interface2.class));
    Assertions.assertTrue(ObjectUtils.hasInterface(Clazz2.class, Interface1.class));
    Assertions.assertTrue(ObjectUtils.hasInterface(Clazz2.class, Interface1.class));
    Assertions.assertFalse(ObjectUtils.hasInterface(Object.class, Interface1.class));
    Assertions.assertFalse(ObjectUtils.hasInterface(Object.class, Interface2.class));
    Clazz3 cls = new Clazz3();
    Assertions.assertEquals(
        String.class, ObjectUtils.getTypeArgumentClass(cls.getClass().getGenericSuperclass(), 0));
    Clazz2 cls2 = new Clazz2();
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> ObjectUtils.getTypeArgumentClass(cls2.getClass().getGenericSuperclass(), 0));
  }

  @Test
  public void testBitMaskUtils() {
    int flag1 = 0;
    int allow1 = 1;
    int allow2 = 1 << 1;
    int allow3 = 1 << 2;
    int allow4 = 1 << 3;
    Assertions.assertFalse(BitMaskUtils.isAllowed(flag1, allow1));
    Assertions.assertFalse(BitMaskUtils.isAllowed(flag1, allow2));
    Assertions.assertFalse(BitMaskUtils.isAllowed(flag1, allow3));
    Assertions.assertFalse(BitMaskUtils.isAllowed(flag1, allow4));
    int flag2 = BitMaskUtils.enable(flag1, allow1);
    Assertions.assertTrue(BitMaskUtils.isAllowed(flag2, allow1));
    Assertions.assertFalse(BitMaskUtils.isAllowed(flag2, allow2));
    Assertions.assertFalse(BitMaskUtils.isAllowed(flag2, allow3));
    Assertions.assertFalse(BitMaskUtils.isAllowed(flag2, allow4));
    int flag3 = BitMaskUtils.enable(flag2, allow2);
    Assertions.assertTrue(BitMaskUtils.isAllowed(flag3, allow1));
    Assertions.assertTrue(BitMaskUtils.isAllowed(flag3, allow2));
    Assertions.assertFalse(BitMaskUtils.isAllowed(flag3, allow3));
    Assertions.assertFalse(BitMaskUtils.isAllowed(flag3, allow4));
    int flag4 = BitMaskUtils.disable(flag3, allow2);
    Assertions.assertTrue(BitMaskUtils.isAllowed(flag4, allow1));
    Assertions.assertFalse(BitMaskUtils.isAllowed(flag4, allow2));
    Assertions.assertFalse(BitMaskUtils.isAllowed(flag4, allow3));
    Assertions.assertFalse(BitMaskUtils.isAllowed(flag4, allow4));
    Assertions.assertEquals(
        BitMaskUtils.isAllowed(flag4, allow1), !BitMaskUtils.isNotAllowed(flag4, allow1));
    Assertions.assertEquals(
        BitMaskUtils.isAllowed(flag4, allow2), !BitMaskUtils.isNotAllowed(flag4, allow2));
  }

  @Test
  public void testPathUtils() {
    Path dir = Paths.get("src", "main", "java", "com", "wangboot", "core", "utils");
    List<File> files1 = PathUtils.listDirectory(dir.toString(), true);
    files1.forEach(System.out::println);
    Assertions.assertEquals(14, files1.size());
    List<File> files2 = PathUtils.listDirectory(dir.toString(), false);
    files2.forEach(System.out::println);
    Assertions.assertEquals(7, files2.size());
  }

  @Test
  public void testStrUtils() {
    // toUnderScoreCase
    String[][] tests =
        new String[][] {
          new String[] {"", ""},
          new String[] {"TestCase", "test_case"},
          new String[] {"TestCaseModel", "test_case_model"},
          new String[] {"TestCase1", "test_case1"},
          new String[] {"TestCase1Model1", "test_case1_model1"},
          new String[] {"testCase2", "test_case2"},
          new String[] {"testa", "testa"},
        };
    for (String[] test : tests) {
      Assertions.assertEquals(test[1], StrUtils.toUnderScoreCase(test[0]));
    }
    // toCamelCase
    String[][] tests1 =
        new String[][] {
          new String[] {"", ""},
          new String[] {"test_case", "TestCase"},
          new String[] {"test_case_model2", "TestCaseModel2"},
          new String[] {"test_case2", "TestCase2"},
          new String[] {"test_case2_model", "TestCase2Model"},
          new String[] {"testb", "Testb"},
        };
    for (String[] test : tests1) {
      Assertions.assertEquals(test[1], StrUtils.toCamelCase(test[0], true));
    }
    String[][] tests2 =
        new String[][] {
          new String[] {"test_case3", "testCase3"},
          new String[] {"test_case_model3", "testCaseModel3"},
          new String[] {"test_case3", "testCase3"},
          new String[] {"test_case3_model", "testCase3Model"},
          new String[] {"testc", "testc"},
        };
    for (String[] test : tests2) {
      Assertions.assertEquals(test[1], StrUtils.toCamelCase(test[0], false));
    }
    // splitStrByLineWidth
    String s1 = RandomUtil.randomString(30);
    List<String> ls1 = StrUtils.splitStrByLineWidth(s1, 6);
    Assertions.assertEquals(5, ls1.size());
    Assertions.assertEquals(s1.substring(0, 6), ls1.get(0));
    String s2 = RandomUtil.randomString(30);
    List<String> ls2 = StrUtils.splitStrByLineWidth(s2, 7);
    Assertions.assertEquals(5, ls2.size());
    Assertions.assertEquals(s2.substring(0, 7), ls2.get(0));
    // getDuration
    Duration d1 = StrUtils.getDuration("10s");
    Assertions.assertEquals(10, d1.getSeconds());
    Duration d2 = StrUtils.getDuration("30s");
    Assertions.assertEquals(30, d2.getSeconds());
    Duration d3 = StrUtils.getDuration("5m");
    Assertions.assertEquals(300, d3.getSeconds());
    Duration d4 = StrUtils.getDuration("2h");
    Assertions.assertEquals(7200, d4.getSeconds());
    Duration d5 = StrUtils.getDuration("10h");
    Assertions.assertEquals(36000, d5.getSeconds());
    Duration d6 = StrUtils.getDuration("5");
    Assertions.assertEquals(5, d6.getSeconds());
    Assertions.assertThrows(IllegalArgumentException.class, () -> StrUtils.getDuration("1ah"));
    Assertions.assertThrows(IllegalArgumentException.class, () -> StrUtils.getDuration("2b0m"));
    Assertions.assertThrows(IllegalArgumentException.class, () -> StrUtils.getDuration("30ss"));
    Assertions.assertThrows(IllegalArgumentException.class, () -> StrUtils.getDuration("41sa"));
  }
}
