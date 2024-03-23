package com.wangboot.core.reliability.license;

import java.time.OffsetDateTime;

/**
 * 许可证接口
 *
 * @author wwtg99
 */
public interface ILicense {
  /** 许可证是否有效 */
  boolean isValid();

  /** 获取验证信息 */
  String getCheckMessage();

  /** 获取许可证被授权者 */
  String getCertSubject();

  /** 获取许可证颁发者 */
  String getCertIssuer();

  /** 获取许可证有效日期 */
  OffsetDateTime getValidDate();
}
