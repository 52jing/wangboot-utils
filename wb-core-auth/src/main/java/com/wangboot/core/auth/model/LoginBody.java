package com.wangboot.core.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录请求体基础类
 *
 * @author wwtg99
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginBody implements ILoginBody, IFrontendBody {

  private static final long serialVersionUID = 1L;

  /** 用户名 */
  private String username;

  /** 用户密码 */
  private String password;

  /** 登录方式 */
  private String loginType = "";

  /** 验证码 */
  private String captcha = "";

  /** 验证码识别码 */
  private String uuid = "";

  /** 验证码类型 */
  private String captchaType = "";

  /** 前端 ID */
  private String frontendId = "";
}
