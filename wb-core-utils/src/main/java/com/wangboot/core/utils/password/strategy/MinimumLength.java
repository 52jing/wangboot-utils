package com.wangboot.core.utils.password.strategy;

import com.wangboot.core.utils.password.IPasswordStrategy;

/**
 * 最小长度策略
 *
 * @author wwtg99
 */
public class MinimumLength implements IPasswordStrategy {

  public static final int DEFAULT_MIN_LENGTH = 6;

  private final int minLength;

  public MinimumLength(int minLength) {
    this.minLength = minLength;
  }

  public MinimumLength() {
    this(DEFAULT_MIN_LENGTH);
  }

  public int getMinLength() {
    return this.minLength;
  }

  @Override
  public boolean check(String password) {
    return password.length() >= this.minLength;
  }
}
