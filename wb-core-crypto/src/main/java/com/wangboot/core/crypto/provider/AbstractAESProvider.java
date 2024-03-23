package com.wangboot.core.crypto.provider;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.wangboot.core.crypto.IHybridCryptoProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

/**
 * AES 加解密抽象类
 *
 * @author wwtg99
 */
public abstract class AbstractAESProvider implements IHybridCryptoProvider {

  public static final int AES_KEY_SIZE = 128;

  @Getter @Setter private String key;

  @NonNull
  protected AES createAES(@NonNull byte[] aesKey) {
    return new AES(Mode.CBC.toString(), "PKCS7Padding", aesKey, aesKey);
  }

  /** 生成随机对称加密密钥 */
  @NonNull
  public byte[] generateSymmetricKey() {
    // 仅支持 AES
    return SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), AES_KEY_SIZE).getEncoded();
  }
}
