package com.wangboot.core.utils;

import cn.hutool.core.codec.Base64;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.imageio.ImageIO;

/**
 * 对象工具类
 *
 * @author wwtg99
 */
public class ObjectUtils {

  private ObjectUtils() {}

  /**
   * 类是否实现了某个接口
   *
   * @param clazz 待检查类
   * @param targetInterface 目标接口类
   * @return boolean
   */
  public static boolean hasInterface(Class<?> clazz, Class<?> targetInterface) {
    // 获取类的所有接口
    Class<?>[] interfaces = clazz.getInterfaces();
    // 遍历所有接口，判断是否与目标接口匹配
    for (Class<?> i : interfaces) {
      if (targetInterface.isAssignableFrom(i)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 获取范型参数的类
   *
   * @param type 类型
   * @param index 范型参数索引
   * @param <T> 类
   * @return 类
   */
  @SuppressWarnings("unchecked")
  public static <T> Class<T> getTypeArgumentClass(Type type, int index) {
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      // 获取泛型参数的类型
      Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
      if (index >= 0 && index < actualTypeArguments.length) {
        Type at = actualTypeArguments[index];
        if (at instanceof Class) {
          return (Class<T>) at;
        }
      }
    }
    throw new IllegalArgumentException("Invalid type argument!");
  }

  /** 图片转字节码 */
  public static String image2Base64(BufferedImage image, String formatName) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(image, formatName, byteArrayOutputStream);
    byte[] imageBytes = byteArrayOutputStream.toByteArray();
    return Base64.encode(imageBytes);
  }

  /** 图片转字节码 */
  public static String image2Base64(BufferedImage image) throws IOException {
    return image2Base64(image, "png");
  }
}
