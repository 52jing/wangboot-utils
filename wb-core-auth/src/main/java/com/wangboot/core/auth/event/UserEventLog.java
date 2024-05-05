package com.wangboot.core.auth.event;

import com.wangboot.core.auth.context.ILoginUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 用户事件记录
 *
 * @author wwtg99
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserEventLog {
  /** 事件名称 */
  private String event;
  /** 事件状态 */
  @NonNull private LogStatus status;
  /** 用户名 */
  private String username;
  /** 登录用户 */
  @Nullable private ILoginUser loginUser;
  /** 消息 */
  private String message;
}
