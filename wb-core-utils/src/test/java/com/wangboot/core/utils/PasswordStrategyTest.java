package com.wangboot.core.utils;

import com.wangboot.core.utils.password.PasswordStrategyManager;
import com.wangboot.core.utils.password.strategy.MinimumLength;
import com.wangboot.core.utils.password.strategy.MultiPattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("密码策略测试")
public class PasswordStrategyTest {

  @Test
  public void testMinimumLength() {
    MinimumLength minimumLength = new MinimumLength();
    Assertions.assertTrue(minimumLength.check("123456"));
    Assertions.assertTrue(minimumLength.check("1234567890"));
    Assertions.assertFalse(minimumLength.check("12345"));
    Assertions.assertFalse(minimumLength.check(""));
    minimumLength = new MinimumLength(10);
    Assertions.assertEquals(10, minimumLength.getMinLength());
    Assertions.assertFalse(minimumLength.check("123456"));
    Assertions.assertTrue(minimumLength.check("1234567890"));
    Assertions.assertFalse(minimumLength.check("12345"));
    Assertions.assertFalse(minimumLength.check(""));
  }

  @Test
  public void testMultiPattern() {
    MultiPattern multiPattern = new MultiPattern();
    Assertions.assertTrue(multiPattern.check("1qA!"));
    Assertions.assertFalse(multiPattern.check("1qA"));
    Assertions.assertFalse(multiPattern.check("1q!"));
    Assertions.assertFalse(multiPattern.check("1Q!"));
    Assertions.assertFalse(multiPattern.check("qQ!"));
    Assertions.assertFalse(multiPattern.check(""));
    multiPattern = new MultiPattern(MultiPattern.NUMBER_PATTERN);
    Assertions.assertEquals(MultiPattern.NUMBER_PATTERN, multiPattern.getPattern());
    Assertions.assertTrue(multiPattern.check("1qA!"));
    Assertions.assertTrue(multiPattern.check("1qA"));
    Assertions.assertTrue(multiPattern.check("1qa!"));
    Assertions.assertTrue(multiPattern.check("1QA!"));
    Assertions.assertFalse(multiPattern.check("qA!"));
    Assertions.assertFalse(multiPattern.check(""));
    multiPattern = new MultiPattern(MultiPattern.UPPER_CASE_PATTERN);
    Assertions.assertEquals(MultiPattern.UPPER_CASE_PATTERN, multiPattern.getPattern());
    Assertions.assertTrue(multiPattern.check("1qA!"));
    Assertions.assertTrue(multiPattern.check("1qA"));
    Assertions.assertFalse(multiPattern.check("1qa!"));
    Assertions.assertTrue(multiPattern.check("1QA!"));
    Assertions.assertTrue(multiPattern.check("qA!"));
    Assertions.assertFalse(multiPattern.check(""));
    multiPattern = new MultiPattern(MultiPattern.LOWER_CASE_PATTERN);
    Assertions.assertEquals(MultiPattern.LOWER_CASE_PATTERN, multiPattern.getPattern());
    Assertions.assertTrue(multiPattern.check("1qA!"));
    Assertions.assertTrue(multiPattern.check("1qA"));
    Assertions.assertTrue(multiPattern.check("1qa!"));
    Assertions.assertFalse(multiPattern.check("1QA!"));
    Assertions.assertTrue(multiPattern.check("qA!"));
    Assertions.assertFalse(multiPattern.check(""));
    multiPattern = new MultiPattern(MultiPattern.SYMBOL_PATTERN);
    Assertions.assertEquals(MultiPattern.SYMBOL_PATTERN, multiPattern.getPattern());
    Assertions.assertTrue(multiPattern.check("1qA!"));
    Assertions.assertFalse(multiPattern.check("1qA"));
    Assertions.assertTrue(multiPattern.check("1qa!"));
    Assertions.assertTrue(multiPattern.check("1QA!"));
    Assertions.assertTrue(multiPattern.check("qA!"));
    Assertions.assertFalse(multiPattern.check(""));
  }

  @Test
  public void testPasswordStrategyManager() {
    PasswordStrategyManager manager = new PasswordStrategyManager();
    manager.addPasswordStrategy(new MinimumLength(8));
    manager.addPasswordStrategy(new MultiPattern());
    Assertions.assertTrue(manager.checkStrategies("qweRTY123-"));
    Assertions.assertFalse(manager.checkStrategies("qweRTY123"));
    Assertions.assertFalse(manager.checkStrategies("qweRTY-_*&"));
    Assertions.assertFalse(manager.checkStrategies("qwe123-_*&"));
    Assertions.assertFalse(manager.checkStrategies("RTY123-_*&"));
    Assertions.assertFalse(manager.checkStrategies("qR1-"));
    manager.clearStrategies();
    Assertions.assertTrue(manager.checkStrategies("123"));
  }
}
