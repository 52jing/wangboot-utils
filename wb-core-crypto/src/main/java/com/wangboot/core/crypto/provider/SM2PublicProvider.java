package com.wangboot.core.crypto.provider;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import com.wangboot.core.crypto.IAsymmetricCryptoProvider;
import java.util.Base64;
import java.util.Objects;

/**
 * SM2 公钥加密
 *
 * @author wwtg99
 */
public class SM2PublicProvider implements IAsymmetricCryptoProvider {
  private final SM2 sm2;

  public SM2PublicProvider(byte[] privateKeyBytes, byte[] publicKeyBytes) {
    this.sm2 = new SM2(privateKeyBytes, publicKeyBytes);
  }

  public SM2PublicProvider(String privateKey, String publicKey) {
    this(SecureUtil.decode(privateKey), SecureUtil.decode(publicKey));
  }

  @Override
  public String encrypt(byte[] bytes) {
    if (Objects.isNull(bytes) || bytes.length == 0) {
      return "";
    }
    return this.sm2.encryptBase64(bytes, KeyType.PublicKey);
  }

  @Override
  public byte[] decrypt(String data) {
    if (StrUtil.isBlank(data)) {
      return new byte[0];
    }
    return Base64.getDecoder().decode(data);
  }
}
