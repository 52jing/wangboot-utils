package com.wangboot.core.utils.password;

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
