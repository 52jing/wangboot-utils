package com.wangboot.core.web.response;

import com.wangboot.core.web.utils.I18NUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 详情响应体<br>
 * 响应结构 { "message": "", "data": {}, "status": 200 }
 *
 * @author wwtg99
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailBody<E> implements IStatusBody {
  public static final String OK_MESSAGE = "ok";
  public static final String CREATED_MESSAGE = "message.created";
  public static final String UPDATED_MESSAGE = "message.updated";

  private static final long serialVersionUID = 5L;

  /** 消息内容 */
  @Builder.Default private String message = OK_MESSAGE;

  /** 数据对象 */
  @Builder.Default private E data = null;

  /** 状态码 */
  @Builder.Default private int status = HttpStatus.OK.value();

  public static <E> DetailBody<E> ok(E data) {
    return new DetailBody<>(I18NUtils.message(OK_MESSAGE), data, HttpStatus.OK.value());
  }

  public static <E> DetailBody<E> created(E data) {
    return new DetailBody<>(I18NUtils.message(CREATED_MESSAGE), data, HttpStatus.CREATED.value());
  }

  public static <E> DetailBody<E> updated(E data) {
    return new DetailBody<>(I18NUtils.message(UPDATED_MESSAGE), data, HttpStatus.OK.value());
  }
}
