package com.wangboot.core.auth.model;

/**
 * 登录数据模型接口
 *
 * @author wwtg99
 */
public interface ILoginBody extends ICaptchaBody, IFrontendBody {
  /** 获取用户名 */
  String getUsername();
  /** 设置用户名 */
  void setUsername(String username);

  /** 获取密码 */
  String getPassword();
  /** 设置密码 */
  void setPassword(String password);

  /** 获取登录类型 */
  String getLoginType();
  /** 设置登录类型 */
  void setLoginType(String loginType);
}
