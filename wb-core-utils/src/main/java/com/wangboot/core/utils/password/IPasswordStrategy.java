package com.wangboot.core.utils.password;

/**
 * 密码策略接口
 *
 * @author wwtg99
 */
public interface IPasswordStrategy {
  boolean check(String password);
}
