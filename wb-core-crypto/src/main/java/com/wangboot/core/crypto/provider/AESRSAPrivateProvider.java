package com.wangboot.core.crypto.provider;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * AES RSA私钥加解密<br>
 * AES 加密请求响应体，RSA 加密密钥，双向加密
 *
 * @author wwtg99
 */
public class AESRSAPrivateProvider extends AbstractAESProvider {

  private final RSAPrivateProvider rsaPrivateProvider;

  public AESRSAPrivateProvider(byte[] privateKeyBytes, byte[] publicKeyBytes) {
    this.rsaPrivateProvider = new RSAPrivateProvider(privateKeyBytes, publicKeyBytes);
  }

  public AESRSAPrivateProvider(String privateKey, String publicKey) {
    this(SecureUtil.decode(privateKey), SecureUtil.decode(publicKey));
  }

  @Override
  public String encrypt(byte[] bytes) {
    if (Objects.isNull(bytes) || bytes.length == 0) {
      return "";
    }
    // 生成随机 AES 密钥
    byte[] key = this.generateSymmetricKey();
    // 使用 RSA 加密密钥
    this.setKey(this.rsaPrivateProvider.encrypt(key));
    // 使用 AES 加密
    return this.createAES(key).encryptBase64(bytes);
  }

  @Override
  public byte[] decrypt(String data) {
    if (StrUtil.isNotBlank(this.getKey()) && StrUtil.isNotBlank(data)) {
      // 使用 RSA 解密 AES 密钥
      byte[] aesKey = this.rsaPrivateProvider.decrypt(this.getKey());
      // 使用 AES 解密
      return this.createAES(aesKey).decryptStr(data).getBytes(StandardCharsets.UTF_8);
    }
    return new byte[0];
  }
}
