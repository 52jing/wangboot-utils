package com.wangboot.core.crypto;

/**
 * 混合加解密提供者
 *
 * @author wwtg99
 */
public interface IHybridCryptoProvider extends IAsymmetricCryptoProvider {

  /** 获取对称加密密钥 */
  String getKey();

  /** 设置对称加密密钥 */
  void setKey(String key);
}
