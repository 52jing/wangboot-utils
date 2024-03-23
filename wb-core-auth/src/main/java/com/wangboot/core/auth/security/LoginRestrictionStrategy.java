package com.wangboot.core.auth.security;

/**
 * 登录限制
 *
 * @author wwtg99
 */
public enum LoginRestrictionStrategy {
  // 无限制
  NO_RESTRICTION,
  // 每个账户每个客户端类型只能登录一个点
  PER_TYPE,
  // 每个账户每个前端只能登录一个点
  PER_FRONTEND,
  // 每个账户只能登录一个点
  ONLY_ONE
}
