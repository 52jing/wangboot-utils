package com.wangboot.core.crypto;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangboot.core.crypto.provider.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;

@DisplayName("加解密测试")
public class CryptoTest {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode
  static class Obj {
    private String key;
    private int val;
  }

  @Test
  public void testCryptoProvider() {
    // RSA
    KeyPair pair1 = SecureUtil.generateKeyPair("RSA");
    // 私钥加密，公钥解密
    IAsymmetricCryptoProvider provider1 =
        new RSAPrivateProvider(
            Base64.encode(pair1.getPrivate().getEncoded()),
            Base64.encode(pair1.getPublic().getEncoded()));
    IAsymmetricCryptoProvider provider2 =
        new RSAPublicProvider(
            Base64.encode(pair1.getPrivate().getEncoded()),
            Base64.encode(pair1.getPublic().getEncoded()));
    Assertions.assertEquals("", provider1.encrypt(null));
    String data = RandomUtil.randomString(30);
    String encrypted = provider1.encrypt(data.getBytes(StandardCharsets.UTF_8));
    byte[] decrypted = provider2.decrypt(encrypted);
    Assertions.assertEquals(data, new String(decrypted, StandardCharsets.UTF_8));
    Assertions.assertEquals(0, provider2.decrypt("").length);
    // 公钥加密，私钥解密
    Assertions.assertEquals("", provider2.encrypt(null));
    encrypted = provider2.encrypt(data.getBytes(StandardCharsets.UTF_8));
    decrypted = provider1.decrypt(encrypted);
    Assertions.assertEquals(data, new String(decrypted, StandardCharsets.UTF_8));
    Assertions.assertEquals(0, provider1.decrypt(null).length);
    // SM2
    KeyPair pair2 = SecureUtil.generateKeyPair("SM2");
    // 私钥加密，公钥解密(Base64)
    IAsymmetricCryptoProvider provider3 =
        new SM2PrivateProvider(
            Base64.encode(pair2.getPrivate().getEncoded()),
            Base64.encode(pair2.getPublic().getEncoded()));
    IAsymmetricCryptoProvider provider4 =
        new SM2PublicProvider(
            Base64.encode(pair2.getPrivate().getEncoded()),
            Base64.encode(pair2.getPublic().getEncoded()));
    Assertions.assertEquals("", provider3.encrypt(null));
    encrypted = provider3.encrypt(data.getBytes(StandardCharsets.UTF_8));
    decrypted = provider4.decrypt(encrypted);
    Assertions.assertEquals(data, new String(decrypted, StandardCharsets.UTF_8));
    Assertions.assertEquals(0, provider4.decrypt("").length);
    // 公钥加密，私钥解密
    Assertions.assertEquals("", provider4.encrypt(null));
    encrypted = provider4.encrypt(data.getBytes(StandardCharsets.UTF_8));
    decrypted = provider3.decrypt(encrypted);
    Assertions.assertEquals(data, new String(decrypted, StandardCharsets.UTF_8));
    Assertions.assertEquals(0, provider3.decrypt(null).length);
    // AES RSA
    // 私钥加密，公钥解密
    AESRSAPrivateProvider provider5 =
        new AESRSAPrivateProvider(
            Base64.encode(pair1.getPrivate().getEncoded()),
            Base64.encode(pair1.getPublic().getEncoded()));
    AESRSAPublicProvider provider6 =
        new AESRSAPublicProvider(
            Base64.encode(pair1.getPrivate().getEncoded()),
            Base64.encode(pair1.getPublic().getEncoded()));
    Assertions.assertEquals("", provider5.encrypt(null));
    encrypted = provider5.encrypt(data.getBytes(StandardCharsets.UTF_8));
    provider6.setKey(provider5.getKey());
    decrypted = provider6.decrypt(encrypted);
    Assertions.assertEquals(data, new String(decrypted, StandardCharsets.UTF_8));
    Assertions.assertEquals(0, provider6.decrypt("").length);
    // 公钥加密，私钥解密
    Assertions.assertEquals("", provider6.encrypt(null));
    encrypted = provider6.encrypt(data.getBytes(StandardCharsets.UTF_8));
    provider5.setKey(provider6.getKey());
    decrypted = provider5.decrypt(encrypted);
    Assertions.assertEquals(data, new String(decrypted, StandardCharsets.UTF_8));
    Assertions.assertEquals(0, provider5.decrypt(null).length);
    // AES SM2
    // 私钥加密，公钥解密
    AESSM2PrivateProvider provider7 =
        new AESSM2PrivateProvider(
            Base64.encode(pair2.getPrivate().getEncoded()),
            Base64.encode(pair2.getPublic().getEncoded()));
    AESSM2PublicProvider provider8 =
        new AESSM2PublicProvider(
            Base64.encode(pair2.getPrivate().getEncoded()),
            Base64.encode(pair2.getPublic().getEncoded()));
    Assertions.assertEquals("", provider7.encrypt(null));
    encrypted = provider7.encrypt(data.getBytes(StandardCharsets.UTF_8));
    provider8.setKey(provider7.getKey());
    decrypted = provider8.decrypt(encrypted);
    Assertions.assertEquals(data, new String(decrypted, StandardCharsets.UTF_8));
    Assertions.assertEquals(0, provider8.decrypt("").length);
    // 公钥加密，私钥解密
    Assertions.assertEquals("", provider8.encrypt(null));
    encrypted = provider8.encrypt(data.getBytes(StandardCharsets.UTF_8));
    provider7.setKey(provider8.getKey());
    decrypted = provider7.decrypt(encrypted);
    Assertions.assertEquals(data, new String(decrypted, StandardCharsets.UTF_8));
    Assertions.assertEquals(0, provider7.decrypt(null).length);
  }

  @Test
  @SneakyThrows
  public void testCryptoProcessor() {
    Obj obj = new Obj(RandomUtil.randomString(10), RandomUtil.randomInt(100));
    ObjectMapper objectMapper = new ObjectMapper();
    KeyPair pairRsa = SecureUtil.generateKeyPair("RSA");
    KeyPair pairSm2 = SecureUtil.generateKeyPair("SM2");
    // AES_RSA
    String mode = CryptoProcessor.AES_RSA;
    CryptoProcessor processor =
        new CryptoProcessor(
            objectMapper,
            Base64.encode(pairRsa.getPrivate().getEncoded()),
            Base64.encode(pairRsa.getPublic().getEncoded()));
    // 加密
    Assertions.assertNull(processor.encryptDataToBody(mode, null));
    CryptoBody body = processor.encryptDataToBody(mode, obj);
    Assertions.assertNotNull(body);
    Assertions.assertTrue(body.getJsondata().length() > 0);
    Assertions.assertTrue(body.getId().length() > 0);
    Assertions.assertEquals(
        Base64Utils.encodeToString(mode.getBytes(StandardCharsets.UTF_8)), body.getMode());
    IHybridCryptoProvider provider =
        new AESRSAPublicProvider(
            pairRsa.getPrivate().getEncoded(), pairRsa.getPublic().getEncoded());
    provider.setKey(body.getId());
    byte[] decrypted = provider.decrypt(body.getJsondata());
    Obj obj1 = objectMapper.readValue(decrypted, Obj.class);
    Assertions.assertEquals(obj, obj1);
    // 解密
    String encrypted = provider.encrypt(objectMapper.writeValueAsBytes(obj));
    body =
        new CryptoBody(
            encrypted,
            provider.getKey(),
            Base64Utils.encodeToString(mode.getBytes(StandardCharsets.UTF_8)));
    decrypted = processor.decryptDataFromBytes(objectMapper.writeValueAsBytes(body));
    obj1 = objectMapper.readValue(decrypted, Obj.class);
    Assertions.assertEquals(obj, obj1);
    // AES_SM2
    mode = CryptoProcessor.AES_SM2;
    processor =
        new CryptoProcessor(
            objectMapper,
            Base64.encode(pairSm2.getPrivate().getEncoded()),
            Base64.encode(pairSm2.getPublic().getEncoded()));
    // 加密
    body = processor.encryptDataToBody(mode, obj);
    Assertions.assertNotNull(body);
    Assertions.assertTrue(body.getJsondata().length() > 0);
    Assertions.assertTrue(body.getId().length() > 0);
    Assertions.assertEquals(
        Base64Utils.encodeToString(mode.getBytes(StandardCharsets.UTF_8)), body.getMode());
    provider =
        new AESSM2PublicProvider(
            pairSm2.getPrivate().getEncoded(), pairSm2.getPublic().getEncoded());
    provider.setKey(body.getId());
    decrypted = provider.decrypt(body.getJsondata());
    obj1 = objectMapper.readValue(decrypted, Obj.class);
    Assertions.assertEquals(obj, obj1);
    // 解密
    encrypted = provider.encrypt(objectMapper.writeValueAsBytes(obj));
    body =
        new CryptoBody(
            encrypted,
            provider.getKey(),
            Base64Utils.encodeToString(mode.getBytes(StandardCharsets.UTF_8)));
    decrypted = processor.decryptDataFromBytes(objectMapper.writeValueAsBytes(body));
    obj1 = objectMapper.readValue(decrypted, Obj.class);
    Assertions.assertEquals(obj, obj1);
    // RSA
    mode = CryptoProcessor.RSA;
    processor =
        new CryptoProcessor(
            objectMapper,
            Base64.encode(pairRsa.getPrivate().getEncoded()),
            Base64.encode(pairRsa.getPublic().getEncoded()));
    // 加密
    body = processor.encryptDataToBody(mode, obj);
    Assertions.assertNotNull(body);
    Assertions.assertTrue(body.getJsondata().length() > 0);
    Assertions.assertEquals(0, body.getId().length());
    Assertions.assertEquals(
        Base64Utils.encodeToString(mode.getBytes(StandardCharsets.UTF_8)), body.getMode());
    IAsymmetricCryptoProvider provider1 =
        new RSAPublicProvider(pairRsa.getPrivate().getEncoded(), pairRsa.getPublic().getEncoded());
    decrypted = provider1.decrypt(body.getJsondata());
    obj1 = objectMapper.readValue(decrypted, Obj.class);
    Assertions.assertEquals(obj, obj1);
    // 解密
    encrypted = provider1.encrypt(objectMapper.writeValueAsBytes(obj));
    body =
        new CryptoBody(
            encrypted, "", Base64Utils.encodeToString(mode.getBytes(StandardCharsets.UTF_8)));
    decrypted = processor.decryptDataFromBytes(objectMapper.writeValueAsBytes(body));
    obj1 = objectMapper.readValue(decrypted, Obj.class);
    Assertions.assertEquals(obj, obj1);
    // 解密字段
    String s1 = RandomUtil.randomString(10);
    String e1 = provider1.encrypt(s1.getBytes(StandardCharsets.UTF_8));
    String d1 = processor.decryptString(mode, e1);
    Assertions.assertEquals(s1, d1);
    // 加密字段
    e1 = processor.encryptString(mode, s1);
    d1 = new String(provider1.decrypt(e1));
    Assertions.assertEquals(s1, d1);
    // SM2
    mode = CryptoProcessor.SM2;
    processor =
        new CryptoProcessor(
            objectMapper,
            Base64.encode(pairSm2.getPrivate().getEncoded()),
            Base64.encode(pairSm2.getPublic().getEncoded()));
    // 加密
    body = processor.encryptDataToBody(mode, obj);
    Assertions.assertNotNull(body);
    Assertions.assertTrue(body.getJsondata().length() > 0);
    Assertions.assertEquals(0, body.getId().length());
    Assertions.assertEquals(
        Base64Utils.encodeToString(mode.getBytes(StandardCharsets.UTF_8)), body.getMode());
    provider1 =
        new SM2PublicProvider(pairSm2.getPrivate().getEncoded(), pairSm2.getPublic().getEncoded());
    decrypted = provider1.decrypt(body.getJsondata());
    obj1 = objectMapper.readValue(decrypted, Obj.class);
    Assertions.assertEquals(obj, obj1);
    // 解密
    encrypted = provider1.encrypt(objectMapper.writeValueAsBytes(obj));
    body =
        new CryptoBody(
            encrypted, "", Base64Utils.encodeToString(mode.getBytes(StandardCharsets.UTF_8)));
    Assertions.assertEquals(0, processor.decryptDataFromBytes(new byte[0]).length);
    decrypted = processor.decryptDataFromBytes(objectMapper.writeValueAsBytes(body));
    obj1 = objectMapper.readValue(decrypted, Obj.class);
    Assertions.assertEquals(obj, obj1);
    // 解密字段
    String s2 = RandomUtil.randomString(10);
    String e2 = provider1.encrypt(s2.getBytes(StandardCharsets.UTF_8));
    String d2 = processor.decryptString(mode, e2);
    Assertions.assertEquals(s2, d2);
    // 加密字段
    e2 = processor.encryptString(mode, s2);
    d2 = new String(provider1.decrypt(e2));
    Assertions.assertEquals(s2, d2);
    // no provider
    Assertions.assertNull(processor.encryptDataToBody("no", obj));
    body = new CryptoBody(encrypted, "", "no");
    byte[] bytes = processor.decryptDataFromBytes(objectMapper.writeValueAsBytes(body));
    Assertions.assertEquals(0, bytes.length);
    Assertions.assertEquals("", processor.encryptString("no", s1));
    Assertions.assertEquals("", processor.decryptString("no", s1));
    Assertions.assertEquals(
        "",
        processor.encryptString(
            Base64Utils.encodeToString(mode.getBytes(StandardCharsets.UTF_8)), ""));
    Assertions.assertEquals(
        "",
        processor.decryptString(
            Base64Utils.encodeToString(mode.getBytes(StandardCharsets.UTF_8)), ""));
  }
}
