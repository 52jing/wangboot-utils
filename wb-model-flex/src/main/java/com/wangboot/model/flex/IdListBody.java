package com.wangboot.model.flex;

import java.io.Serializable;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 批量ID请求体
 *
 * @author wwtg99
 */
@Data
@Accessors(chain = true)
public class IdListBody<T extends Serializable> implements Serializable {

  @NotNull(message = "message.ids_not_empty")
  private Collection<T> ids;
}
