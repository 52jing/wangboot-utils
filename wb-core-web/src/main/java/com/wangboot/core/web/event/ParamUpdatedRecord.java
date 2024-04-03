package com.wangboot.core.web.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 配置更新记录
 *
 * @author wwtg99
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ParamUpdatedRecord {

  private String key = "";
  private String val = "";
}
