package com.wangboot.model.entity.controller;

import com.wangboot.core.web.exception.CreateFailedException;
import com.wangboot.core.web.exception.DeleteFailedException;
import com.wangboot.core.web.exception.UpdateFailedException;
import com.wangboot.core.web.response.DetailBody;
import com.wangboot.core.web.utils.ResponseUtils;
import com.wangboot.model.entity.IRestfulService;
import com.wangboot.model.entity.IdEntity;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface IRestfulWriteController<I extends Serializable, T extends IdEntity<I>> {
  /** 获取服务 */
  @NonNull
  IRestfulService<I, T> getWriteService();

  /**
   * 创建对象
   *
   * @param obj 对象数据
   * @return 数据
   */
  @NonNull
  default T create(@Nullable T obj) {
    if (Objects.isNull(obj)) {
      throw new CreateFailedException();
    }
    // 执行创建
    boolean ret = getWriteService().createResource(obj);
    if (ret) {
      return obj;
    } else {
      throw new CreateFailedException();
    }
  }

  /**
   * 创建对象接口
   *
   * @param obj 对象数据
   * @return 响应对象
   */
  @NonNull
  default ResponseEntity<?> createResponse(@Nullable T obj) {
    return ResponseUtils.created(DetailBody.created(create(obj)));
  }

  /**
   * 更新对象
   *
   * @param obj 对象数据
   * @return 数据
   */
  @NonNull
  default T update(@Nullable T obj) {
    if (Objects.isNull(obj)) {
      throw new UpdateFailedException("");
    }
    // 执行更新
    boolean ret = getWriteService().updateResource(obj);
    if (ret) {
      return obj;
    } else {
      throw new UpdateFailedException(obj.getId());
    }
  }

  /**
   * 更新对象接口
   *
   * @param obj 对象数据
   * @return 响应对象
   */
  @NonNull
  default ResponseEntity<?> updateResponse(@Nullable T obj) {
    return ResponseEntity.ok(DetailBody.updated(update(obj)));
  }

  /**
   * 删除对象
   *
   * @param id 对象ID
   * @return 是否成功
   */
  @NonNull
  default boolean deleteById(@Nullable I id) {
    T entity = getWriteService().viewResource(id);
    if (Objects.isNull(entity)) {
      throw new UpdateFailedException("");
    }
    // 执行更新
    boolean ret = getWriteService().deleteResource(entity);
    if (ret) {
      return true;
    } else {
      throw new DeleteFailedException(entity.getId());
    }
  }

  /**
   * 删除对象接口
   *
   * @param id ID
   * @return 响应对象
   */
  default ResponseEntity<?> deleteByIdResponse(@Nullable I id) {
    deleteById(id);
    return ResponseUtils.deleted();
  }

  /**
   * 批量删除对象
   *
   * @param ids ID集合
   * @return 是否成功
   */
  default boolean batchDeleteById(@Nullable Collection<I> ids) {
    if (Objects.isNull(ids) || ids.isEmpty()) {
      throw new DeleteFailedException("");
    }
    List<T> entities = getWriteService().viewResources(ids);
    // 执行删除
    boolean ret = getWriteService().batchDeleteResources(entities);
    if (ret) {
      return true;
    } else {
      throw new DeleteFailedException(
          ids.stream().map(Objects::toString).collect(Collectors.joining(",")));
    }
  }

  /**
   * 删除对象接口
   *
   * @param ids ID集合
   * @return 响应对象
   */
  default ResponseEntity<?> batchDeleteByIdResponse(@Nullable Collection<I> ids) {
    batchDeleteById(ids);
    return ResponseUtils.deleted();
  }
}
