package com.wangboot.core.utils;

/**
 * 位掩码工具
 *
 * @author wwtg99
 *     <p>flag 为二进制表示的一系列权限位，每一位为一个权限开关，1 为开启，0为关闭。
 *     <p>bit 为二进制表示的某一个开关，只有一位为 1，其余位都是 0。
 *     <p>例如：
 *     <p>int sw1 = 1 << 0; // 0001
 *     <p>int sw2 = 1 << 1; // 0010
 *     <p>int sw3 = 1 << 2; // 0100
 *     <p>int sw3 = 1 << 3; // 1000
 */
public class BitMaskUtils {

  private BitMaskUtils() {}

  /** 开启 */
  public static int enable(int flag, int bit) {
    return flag | bit;
  }

  /** 关闭 */
  public static int disable(int flag, int bit) {
    return flag & ~bit;
  }

  /** 是否开启 */
  public static boolean isAllowed(int flag, int bit) {
    return (flag & bit) == bit;
  }

  /** 是否关闭 */
  public static boolean isNotAllowed(int flag, int bit) {
    return (flag & bit) == 0;
  }
}
