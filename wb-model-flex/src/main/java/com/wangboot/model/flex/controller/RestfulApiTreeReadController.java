package com.wangboot.model.flex.controller;

import com.wangboot.model.entity.IRestfulService;
import com.wangboot.model.entity.RequestConstants;
import lombok.Generated;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 集成只读查 Restful 接口的树状控制器抽象基类
 *
 * @param <T> 实体类
 * @param <S> 服务类
 */
@Generated
public abstract class RestfulApiTreeReadController<T, S extends IRestfulService<T>>
    extends RestfulApiReadController<T, S> {

  @GetMapping("/" + RequestConstants.PATH_ROOT)
  @NonNull
  public ResponseEntity<?> listRootApi() {
    return this.listRootResponse();
  }

  @GetMapping("/{id}/" + RequestConstants.PATH_CHILDREN)
  @NonNull
  public ResponseEntity<?> listChildrenApi(@PathVariable String id) {
    return this.listDirectChildrenResponse(id);
  }
}
