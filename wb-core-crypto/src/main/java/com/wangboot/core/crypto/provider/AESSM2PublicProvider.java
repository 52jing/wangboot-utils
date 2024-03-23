package com.wangboot.core.crypto.provider;

import cn.hutool.crypto.SecureUtil;
import java.nio.charset.StandardCharsets;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * AES SM2加解密<br>
 * AES 加密，SM2 加密密钥，解密密钥由 base64 编码
 *
 * @author wwtg99
 */
public class AESSM2PublicProvider extends AbstractAESProvider {
  private final SM2PublicProvider sm2PublicProvider;

  public AESSM2PublicProvider(byte[] privateKeyBytes, byte[] publicKeyBytes) {
    this.sm2PublicProvider = new SM2PublicProvider(privateKeyBytes, publicKeyBytes);
  }

  public AESSM2PublicProvider(String privateKey, String publicKey) {
    this(SecureUtil.decode(privateKey), SecureUtil.decode(publicKey));
  }

  @Override
  public String encrypt(@NonNull byte[] bytes) {
    // 生成随机 AES 密钥
    byte[] key = this.generateSymmetricKey();
    // 使用 SM2 加密密钥
    this.setKey(this.sm2PublicProvider.encrypt(key));
    // 使用 AES 加密
    return this.createAES(key).encryptBase64(bytes);
  }

  @NonNull
  @Override
  public byte[] decrypt(String data) {
    if (StringUtils.hasText(this.getKey()) && StringUtils.hasText(data)) {
      // 使用 Base64 解密 AES 密钥
      byte[] aesKey = this.sm2PublicProvider.decrypt(this.getKey());
      // AES 解密
      return this.createAES(aesKey).decryptStr(data).getBytes(StandardCharsets.UTF_8);
    }
    return new byte[0];
  }
}
