package com.wangboot.model.flex.controller;

import com.wangboot.core.auth.annotation.RestPermissionAction;
import com.wangboot.core.auth.authorization.resource.ApiResource;
import com.wangboot.model.entity.IRestfulService;
import com.wangboot.model.entity.utils.EntityUtils;
import com.wangboot.model.flex.IdListBody;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 集成只写增删改 Restful 接口的控制器抽象基类
 *
 * @param <T> 实体类
 * @param <S> 服务类
 */
public abstract class RestfulApiWriteController<T, S extends IRestfulService<T>>
    implements IRestfulWriteController<T> {

  @Getter @Setter private ApplicationEventPublisher applicationEventPublisher;

  @Autowired @Getter private S entityService;

  @NonNull
  @Override
  public IRestfulService<T> getWriteService() {
    return this.entityService;
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
