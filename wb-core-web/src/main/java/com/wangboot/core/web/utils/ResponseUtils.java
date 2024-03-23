package com.wangboot.core.web.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 响应工具类
 *
 * @author wwtg99
 */
public class ResponseUtils {

  private ResponseUtils() {}

  /** 返回成功 */
  @NonNull
  public static <T> ResponseEntity<T> success() {
    return ResponseEntity.ok().build();
  }

  /**
   * 返回成功
   *
   * @param responseBody: 响应体
   */
  @NonNull
  public static <T> ResponseEntity<T> success(@Nullable T responseBody) {
    return ResponseEntity.ok(responseBody);
  }

  /**
   * 创建成功
   *
   * @param responseBody: 响应体
   */
  @NonNull
  public static <T> ResponseEntity<T> created(@Nullable T responseBody) {
    return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
  }

  /** 删除成功 */
  @NonNull
  public static ResponseEntity<?> deleted() {
    return noContent();
  }

  /** 无内容 */
  @NonNull
  public static ResponseEntity<?> noContent() {
    return ResponseEntity.noContent().build();
  }

  /** 返回内部错误 */
  @NonNull
  public static <T> ResponseEntity<T> error() {
    return ResponseEntity.internalServerError().build();
  }

  /**
   * 返回错误
   *
   * @param responseBody: 响应体
   */
  @NonNull
  public static <T> ResponseEntity<T> error(@Nullable T responseBody, int status) {
    return ResponseEntity.status(status).body(responseBody);
  }
}
