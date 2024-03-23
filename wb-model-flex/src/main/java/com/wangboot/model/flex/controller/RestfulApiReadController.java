package com.wangboot.model.flex.controller;

import com.wangboot.core.auth.annotation.RestPermissionAction;
import com.wangboot.core.auth.authorization.resource.ApiResource;
import com.wangboot.model.entity.IRestfulService;
import java.io.Serializable;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 集成只读查 Restful 接口的控制器抽象基类
 *
 * @param <T> 实体类
 * @param <S> 服务类
 */
@Generated
public abstract class RestfulApiReadController<T, S extends IRestfulService<T>>
    implements IRestfulReadController<T> {

  @Getter @Setter private ApplicationEventPublisher applicationEventPublisher;

  @Autowired @Getter private S entityService;

  @NonNull
  @Override
  public IRestfulService<T> getReadService() {
    return this.entityService;
  }

  @GetMapping
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_VIEW)
  @NonNull
  public ResponseEntity<?> listApi() {
    return this.listPageResponse();
  }

  @GetMapping("/{id}")
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_VIEW)
  @NonNull
  public ResponseEntity<?> detailApi(@PathVariable Serializable id) {
    return this.detailResponse(id);
  }
}
