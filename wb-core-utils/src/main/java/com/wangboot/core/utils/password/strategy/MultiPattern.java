package com.wangboot.core.utils.password.strategy;

import cn.hutool.core.text.CharSequenceUtil;
import com.wangboot.core.utils.password.IPasswordStrategy;

/**
 * 包含模式策略
 *
 * @author wwtg99
 */
public class MultiPattern implements IPasswordStrategy {

  public static final int NUMBER_PATTERN = 1;

  public static final int LOWER_CASE_PATTERN = 1 << 1;

  public static final int UPPER_CASE_PATTERN = 1 << 2;

  public static final int SYMBOL_PATTERN = 1 << 3;

  public static final String NUMBERS = "1234567890";

  public static final String UPPERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public static final String LOWERS = "abcdefghijklmnopqrstuvwxyz";

  public static final String SYMBOLS = "!@#$%^&*-_";

  public static final int DEFAULT_PATTERN =
      NUMBER_PATTERN | LOWER_CASE_PATTERN | UPPER_CASE_PATTERN | SYMBOL_PATTERN;

  private final int pattern;

  public MultiPattern(int pattern) {
    this.pattern = pattern;
  }

  public MultiPattern() {
    this(DEFAULT_PATTERN);
  }

  public boolean isPatternEnabled(int pm) {
    return (this.pattern & pm) == pm;
  }

  public int getPattern() {
    return this.pattern;
  }

  @Override
  public boolean check(String password) {
    if (this.isPatternEnabled(NUMBER_PATTERN)
        && !CharSequenceUtil.containsAny(password, NUMBERS.toCharArray())) {
      return false;
    }
    if (this.isPatternEnabled(UPPER_CASE_PATTERN)
        && !CharSequenceUtil.containsAny(password, UPPERS.toCharArray())) {
      return false;
    }
    if (this.isPatternEnabled(LOWER_CASE_PATTERN)
        && !CharSequenceUtil.containsAny(password, LOWERS.toCharArray())) {
      return false;
    }
    return !this.isPatternEnabled(SYMBOL_PATTERN)
        || CharSequenceUtil.containsAny(password, SYMBOLS.toCharArray());
  }
}
