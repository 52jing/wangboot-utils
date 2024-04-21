package com.wangboot.model.entity.controller;

import com.wangboot.core.auth.annotation.RestPermissionAction;
import com.wangboot.core.auth.authorization.resource.ApiResource;
import com.wangboot.model.entity.IRestfulService;
import com.wangboot.model.entity.IdEntity;
import com.wangboot.model.entity.request.IdListBody;
import com.wangboot.model.entity.utils.EntityUtils;
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
 * @param <I> 主键类型
 * @param <S> 服务类型
 * @param <T> 实体类型
 */
@Generated
public abstract class RestfulApiFullController<
        I extends Serializable, T extends IdEntity<I>, S extends IRestfulService<I, T>>
    implements IRestfulReadController<I, T>, IRestfulWriteController<I, T> {

  @Getter @Setter private ApplicationEventPublisher applicationEventPublisher;

  @Autowired @Getter private S entityService;

  @NonNull
  @Override
  public IRestfulService<I, T> getReadService() {
    return this.entityService;
  }

  @Override
  @NonNull
  public IRestfulService<I, T> getWriteService() {
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

  @PostMapping
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_CREATE)
  @NonNull
  public ResponseEntity<?> createApi(@Validated @RequestBody T obj) {
    return this.createResponse(obj);
  }

  @PutMapping("/{id}")
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_UPDATE)
  @NonNull
  public ResponseEntity<?> updateApi(@PathVariable I id, @Validated @RequestBody T obj) {
    EntityUtils.setEntityIdentifier(obj, id);
    return this.updateResponse(obj);
  }

  @DeleteMapping("/{id}")
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_DELETE)
  @NonNull
  public ResponseEntity<?> removeApi(@PathVariable I id) {
    return this.deleteByIdResponse(id);
  }

  @DeleteMapping
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_DELETE)
  @NonNull
  public ResponseEntity<?> batchRemoveApi(@Validated @RequestBody IdListBody<I> ids) {
    return this.batchDeleteByIdResponse(ids.getIds());
  }
}
