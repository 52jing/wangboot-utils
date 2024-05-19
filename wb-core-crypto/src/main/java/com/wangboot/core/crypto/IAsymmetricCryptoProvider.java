package com.wangboot.core.crypto;

/**
 * 非对称加解密提供者
 *
 * @author wwtg99
 */
public interface IAsymmetricCryptoProvider {

  /** 加密 */
  String encrypt(byte[] bytes);

  /** 解密 */
  byte[] decrypt(String data);
}
