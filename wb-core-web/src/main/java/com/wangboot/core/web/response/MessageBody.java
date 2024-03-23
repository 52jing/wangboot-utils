package com.wangboot.core.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 消息响应体<br>
 * 响应结构 { "message": "", "status": 200 }
 *
 * @author wwtg99
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageBody implements IStatusBody {
  private static final long serialVersionUID = 2L;

  @Builder.Default private String message = "";
  @Builder.Default private int status = HttpStatus.OK.value();
}
