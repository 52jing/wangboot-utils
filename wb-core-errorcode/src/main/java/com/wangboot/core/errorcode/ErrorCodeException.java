package com.wangboot.core.errorcode;

import java.io.Serializable;
import lombok.Getter;

/**
 * 错误码异常
 *
 * @author wwtg99
 */
public class ErrorCodeException extends RuntimeException {

  @Getter private final int statusCode;
  @Getter private final String errCode;
  @Getter private final String errMsg;
  @Getter private final Serializable[] args;

  public ErrorCodeException(
      int statusCode, String errCode, String errMsg, Serializable[] args, Throwable cause) {
    super(errMsg, cause);
    this.statusCode = statusCode;
    this.errCode = errCode;
    this.errMsg = errMsg;
    this.args = args;
  }

  public ErrorCodeException(int statusCode, String errCode, String errMsg, Throwable cause) {
    this(statusCode, errCode, errMsg, null, cause);
  }

  public ErrorCodeException(int statusCode, String errCode, String errMsg, Serializable[] args) {
    super(errMsg);
    this.statusCode = statusCode;
    this.errCode = errCode;
    this.errMsg = errMsg;
    this.args = args;
  }

  public ErrorCodeException(int statusCode, String errCode, String errMsg) {
    super(errMsg);
    this.statusCode = statusCode;
    this.errCode = errCode;
    this.errMsg = errMsg;
    this.args = null;
  }

  public ErrorCodeException(IErrorCode errorCode, Serializable[] args, Throwable cause) {
    this(errorCode.getStatusCode(), errorCode.getErrCode(), errorCode.getErrMsg(), args, cause);
  }

  public ErrorCodeException(IErrorCode errorCode, Throwable cause) {
    this(errorCode.getStatusCode(), errorCode.getErrCode(), errorCode.getErrMsg(), cause);
  }

  public ErrorCodeException(IErrorCode errorCode, Serializable[] args) {
    this(errorCode.getStatusCode(), errorCode.getErrCode(), errorCode.getErrMsg(), args);
  }

  public ErrorCodeException(IErrorCode errorCode, String msg) {
    this(errorCode.getStatusCode(), errorCode.getErrCode(), msg);
  }

  public ErrorCodeException(IErrorCode errorCode) {
    this(errorCode.getStatusCode(), errorCode.getErrCode(), errorCode.getErrMsg());
  }
}
