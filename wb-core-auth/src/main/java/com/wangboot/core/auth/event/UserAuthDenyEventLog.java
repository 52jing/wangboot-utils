package com.wangboot.core.auth.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户认证授权拒绝事件日志
 *
 * @author wwtg99
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthDenyEventLog {
  /** 用户ID */
  private String userId;
  /** 来源IP */
  private String ip;
  /** 消息 */
  private String message;
  /** 请求方法 */
  private String method;
  /** 请求路径 */
  private String path;
}
