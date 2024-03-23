package com.wangboot.model.entity.event;

import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 操作日志实体
 *
 * @author wwtg99
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OperationLog {
  @Builder.Default private String event = "";

  @Builder.Default private String userId = "";

  @Builder.Default private String resource = "";

  @Builder.Default private Serializable resourceId = "";

  @Builder.Default private Object obj = null;
}
