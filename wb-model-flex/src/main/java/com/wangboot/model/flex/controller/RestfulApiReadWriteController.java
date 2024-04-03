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
 * 集成读写分离 Restful 接口的控制器抽象基类
 *
 * @param <R> 读实体
 * @param <RS> 读服务
 * @param <W> 写实体
 * @param <WS> 写服务
 */
public abstract class RestfulApiReadWriteController<
        R, RS extends IRestfulService<R>, W, WS extends IRestfulService<W>>
    implements IRestfulReadWriteController<R, W> {

  @Getter @Setter private ApplicationEventPublisher applicationEventPublisher;

  @Autowired @Getter private RS readEntityService;

  @Autowired @Getter private WS writeEntityService;

  @NonNull
  @Override
  public IRestfulService<R> getReadService() {
    return this.readEntityService;
  }

  @NonNull
  @Override
  public IRestfulService<W> getWriteService() {
    return this.writeEntityService;
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

  @PostMapping
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_CREATE)
  @NonNull
  public ResponseEntity<?> createApi(@Validated @RequestBody W obj) {
    return this.createDataResponse(obj);
  }

  @PutMapping("/{id}")
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_UPDATE)
  @NonNull
  public ResponseEntity<?> updateApi(@PathVariable Serializable id, @Validated @RequestBody W obj) {
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
