package com.wangboot.model.entity.controller;

import com.wangboot.core.auth.annotation.RestPermissionAction;
import com.wangboot.core.auth.authorization.resource.ApiResource;
import com.wangboot.model.entity.IRestfulService;
import com.wangboot.model.entity.IdEntity;
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
 * @param <I> 主键类型
 * @param <T> 实体类型
 * @param <S> 服务类型
 */
@Generated
public abstract class RestfulApiReadController<
        I extends Serializable, T extends IdEntity<I>, S extends IRestfulService<I, T>>
    implements IRestfulReadController<I, T> {

  @Getter @Setter private ApplicationEventPublisher applicationEventPublisher;

  @Autowired @Getter private S entityService;

  @NonNull
  @Override
  public IRestfulService<I, T> getReadService() {
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
  public ResponseEntity<?> detailApi(@PathVariable I id) {
    return this.viewResponse(id);
  }
}
