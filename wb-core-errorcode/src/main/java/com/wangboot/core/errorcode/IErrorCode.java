package com.wangboot.core.errorcode;

/**
 * 错误码接口
 *
 * @author wwtg99
 */
public interface IErrorCode {
  int getStatusCode();

  String getErrCode();

  String getErrMsg();
}
