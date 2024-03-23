package com.wangboot.core.web.event;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.Generated;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 基础请求事件
 *
 * @author wwtg99
 */
@Generated
public abstract class BaseRequestEvent<T> extends ApplicationEvent {

  @Getter @Nullable private final HttpServletRequest request;

  @Getter @NonNull private final OffsetDateTime createTime;

  public BaseRequestEvent(T source, @Nullable HttpServletRequest request) {
    this(source, request, null);
  }

  public BaseRequestEvent(
      T source, @Nullable HttpServletRequest request, @Nullable OffsetDateTime createTime) {
    super(source);
    this.request = request;
    this.createTime = Objects.isNull(createTime) ? OffsetDateTime.now() : createTime;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public T getSourceObject() {
    return (T) Optional.of(getSource()).get();
  }
}
