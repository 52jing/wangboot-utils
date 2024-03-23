package com.wangboot.core.crypto;

import org.springframework.lang.NonNull;

/**
 * 非对称加解密提供者
 *
 * @author wwtg99
 */
public interface IAsymmetricCryptoProvider {

  /** 加密 */
  String encrypt(@NonNull byte[] bytes);

  /** 解密 */
  @NonNull
  byte[] decrypt(String data);
}
