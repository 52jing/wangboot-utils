package com.wangboot.core.web.handler;

import com.wangboot.core.errorcode.IErrorCode;
import com.wangboot.core.web.response.ErrorBody;
import com.wangboot.core.web.utils.I18NUtils;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * 基本错误码处理器
 *
 * @author wwtg99
 */
@Slf4j
@Generated
public class BaseErrorCodeHandler {

  @Getter @Setter private boolean debug;

  public BaseErrorCodeHandler(boolean debug) {
    this.debug = debug;
  }

  /** 统一错误码响应 翻译错误信息 */
  @NonNull
  protected ResponseEntity<ErrorBody> toResponse(
      int statusCode, String errCode, String errMsg, Object... args) {
    return ResponseEntity.status(statusCode)
        .body(new ErrorBody(errCode, I18NUtils.message(errMsg, args), statusCode));
  }

  /** 统一错误码响应 翻译错误信息 */
  @NonNull
  protected ResponseEntity<ErrorBody> toResponse(@NonNull IErrorCode errorCode) {
    return toResponse(errorCode.getStatusCode(), errorCode.getErrCode(), errorCode.getErrMsg());
  }

  /** 统一错误码响应 翻译错误信息 */
  @NonNull
  protected ResponseEntity<ErrorBody> toResponse(@NonNull IErrorCode errorCode, String msg) {
    return toResponse(errorCode.getStatusCode(), errorCode.getErrCode(), msg);
  }

  /** 统一异常处理 */
  protected void processException(@NonNull Exception e, String msg, boolean warning) {
    if (StringUtils.hasText(msg)) {
      if (warning) {
        log.warn(msg, isDebug() ? e : null);
      } else {
        log.error(msg, isDebug() ? e : null);
      }
    }
  }

  /** 统一异常处理 */
  protected void processWarning(@NonNull Exception e, String msg) {
    processException(e, msg, false);
  }

  /** 统一异常处理 */
  protected void processError(@NonNull Exception e, String msg) {
    processException(e, msg, true);
  }
}
