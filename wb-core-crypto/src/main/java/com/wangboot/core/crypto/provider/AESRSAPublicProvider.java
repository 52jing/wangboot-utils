package com.wangboot.core.crypto.provider;

import cn.hutool.crypto.SecureUtil;
import java.nio.charset.StandardCharsets;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * AES RSA公钥加解密<br>
 * AES 加密请求响应体，RSA 加密密钥，双向加密
 *
 * @author wwtg99
 */
public class AESRSAPublicProvider extends AbstractAESProvider {
  private final RSAPublicProvider rsaPublicProvider;

  public AESRSAPublicProvider(byte[] privateKeyBytes, byte[] publicKeyBytes) {
    this.rsaPublicProvider = new RSAPublicProvider(privateKeyBytes, publicKeyBytes);
  }

  public AESRSAPublicProvider(String privateKey, String publicKey) {
    this(SecureUtil.decode(privateKey), SecureUtil.decode(publicKey));
  }

  @Override
  public String encrypt(@NonNull byte[] bytes) {
    // 生成随机 AES 密钥
    byte[] key = this.generateSymmetricKey();
    // 使用 RSA 加密密钥
    this.setKey(this.rsaPublicProvider.encrypt(key));
    // 使用 AES 加密
    return this.createAES(key).encryptBase64(bytes);
  }

  @Override
  @NonNull
  public byte[] decrypt(String data) {
    if (StringUtils.hasText(this.getKey()) && StringUtils.hasText(data)) {
      // 使用 RSA 解密 AES 密钥
      byte[] aesKey = this.rsaPublicProvider.decrypt(this.getKey());
      // 使用 AES 解密
      return this.createAES(aesKey).decryptStr(data).getBytes(StandardCharsets.UTF_8);
    }
    return new byte[0];
  }
}
