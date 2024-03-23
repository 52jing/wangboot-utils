package com.wangboot.core.web.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 请求记录对象
 *
 * @author wwtg99
 */
@Data
@Builder
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class RequestRecord {
  /** 请求ID */
  private String requestId;
  /** 请求持续时间 */
  private long duration;
  /** 请求来源IP */
  private String remoteIp;
  /** 响应状态码 */
  private int status;
}
