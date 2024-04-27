package com.wangboot.model.entity.controller;

import com.wangboot.core.auth.annotation.RestPermissionAction;
import com.wangboot.core.auth.authorization.resource.ApiResource;
import com.wangboot.core.web.exception.NotSupportedComponentException;
import com.wangboot.model.entity.IRestfulService;
import com.wangboot.model.entity.IdEntity;
import com.wangboot.model.entity.RequestConstants;
import com.wangboot.model.entity.request.IdListBody;
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
 * @param <I> 主键类型
 * @param <R> 读实体类型
 * @param <RS> 读服务类型
 * @param <W> 写实体类型
 * @param <WS> 写服务类型
 * @author wwtg99
 */
public abstract class RestfulApiReadWriteController<
        I extends Serializable,
        R extends IdEntity<I>,
        RS extends IRestfulService<I, R>,
        W extends IdEntity<I>,
        WS extends IRestfulService<I, W>>
    extends RestfulBaseController implements IRestfulReadWriteController<I, R, W> {

  @Getter @Setter private ApplicationEventPublisher applicationEventPublisher;

  @Autowired @Getter private RS readEntityService;

  @Autowired @Getter private WS writeEntityService;

  @NonNull
  @Override
  public IRestfulService<I, R> getReadService() {
    return this.readEntityService;
  }

  @NonNull
  @Override
  public IRestfulService<I, W> getWriteService() {
    return this.writeEntityService;
  }

  @GetMapping
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_VIEW)
  @NonNull
  public ResponseEntity<?> listApi() {
    if (!isEnabledApi(ControllerApi.LIST)) {
      throw new NotSupportedComponentException("Invalid list api for " + this.getClass().getName());
    }
    return this.listPageResponse();
  }

  @GetMapping("/{id}")
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_VIEW)
  @NonNull
  public ResponseEntity<?> detailApi(@PathVariable I id) {
    if (!isEnabledApi(ControllerApi.DETAIL)) {
      throw new NotSupportedComponentException(
          "Invalid detail api for " + this.getClass().getName());
    }
    return this.viewResponse(id);
  }

  @PostMapping
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_CREATE)
  @NonNull
  public ResponseEntity<?> createApi(@Validated @RequestBody W obj) {
    if (!isEnabledApi(ControllerApi.CREATE)) {
      throw new NotSupportedComponentException(
          "Invalid create api for " + this.getClass().getName());
    }
    return this.createResponse(obj);
  }

  @PutMapping("/{id}")
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_UPDATE)
  @NonNull
  public ResponseEntity<?> updateApi(@PathVariable I id, @Validated @RequestBody W obj) {
    if (!isEnabledApi(ControllerApi.UPDATE)) {
      throw new NotSupportedComponentException(
          "Invalid update api for " + this.getClass().getName());
    }
    obj.setId(id);
    return this.updateResponse(obj);
  }

  @DeleteMapping("/{id}")
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_DELETE)
  @NonNull
  public ResponseEntity<?> removeApi(@PathVariable I id) {
    if (!isEnabledApi(ControllerApi.REMOVE)) {
      throw new NotSupportedComponentException(
          "Invalid remove api for " + this.getClass().getName());
    }
    return this.deleteByIdResponse(id);
  }

  @DeleteMapping
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_DELETE)
  @NonNull
  public ResponseEntity<?> batchRemoveApi(@Validated @RequestBody IdListBody<I> ids) {
    if (!isEnabledApi(ControllerApi.BATCH_REMOVE)) {
      throw new NotSupportedComponentException(
          "Invalid batch remove api for " + this.getClass().getName());
    }
    return this.batchDeleteByIdResponse(ids.getIds());
  }

  @GetMapping("/" + RequestConstants.PATH_ROOT)
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_VIEW)
  @NonNull
  public ResponseEntity<?> listRootApi() {
    if (!isEnabledApi(ControllerApi.LIST_ROOT)) {
      throw new NotSupportedComponentException(
          "Invalid list root api for " + this.getClass().getName());
    }
    return this.listRootResponse();
  }

  @GetMapping("/{id}/" + RequestConstants.PATH_CHILDREN)
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_VIEW)
  @NonNull
  public ResponseEntity<?> listChildrenApi(@PathVariable I id) {
    if (!isEnabledApi(ControllerApi.LIST_CHILDREN)) {
      throw new NotSupportedComponentException(
          "Invalid list children api for " + this.getClass().getName());
    }
    return this.listDirectChildrenResponse(id);
  }
}
