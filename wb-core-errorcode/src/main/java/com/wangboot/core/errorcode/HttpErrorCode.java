package com.wangboot.core.errorcode;

import lombok.Getter;

/**
 * HTTP 错误码
 *
 * @author wwtg99
 */
public enum HttpErrorCode implements IErrorCode {
  BAD_REQUEST(400, "400", "bad_request"),
  UNAUTHORIZED(401, "401", "unauthorized"),
  PAYMENT_REQUIRED(402, "402", "payment_required"),
  FORBIDDEN(403, "403", "forbidden"),
  NOT_FOUND(404, "404", "not_found"),
  METHOD_NOT_ALLOWED(405, "405", "method_not_allowed"),
  NOT_ACCEPTABLE(406, "406", "not_acceptable"),
  PROXY_AUTHENTICATION_REQUIRED(407, "407", "proxy_authentication_required"),
  REQUEST_TIMEOUT(408, "408", "request_timeout"),
  TOO_MANY_REQUESTS(429, "429", "too_many_requests"),
  INTERNAL_SERVER_ERROR(500, "500", "internal_server_error"),
  NOT_IMPLEMENTED(501, "501", "not_implemented"),
  BAD_GATEWAY(502, "502", "bad_gateway"),
  SERVICE_UNAVAILABLE(503, "503", "service_unavailable"),
  GATEWAY_TIMEOUT(504, "504", "gateway_timeout");

  @Getter private final int statusCode;
  @Getter private final String errCode;
  @Getter private final String errMsg;

  HttpErrorCode(int statusCode, String errCode, String errMsg) {
    this.statusCode = statusCode;
    this.errCode = errCode;
    this.errMsg = errMsg;
  }
}
