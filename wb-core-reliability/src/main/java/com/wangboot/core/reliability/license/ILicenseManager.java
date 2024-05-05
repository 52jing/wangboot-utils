package com.wangboot.core.reliability.license;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * 许可证管理器接口
 *
 * @author wwtg99
 */
@Deprecated
public interface ILicenseManager {

  /** 解析许可证 */
  ILicense parseLicense(String cert);

  /** 获取服务器标识 */
  String getServerId();

  /** 创建许可证 */
  String createLicense(
      String issuer,
      String subject,
      OffsetDateTime deadline,
      List<String> modules,
      Map<String, String> params);
}
