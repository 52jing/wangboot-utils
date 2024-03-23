package com.wangboot.model.flex.controller;

import com.wangboot.core.auth.annotation.RestPermissionAction;
import com.wangboot.core.auth.authorization.resource.ApiResource;
import com.wangboot.model.entity.IRestfulService;
import com.wangboot.model.entity.utils.EntityUtils;
import com.wangboot.model.flex.IdListBody;
import java.io.Serializable;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 集成增、删、改、查 Restful 接口的控制器抽象基类
 *
 * @param <S> 服务类
 * @param <T> 实体类
 */
@Generated
public abstract class RestfulApiFullController<T, S extends IRestfulService<T>>
    implements IRestfulReadController<T>, IRestfulWriteController<T> {

  @Getter @Setter private ApplicationEventPublisher applicationEventPublisher;

  @Autowired @Getter private S entityService;

  @NonNull
  @Override
  public IRestfulService<T> getReadService() {
    return this.entityService;
  }

  @Override
  @NonNull
  public Class<T> getReadEntityClass() {
    return getReadService().getEntityClass();
  }

  @Override
  @NonNull
  public IRestfulService<T> getWriteService() {
    return this.entityService;
  }

  @Override
  @NonNull
  public Class<T> getWriteEntityClass() {
    return getWriteService().getEntityClass();
  }

  @GetMapping
  @NonNull
  public ResponseEntity<?> listApi() {
    return this.listPageResponse();
  }

  @GetMapping("/{id}")
  @NonNull
  public ResponseEntity<?> detailApi(@PathVariable Serializable id) {
    return this.detailResponse(id);
  }

  @PostMapping
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_CREATE)
  @NonNull
  public ResponseEntity<?> createApi(@Validated @RequestBody T obj) {
    return this.createDataResponse(obj);
  }

  @PutMapping("/{id}")
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_UPDATE)
  @NonNull
  public ResponseEntity<?> updateApi(@PathVariable Serializable id, @Validated @RequestBody T obj) {
    EntityUtils.setEntityIdentifier(obj, id);
    return this.updateDataResponse(obj);
  }

  @DeleteMapping("/{id}")
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_DELETE)
  @NonNull
  public ResponseEntity<?> removeApi(@PathVariable Serializable id) {
    return this.deleteDataByIdResponse(id);
  }

  @DeleteMapping
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_DELETE)
  @NonNull
  public ResponseEntity<?> batchRemoveApi(
      @Validated @RequestBody IdListBody<? extends Serializable> ids) {
    return this.batchDeleteDataByIdResponse(ids.getIds());
  }
}
