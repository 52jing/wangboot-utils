package com.wangboot.core.crypto.provider;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.wangboot.core.crypto.IAsymmetricCryptoProvider;
import java.util.Base64;
import java.util.Objects;

/**
 * RSA 公钥加密解密
 *
 * @author wwtg99
 */
public class RSAPublicProvider implements IAsymmetricCryptoProvider {

  private final RSA rsa;

  public RSAPublicProvider(byte[] privateKeyBytes, byte[] publicKeyBytes) {
    this.rsa = new RSA(privateKeyBytes, publicKeyBytes);
  }

  public RSAPublicProvider(String privateKey, String publicKey) {
    this(SecureUtil.decode(privateKey), SecureUtil.decode(publicKey));
  }

  @Override
  public String encrypt(byte[] bytes) {
    if (Objects.isNull(bytes) || bytes.length == 0) {
      return "";
    }
    return this.rsa.encryptBase64(bytes, KeyType.PublicKey);
  }

  @Override
  public byte[] decrypt(String data) {
    if (StrUtil.isBlank(data)) {
      return new byte[0];
    }
    return this.rsa.decrypt(Base64.getDecoder().decode(data), KeyType.PublicKey);
  }
}
