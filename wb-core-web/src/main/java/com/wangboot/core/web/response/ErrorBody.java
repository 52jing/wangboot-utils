package com.wangboot.core.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 错误响应体<br>
 * 响应结构 { "errCode": "", "message": "", "status": 200 }
 *
 * @author wwtg99
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorBody implements IStatusBody {
  private static final long serialVersionUID = 3L;

  /** 错误码 */
  @Builder.Default private String errCode = "";

  /** 错误内容 */
  @Builder.Default private String message = "";

  /** 状态码 */
  @Builder.Default private int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
}
