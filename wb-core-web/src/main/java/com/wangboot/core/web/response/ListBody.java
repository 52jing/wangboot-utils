package com.wangboot.core.web.response;

import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 列举对象响应体<br>
 * 响应结构 { "data": [], "total": 0, "page": 1, "pageSize": 10, "status": 200 }
 *
 * @author wwtg99
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListBody<E> implements IStatusBody {
  private static final long serialVersionUID = 4L;

  public static final long DEFAULT_PAGE = 1;
  public static final long DEFAULT_PAGE_SIZE = 10;

  /** 数据对象 */
  @Builder.Default private Collection<E> data = Collections.emptyList();

  /** 总数 */
  @Builder.Default private long total = 0;

  /** 当前页码 */
  @Builder.Default private long page = DEFAULT_PAGE;

  /** 每页数量 */
  @Builder.Default private long pageSize = DEFAULT_PAGE_SIZE;

  /** 状态码 */
  @Builder.Default private int status = HttpStatus.OK.value();

  public static <E> ListBody<E> ok(Collection<E> data) {
    return new ListBody<>(data, data.size(), 1L, data.size(), HttpStatus.OK.value());
  }
}
