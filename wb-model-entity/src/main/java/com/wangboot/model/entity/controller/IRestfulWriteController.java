package com.wangboot.model.entity.controller;

import com.wangboot.core.auth.utils.AuthUtils;
import com.wangboot.core.web.response.DetailBody;
import com.wangboot.core.web.utils.ResponseUtils;
import com.wangboot.core.web.utils.ServletUtils;
import com.wangboot.model.entity.FieldConstants;
import com.wangboot.model.entity.IRestfulService;
import com.wangboot.model.entity.IdEntity;
import com.wangboot.model.entity.event.IOperationEventPublisher;
import com.wangboot.model.entity.event.OperationEventType;
import com.wangboot.model.entity.exception.CreateFailedException;
import com.wangboot.model.entity.exception.DeleteFailedException;
import com.wangboot.model.entity.exception.UpdateFailedException;
import com.wangboot.model.entity.utils.EntityUtils;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface IRestfulWriteController<I extends Serializable, T extends IdEntity<I>>
    extends IOperationEventPublisher {

  /**
   * 获取当前用户ID
   *
   * @return 用户ID
   */
  @Override
  default String getUserId() {
    return AuthUtils.getUserId();
  }

  /**
   * 获取请求
   *
   * @return 请求
   */
  @Override
  @Nullable
  default HttpServletRequest getRequest() {
    return ServletUtils.getRequest();
  }

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
    if (EntityUtils.isOperationLogEnabled(getWriteService().getEntityClass())) {
      // 发布创建前操作事件
      this.publishOperationEvent(
          getWriteService().getEntityClass(), OperationEventType.BEFORE_CREATE_EVENT, obj);
    }
    // 执行创建
    boolean ret = getWriteService().createResource(obj);
    if (ret) {
      if (EntityUtils.isOperationLogEnabled(getWriteService().getEntityClass())) {
        // 发布创建操作事件
        this.publishOperationEvent(
            getWriteService().getEntityClass(),
            OperationEventType.CREATED_EVENT,
            obj.getId().toString(),
            obj);
      }
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
   * 批量创建对象
   *
   * @param obj 对象集合
   * @return 对象集合
   */
  @NonNull
  default Collection<T> batchCreate(@Nullable Collection<T> obj) {
    if (Objects.isNull(obj)) {
      throw new CreateFailedException();
    }
    if (EntityUtils.isOperationLogEnabled(getWriteService().getEntityClass())) {
      // 发布创建前操作事件
      obj.forEach(
          data ->
              this.publishOperationEvent(
                  getWriteService().getEntityClass(),
                  OperationEventType.BEFORE_CREATE_EVENT,
                  data));
    }
    boolean ret = getWriteService().batchCreateResources(obj);
    if (ret) {
      if (EntityUtils.isOperationLogEnabled(getWriteService().getEntityClass())) {
        // 发布创建操作事件
        obj.forEach(
            data ->
                this.publishOperationEvent(
                    getWriteService().getEntityClass(),
                    OperationEventType.CREATED_EVENT,
                    data.getId().toString(),
                    data));
      }
    }
    return obj;
  }

  /**
   * 批量创建对象接口
   *
   * @param obj 对象集合
   * @return 响应对象
   */
  @NonNull
  default ResponseEntity<?> batchCreateResponse(@Nullable Collection<T> obj) {
    return ResponseUtils.created(DetailBody.created(batchCreate(obj)));
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
    if (EntityUtils.isOperationLogEnabled(getWriteService().getEntityClass())) {
      // 发布更新前操作事件
      this.publishOperationEvent(
          getWriteService().getEntityClass(),
          OperationEventType.BEFORE_UPDATE_EVENT,
          obj.getId().toString(),
          obj);
    }
    // 执行更新
    boolean ret = getWriteService().updateResource(obj);
    if (ret) {
      if (EntityUtils.isOperationLogEnabled(getWriteService().getEntityClass())) {
        // 发布更新操作事件
        this.publishOperationEvent(
            getWriteService().getEntityClass(),
            OperationEventType.UPDATED_EVENT,
            obj.getId().toString(),
            obj);
      }
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
      throw new DeleteFailedException("");
    }
    if (EntityUtils.isOperationLogEnabled(getWriteService().getEntityClass())) {
      // 发布删除前操作事件
      this.publishOperationEvent(
          getWriteService().getEntityClass(),
          OperationEventType.BEFORE_DELETE_EVENT,
          entity.getId().toString(),
          entity);
    }
    // 执行删除
    boolean ret = getWriteService().deleteResource(entity);
    if (ret) {
      if (EntityUtils.isOperationLogEnabled(getWriteService().getEntityClass())) {
        // 发布删除操作事件
        this.publishOperationEvent(
            getWriteService().getEntityClass(),
            OperationEventType.DELETED_EVENT,
            entity.getId().toString(),
            entity);
      }
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
  @NonNull
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
    if (EntityUtils.isOperationLogEnabled(getWriteService().getEntityClass())) {
      // 发布删除前操作事件
      entities.forEach(
          et ->
              this.publishOperationEvent(
                  getWriteService().getEntityClass(),
                  OperationEventType.BEFORE_DELETE_EVENT,
                  et.getId().toString(),
                  et));
    }
    // 执行删除
    boolean ret = getWriteService().batchDeleteResources(entities);
    if (ret) {
      if (EntityUtils.isOperationLogEnabled(getWriteService().getEntityClass())) {
        // 发布删除操作事件
        entities.forEach(
            et ->
                this.publishOperationEvent(
                    getWriteService().getEntityClass(),
                    OperationEventType.DELETED_EVENT,
                    et.getId().toString(),
                    et));
      }
      return true;
    } else {
      throw new DeleteFailedException(
          ids.stream()
              .map(Objects::toString)
              .collect(Collectors.joining(FieldConstants.FIELD_SEP)));
    }
  }

  /**
   * 删除对象接口
   *
   * @param ids ID集合
   * @return 响应对象
   */
  @NonNull
  default ResponseEntity<?> batchDeleteByIdResponse(@Nullable Collection<I> ids) {
    batchDeleteById(ids);
    return ResponseUtils.deleted();
  }
}
