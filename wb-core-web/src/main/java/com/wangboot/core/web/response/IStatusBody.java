package com.wangboot.core.web.response;

import java.io.Serializable;

/**
 * 含状态码响应体
 *
 * @author wwtg99
 */
public interface IStatusBody extends Serializable {
  int getStatus();

  void setStatus(int status);
}
