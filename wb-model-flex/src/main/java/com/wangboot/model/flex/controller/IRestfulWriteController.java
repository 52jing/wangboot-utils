package com.wangboot.model.flex.controller;

import com.wangboot.core.web.exception.CreateFailedException;
import com.wangboot.core.web.exception.DeleteFailedException;
import com.wangboot.core.web.exception.UpdateFailedException;
import com.wangboot.core.web.response.DetailBody;
import com.wangboot.core.web.utils.ResponseUtils;
import com.wangboot.model.entity.IRestfulService;
import com.wangboot.model.entity.event.OperationEventType;
import com.wangboot.model.entity.request.DeletionPolicy;
import com.wangboot.model.entity.utils.EntityUtils;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Restful 只写控制器接口
 *
 * @param <T> 实体类型
 */
public interface IRestfulWriteController<T> extends IRestfulController {

  /** 获取服务 */
  @NonNull
  IRestfulService<T> getWriteService();

  /** 获取实体类型 */
  @NonNull
  default Class<T> getWriteEntityClass() {
    return getWriteService().getEntityClass();
  }

  /**
   * 配置方法<br>
   * 获取删除策略
   *
   * @return 删除策略
   */
  default DeletionPolicy getDeletionPolicy() {
    return DeletionPolicy.DO_NOTHING;
  }

  /**
   * 创建对象
   *
   * @param obj 对象数据
   * @return 数据
   */
  @NonNull
  default T createData(@Nullable T obj) {
    if (Objects.isNull(obj)) {
      throw new CreateFailedException();
    }
    // 发布创建前操作事件
    this.publishOperationEvent(
        OperationEventType.BEFORE_CREATE_EVENT, obj.getClass().getSimpleName(), obj);
    // 执行创建
    boolean ret = getWriteService().createObject(obj);
    if (ret) {
      // 发布创建操作事件
      this.publishOperationEvent(
          OperationEventType.CREATED_EVENT,
          obj.getClass().getSimpleName(),
          EntityUtils.getEntityIdentifierStr(obj),
          obj);
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
  default ResponseEntity<?> createDataResponse(@Nullable T obj) {
    return ResponseUtils.created(DetailBody.created(createData(obj)));
  }

  /**
   * 更新对象
   *
   * @param obj 对象数据
   * @return 数据
   */
  @NonNull
  default T updateData(@Nullable T obj) {
    if (Objects.isNull(obj)) {
      throw new UpdateFailedException("");
    }
    Serializable id = EntityUtils.getEntityIdentifier(obj);
    // 发布更新前操作事件
    this.publishOperationEvent(
        OperationEventType.BEFORE_UPDATE_EVENT, obj.getClass().getSimpleName(), id.toString(), obj);
    // 执行更新
    boolean ret = getWriteService().updateObject(obj);
    if (ret) {
      // 发布更新操作事件
      this.publishOperationEvent(
          OperationEventType.UPDATED_EVENT, obj.getClass().getSimpleName(), id.toString(), obj);
      return obj;
    } else {
      throw new UpdateFailedException(id);
    }
  }

  /**
   * 更新对象接口
   *
   * @param obj 对象数据
   * @return 响应对象
   */
  @NonNull
  default ResponseEntity<?> updateDataResponse(@Nullable T obj) {
    return ResponseEntity.ok(DetailBody.updated(updateData(obj)));
  }

  /**
   * 删除对象
   *
   * @param id ID
   */
  default void deleteDataById(@Nullable Serializable id) {
    if (Objects.isNull(id)) {
      throw new DeleteFailedException("");
    }
    // 发布删除前操作事件
    this.publishOperationEvent(
        OperationEventType.BEFORE_DELETE_EVENT,
        getWriteEntityClass().getSimpleName(),
        id.toString(),
        null);
    // 执行删除
    boolean ret = getWriteService().deleteObjectById(id, getDeletionPolicy());
    if (ret) {
      // 发布删除操作事件
      this.publishOperationEvent(
          OperationEventType.DELETED_EVENT,
          getWriteEntityClass().getSimpleName(),
          id.toString(),
          null);
    } else {
      throw new DeleteFailedException(id);
    }
  }

  /**
   * 删除对象接口
   *
   * @param id ID
   * @return 响应对象
   */
  default ResponseEntity<?> deleteDataByIdResponse(@Nullable Serializable id) {
    deleteDataById(id);
    return ResponseUtils.deleted();
  }

  /**
   * 批量删除对象
   *
   * @param ids ID集合
   */
  default void batchDeleteDataById(@Nullable Collection<? extends Serializable> ids) {
    if (Objects.isNull(ids) || ids.isEmpty()) {
      throw new DeleteFailedException("");
    }
    // 发布删除前操作事件
    ids.forEach(
        i ->
            this.publishOperationEvent(
                OperationEventType.BEFORE_DELETE_EVENT,
                getWriteEntityClass().getSimpleName(),
                i.toString(),
                null));
    // 执行删除
    boolean ret = getWriteService().batchDeleteObjectsByIds(ids, getDeletionPolicy());
    if (ret) {
      // 发布删除操作事件
      ids.forEach(
          i ->
              this.publishOperationEvent(
                  OperationEventType.DELETED_EVENT,
                  getWriteEntityClass().getSimpleName(),
                  i.toString(),
                  null));

    } else {
      throw new DeleteFailedException(
          ids.stream().map(Objects::toString).collect(Collectors.joining(IRestfulService.IDS_SEP)));
    }
  }

  /**
   * 删除对象接口
   *
   * @param ids ID集合
   * @return 响应对象
   */
  default ResponseEntity<?> batchDeleteDataByIdResponse(
      @Nullable Collection<? extends Serializable> ids) {
    batchDeleteDataById(ids);
    return ResponseUtils.deleted();
  }
}
