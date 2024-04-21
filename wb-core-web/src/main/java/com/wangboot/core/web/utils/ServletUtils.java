package com.wangboot.core.web.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.servlet.ServletUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 客户端工具类
 *
 * @author wwtg99
 */
@Generated
public class ServletUtils {

  public static final String USER_AGENT_HEADER = "user-agent";
  public static final String ATTR_REQUEST_ID = "request_id";

  private ServletUtils() {}

  /** 获取 request attributes */
  @Nullable
  public static ServletRequestAttributes getRequestAttributes() {
    RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
    return Objects.nonNull(attributes) ? (ServletRequestAttributes) attributes : null;
  }

  /** 获取 request */
  @Nullable
  public static HttpServletRequest getRequest() {
    ServletRequestAttributes attributes = getRequestAttributes();
    return Objects.nonNull(attributes) ? attributes.getRequest() : null;
  }

  /** 获取 response */
  @Nullable
  public static HttpServletResponse getResponse() {
    ServletRequestAttributes attributes = getRequestAttributes();
    return Objects.nonNull(attributes) ? attributes.getResponse() : null;
  }

  /** 获取请求URI */
  @Nullable
  public static String getRequestURI(@Nullable HttpServletRequest request) {
    return Objects.nonNull(request) ? request.getRequestURI() : null;
  }

  @Nullable
  public static String getRequestURI() {
    return getRequestURI(getRequest());
  }

  /** 获取客户端IP */
  @NonNull
  public static String getRemoteIp(@Nullable HttpServletRequest request) {
    return Objects.nonNull(request) ? ServletUtil.getClientIP(request) : "";
  }

  @NonNull
  public static String getRemoteIp() {
    return getRemoteIp(getRequest());
  }

  /** 获取客户端 User Agent */
  @NonNull
  public static String getUserAgent(@Nullable HttpServletRequest request) {
    return Objects.nonNull(request) ? request.getHeader(USER_AGENT_HEADER) : "";
  }

  @NonNull
  public static String getUserAgent() {
    return getUserAgent(getRequest());
  }

  /** 获取 String 参数 */
  @Nullable
  public static String getParameter(
      @Nullable HttpServletRequest request, @Nullable String name, @Nullable String defaultValue) {
    return Objects.nonNull(request) && StringUtils.hasText(name)
        ? request.getParameter(name)
        : defaultValue;
  }

  /** 获取 String 参数 */
  @Nullable
  public static String getParameter(@Nullable String name, @Nullable String defaultValue) {
    return getParameter(getRequest(), name, defaultValue);
  }

  /** 获取 String 参数 */
  @Nullable
  public static String getParameter(@Nullable String name) {
    return getParameter(getRequest(), name, null);
  }

  /** 获取 Integer 参数 */
  @Nullable
  public static Integer getParameterToInt(
      @Nullable HttpServletRequest request, @Nullable String name, @Nullable Integer defaultValue) {
    return Objects.nonNull(request) && StringUtils.hasText(name)
        ? Convert.toInt(request.getParameter(name), defaultValue)
        : defaultValue;
  }

  /** 获取 Integer 参数 */
  @Nullable
  public static Integer getParameterToInt(@Nullable String name, @Nullable Integer defaultValue) {
    return getParameterToInt(getRequest(), name, defaultValue);
  }

  /** 获取 Integer 参数 */
  @Nullable
  public static Integer getParameterToInt(@Nullable String name) {
    return getParameterToInt(getRequest(), name, null);
  }

  /** 获取 Integer 参数 */
  @Nullable
  public static Long getParameterToLong(
      @Nullable HttpServletRequest request, @Nullable String name, @Nullable Long defaultValue) {
    return Objects.nonNull(request) && StringUtils.hasText(name)
        ? Convert.toLong(request.getParameter(name), defaultValue)
        : defaultValue;
  }

  /** 获取 Integer 参数 */
  @Nullable
  public static Long getParameterToLong(@Nullable String name, @Nullable Long defaultValue) {
    return getParameterToLong(getRequest(), name, defaultValue);
  }

  /** 获取 Integer 参数 */
  @Nullable
  public static Long getParameterToLong(@Nullable String name) {
    return getParameterToLong(getRequest(), name, null);
  }

  /** 获取 Boolean 参数 */
  @Nullable
  public static Boolean getParameterToBool(
      @Nullable HttpServletRequest request, @Nullable String name, @Nullable Boolean defaultValue) {
    return Objects.nonNull(request) && StringUtils.hasText(name)
        ? Convert.toBool(request.getParameter(name), defaultValue)
        : defaultValue;
  }

  /** 获取 Boolean 参数 */
  @Nullable
  public static Boolean getParameterToBool(@Nullable String name, @Nullable Boolean defaultValue) {
    return getParameterToBool(getRequest(), name, defaultValue);
  }

  /** 获取 Boolean 参数 */
  @Nullable
  public static Boolean getParameterToBool(@Nullable String name) {
    return getParameterToBool(getRequest(), name, null);
  }

  /** 获取请求参数数组 */
  @NonNull
  public static String[] getParameterValues(
      @Nullable HttpServletRequest request, @Nullable String name) {
    return Objects.nonNull(request) ? request.getParameterValues(name) : new String[] {};
  }

  /** 获取请求参数数组 */
  @NonNull
  public static String[] getParameterValues(@Nullable String name) {
    return getParameterValues(getRequest(), name);
  }

  /** 获取所有请求参数 */
  @NonNull
  public static Map<String, String[]> getParameterMap(@Nullable HttpServletRequest request) {
    return Objects.nonNull(request) ? request.getParameterMap() : new HashMap<>();
  }

  /** 获取所有请求参数 */
  @NonNull
  public static Map<String, String[]> getParameterMap() {
    return getParameterMap(getRequest());
  }

  /** 生成请求ID并保存到请求对象 */
  public static String generateRequestId(@Nullable HttpServletRequest request) {
    String rid = UUID.randomUUID().toString();
    if (Objects.nonNull(request)) {
      request.setAttribute(ATTR_REQUEST_ID, rid);
    }
    return rid;
  }

  /** 获取请求ID */
  public static String getRequestId(@Nullable HttpServletRequest request) {
    return Objects.nonNull(request) ? request.getAttribute(ATTR_REQUEST_ID).toString() : null;
  }

  /**
   * 将字符串渲染到客户端
   *
   * @param response 渲染对象
   * @param string 待渲染的字符串
   */
  public static void renderString(
      @NonNull HttpServletResponse response,
      @NonNull String string,
      int status,
      @NonNull String contentType) {
    try {
      response.setStatus(status);
      response.setContentType(contentType);
      response.setCharacterEncoding("utf-8");
      response.getWriter().print(string);
    } catch (IOException e) {
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * 将字符串渲染到客户端
   *
   * @param response 渲染对象
   * @param string 待渲染的字符串
   */
  public static void renderString(@NonNull HttpServletResponse response, @NonNull String string) {
    renderString(response, string, 200, "application/json");
  }

  /**
   * 是否是Ajax异步请求
   *
   * @param request 请求
   */
  public static boolean isAjaxRequest(@NonNull HttpServletRequest request) {
    String accept = request.getHeader("accept");
    if (accept != null && accept.contains("application/json")) {
      return true;
    }

    String xRequestedWith = request.getHeader("X-Requested-With");
    return xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest");
  }

  /** 下载文件 */
  public static void downloadFile(
      @NonNull InputStream inputStream, @NonNull HttpServletResponse response, int bufferSize)
      throws IOException {
    ServletOutputStream outputStream = response.getOutputStream();
    byte[] bytes = new byte[bufferSize];
    int len;
    while ((len = inputStream.read(bytes)) > 0) {
      outputStream.write(bytes, 0, len);
    }
    inputStream.close();
  }

  /** 下载文件 */
  public static void downloadFile(
      @NonNull InputStream inputStream, @NonNull HttpServletResponse response) throws IOException {
    downloadFile(inputStream, response, 1024);
  }
}
