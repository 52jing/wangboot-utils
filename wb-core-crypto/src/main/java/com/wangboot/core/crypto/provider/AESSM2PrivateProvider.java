package com.wangboot.core.crypto.provider;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * AES SM2加解密<br>
 * AES 解密，SM2 解密密钥，加密密钥由 base64 编码
 *
 * @author wwtg99
 */
public class AESSM2PrivateProvider extends AbstractAESProvider {

  private final SM2PrivateProvider sm2PrivateProvider;

  public AESSM2PrivateProvider(byte[] privateKeyBytes, byte[] publicKeyBytes) {
    this.sm2PrivateProvider = new SM2PrivateProvider(privateKeyBytes, publicKeyBytes);
  }

  public AESSM2PrivateProvider(String privateKey, String publicKey) {
    this(SecureUtil.decode(privateKey), SecureUtil.decode(publicKey));
  }

  @Override
  public String encrypt(byte[] bytes) {
    if (Objects.isNull(bytes) || bytes.length == 0) {
      return "";
    }
    // 生成随机 AES 密钥
    byte[] key = this.generateSymmetricKey();
    // 使用 Base64 加密密钥
    this.setKey(this.sm2PrivateProvider.encrypt(key));
    // 使用 AES 加密
    return this.createAES(key).encryptBase64(bytes);
  }

  @Override
  public byte[] decrypt(String data) {
    if (StrUtil.isNotBlank(this.getKey()) && StrUtil.isNotBlank(data)) {
      // 使用 SM2 解密 AES 密钥
      byte[] aesKey = this.sm2PrivateProvider.decrypt(this.getKey());
      // AES 解密
      return this.createAES(aesKey).decryptStr(data).getBytes(StandardCharsets.UTF_8);
    }
    return new byte[0];
  }
}
