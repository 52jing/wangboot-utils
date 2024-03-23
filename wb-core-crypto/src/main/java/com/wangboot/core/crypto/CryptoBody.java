package com.wangboot.core.crypto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 加密请求响应体
 *
 * @author wwtg99
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoBody {
  /** 加密数据 */
  private String jsondata;
  /** 加密密钥 */
  private String id;
  /** 加密模式 */
  private String mode;
}
