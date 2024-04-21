package com.wangboot.model.entity.controller;

import com.wangboot.core.auth.annotation.RestPermissionAction;
import com.wangboot.core.auth.authorization.resource.ApiResource;
import com.wangboot.model.entity.IRestfulService;
import com.wangboot.model.entity.ITreeEntity;
import com.wangboot.model.entity.RequestConstants;
import java.io.Serializable;
import lombok.Generated;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 集成增、删、改、查 Restful 接口的树状控制器抽象基类
 *
 * @param <I> 主键类型
 * @param <S> 服务类型
 * @param <T> 实体类型
 */
@Generated
public abstract class RestfulApiTreeFullController<
        I extends Serializable, T extends ITreeEntity<I>, S extends IRestfulService<I, T>>
    extends RestfulApiFullController<I, T, S> {

  @GetMapping("/" + RequestConstants.PATH_ROOT)
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_VIEW)
  @NonNull
  public ResponseEntity<?> listRootApi() {
    return this.listRootResponse();
  }

  @GetMapping("/{id}/" + RequestConstants.PATH_CHILDREN)
  @RestPermissionAction(ApiResource.REST_PERMISSION_ACTION_VIEW)
  @NonNull
  public ResponseEntity<?> listChildrenApi(@PathVariable I id) {
    return this.listDirectChildrenResponse(id);
  }
}
