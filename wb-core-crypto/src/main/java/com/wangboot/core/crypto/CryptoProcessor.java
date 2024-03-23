package com.wangboot.core.crypto;

import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangboot.core.crypto.provider.AESRSAPrivateProvider;
import com.wangboot.core.crypto.provider.AESSM2PrivateProvider;
import com.wangboot.core.crypto.provider.RSAPrivateProvider;
import com.wangboot.core.crypto.provider.SM2PrivateProvider;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

/**
 * 加解密处理器
 *
 * @author wwtg99
 */
public class CryptoProcessor {

  public static final String AES_RSA = "aes_rsa";
  public static final String AES_SM2 = "aes_sm2";
  public static final String RSA = "rsa";
  public static final String SM2 = "sm2";

  private final ObjectMapper objectMapper;
  private final byte[] privateKeyBytes;
  private final byte[] publicKeyBytes;
  private final Map<String, IAsymmetricCryptoProvider> cryptoProviderMap = new HashMap<>();

  public CryptoProcessor(
      @NonNull ObjectMapper objectMapper, @NonNull String privateKey, @NonNull String publicKey) {
    this.objectMapper = objectMapper;
    this.privateKeyBytes = SecureUtil.decode(privateKey);
    this.publicKeyBytes = SecureUtil.decode(publicKey);
  }

  /** 加密响应数据 */
  @Nullable
  public CryptoBody encryptDataToBody(String mode, @NonNull Object data)
      throws JsonProcessingException {
    // 获取提供者
    IAsymmetricCryptoProvider provider = this.getAsymmetricCryptoProvider(mode);
    if (Objects.isNull(provider)) {
      return null;
    }
    // 构建响应对象
    CryptoBody body = new CryptoBody();
    body.setJsondata(provider.encrypt(this.objectMapper.writeValueAsBytes(data)));
    String key = "";
    if (provider instanceof IHybridCryptoProvider) {
      key = ((IHybridCryptoProvider) provider).getKey();
    }
    body.setId(key);
    body.setMode(Base64Utils.encodeToString(mode.getBytes(StandardCharsets.UTF_8)));
    return body;
  }

  /** 解密请求数据 */
  @NonNull
  public byte[] decryptDataFromBytes(@NonNull byte[] bytes) throws IOException {
    if (bytes.length <= 0) {
      return new byte[0];
    }
    // 解析对象
    CryptoBody body = this.objectMapper.readValue(bytes, CryptoBody.class);
    // 获取模式
    String mode = new String(Base64Utils.decodeFromString(body.getMode()));
    // 获取提供者
    IAsymmetricCryptoProvider provider = this.getAsymmetricCryptoProvider(mode);
    if (Objects.isNull(provider)) {
      return new byte[0];
    }
    // 设置对称加密密钥
    if (provider instanceof IHybridCryptoProvider) {
      ((IHybridCryptoProvider) provider).setKey(body.getId());
    }
    // 解密
    return provider.decrypt(body.getJsondata());
  }

  /** 加密字段 */
  @NonNull
  public String encryptString(String mode, @NonNull String data) {
    if (!StringUtils.hasText(data)) {
      return "";
    }
    // 获取非对称解密提供者
    IAsymmetricCryptoProvider provider = this.getAsymmetricCryptoProvider(mode);
    if (Objects.isNull(provider)) {
      return "";
    }
    // 加密
    return provider.encrypt(data.getBytes(StandardCharsets.UTF_8));
  }

  /** 解密字段 */
  @NonNull
  public String decryptString(String mode, @NonNull String data) {
    if (!StringUtils.hasText(data)) {
      return "";
    }
    // 获取非对称解密提供者
    IAsymmetricCryptoProvider provider = this.getAsymmetricCryptoProvider(mode);
    if (Objects.isNull(provider)) {
      return "";
    }
    // 解密
    return new String(provider.decrypt(data), StandardCharsets.UTF_8);
  }

  /** 获取非对称加解密提供者 */
  @Nullable
  public IAsymmetricCryptoProvider getAsymmetricCryptoProvider(String mode) {
    if (this.cryptoProviderMap.containsKey(mode)) {
      return this.cryptoProviderMap.get(mode);
    } else {
      IAsymmetricCryptoProvider provider;
      switch (mode) {
        case RSA:
          provider = new RSAPrivateProvider(this.privateKeyBytes, this.publicKeyBytes);
          this.cryptoProviderMap.put(RSA, provider);
          return provider;
        case SM2:
          provider = new SM2PrivateProvider(this.privateKeyBytes, this.publicKeyBytes);
          this.cryptoProviderMap.put(SM2, provider);
          return provider;
        case AES_RSA:
          provider = new AESRSAPrivateProvider(this.privateKeyBytes, this.publicKeyBytes);
          this.cryptoProviderMap.put(AES_RSA, provider);
          return provider;
        case AES_SM2:
          provider = new AESSM2PrivateProvider(this.privateKeyBytes, this.publicKeyBytes);
          this.cryptoProviderMap.put(AES_SM2, provider);
          return provider;
      }
    }
    return null;
  }
}
