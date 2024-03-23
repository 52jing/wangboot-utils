package com.wangboot.core.utils.password;

import com.wangboot.core.utils.password.strategy.MinimumLength;
import com.wangboot.core.utils.password.strategy.MultiPattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 密码策略管理器
 *
 * @author wwtg99
 */
public class PasswordStrategyManager {

  private final List<IPasswordStrategy> strategies;

  public PasswordStrategyManager() {
    this.strategies = new ArrayList<>();
  }

  public void initStrategy(
      int minLength,
      boolean requireNumber,
      boolean requireUpper,
      boolean requireLower,
      boolean requireSymbol) {
    this.clearStrategies();
    if (minLength > 0) {
      this.addPasswordStrategy(new MinimumLength(minLength));
    }
    int pattern = 0;
    if (requireNumber) {
      pattern |= MultiPattern.NUMBER_PATTERN;
    }
    if (requireUpper) {
      pattern |= MultiPattern.UPPER_CASE_PATTERN;
    }
    if (requireLower) {
      pattern |= MultiPattern.LOWER_CASE_PATTERN;
    }
    if (requireSymbol) {
      pattern |= MultiPattern.SYMBOL_PATTERN;
    }
    if (pattern > 0) {
      this.addPasswordStrategy(new MultiPattern(pattern));
    }
  }

  public void addPasswordStrategy(IPasswordStrategy passwordStrategy) {
    if (Objects.nonNull(passwordStrategy)) {
      this.strategies.add(passwordStrategy);
    }
  }

  public void clearStrategies() {
    this.strategies.clear();
  }

  /** 检查所有策略 */
  public boolean checkStrategies(String password) {
    for (IPasswordStrategy passwordStrategy : this.strategies) {
      if (!passwordStrategy.check(password)) {
        return false;
      }
    }
    return true;
  }
}
