package com.wangboot.core.reliability.license;

/**
 * 许可证管理器接口
 *
 * @author wwtg99
 */
public interface ILicenseManager {

  /** 是否启用 */
  boolean isEnabled();

  /** 创建许可证 */
  ILicense create(String license);

  /** 获取服务器标识 */
  String getServerId();

  /** 验证许可证是否有效 */
  boolean checkValidation();
}
