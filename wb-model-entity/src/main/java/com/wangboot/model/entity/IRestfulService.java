package com.wangboot.model.entity;

import com.wangboot.core.auth.utils.AuthUtils;
import com.wangboot.core.web.response.ListBody;
import com.wangboot.core.web.utils.ServletUtils;
import com.wangboot.model.dataauthority.authorizer.IDataAuthorizer;
import com.wangboot.model.dataauthority.utils.DataAuthorityUtils;
import com.wangboot.model.entity.event.IOperationEventPublisher;
import com.wangboot.model.entity.event.OperationEventType;
import com.wangboot.model.entity.exception.DuplicatedException;
import com.wangboot.model.entity.request.FieldFilter;
import com.wangboot.model.entity.request.SortFilter;
import com.wangboot.model.entity.utils.EntityUtils;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Restful 服务接口
 *
 * @param <I> 主键类型
 * @param <T> 实体类型
 * @author wwtg99
 */
public interface IRestfulService<I extends Serializable, T extends IdEntity<I>>
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

  /**
   * 获取实体类型
   *
   * @return 实体类型
   */
  Class<T> getEntityClass();

  /**
   * 获取数据权限授权者，如果不支持则返回 null
   *
   * @return 数据权限授权者
   */
  @Nullable
  default IDataAuthorizer getDataAuthorizer() {
    return DataAuthorityUtils.getDataAuthorizer(getEntityClass());
  }

  /**
   * 数据层根据 ID 获取对象，不存在则返回 null
   *
   * @param id ID
   * @return 实体对象
   */
  @Nullable
  T getDataById(@NonNull I id);

  /**
   * 逻辑层根据 ID 获取对象详情，检查数据权限，不存在或无权限则返回 null
   *
   * @param id ID
   * @return 实体对象
   */
  @Nullable
  default T viewResource(@Nullable I id) {
    if (Objects.isNull(id)) {
      return null;
    }
    T data = getDataById(id);
    IDataAuthorizer dataAuthorizer = getDataAuthorizer();
    if (Objects.nonNull(dataAuthorizer)) {
      // 限制数据权限
      if (dataAuthorizer.hasDataAuthority(data)) {
        return data;
      } else {
        return null;
      }
    } else {
      // 不限制数据权限
      return data;
    }
  }

  /**
   * 数据层根据 ID 集合获取对象详情集合，不存在或无权限则返回空集合
   *
   * @param ids ID 集合
   * @return 实体对象集合
   */
  @NonNull
  List<T> getDataByIds(@NonNull Collection<I> ids);

  /**
   * 逻辑层根据 ID 集合获取对象详情集合，检查数据权限，不存在或无权限则返回空集合
   *
   * @param ids ID 集合
   * @return 实体对象集合
   */
  @NonNull
  default List<T> viewResources(@Nullable Collection<I> ids) {
    if (Objects.isNull(ids)) {
      return Collections.emptyList();
    }
    List<T> data = getDataByIds(ids);
    IDataAuthorizer dataAuthorizer = getDataAuthorizer();
    if (Objects.nonNull(dataAuthorizer)) {
      // 限制数据权限
      return data.stream().filter(dataAuthorizer::hasDataAuthority).collect(Collectors.toList());
    } else {
      // 不限制数据权限
      return data;
    }
  }

  /**
   * 逻辑层获取对象所有列表，支持搜索、排序、对象筛选
   *
   * @param sortFilters 排序参数
   * @param fieldFilters 筛选参数
   * @param searchFilters 搜索参数
   * @return 数据列表
   */
  @NonNull
  ListBody<T> listResourcesAll(
      @Nullable SortFilter[] sortFilters,
      @Nullable FieldFilter[] fieldFilters,
      @Nullable FieldFilter[] searchFilters);

  /**
   * 逻辑层获取对象分页列表，支持搜索、分页、排序、对象筛选
   *
   * @param sortFilter 排序参数
   * @param fieldFilter 筛选参数
   * @param searchFilters 搜索参数
   * @param page 页码
   * @param pageSize 每页数量
   * @return 数据列表
   */
  @NonNull
  ListBody<T> listResourcesPage(
      @Nullable SortFilter[] sortFilter,
      @Nullable FieldFilter[] fieldFilter,
      @Nullable FieldFilter[] searchFilters,
      long page,
      long pageSize);

  /**
   * 逻辑层获取根数据列表
   *
   * @return 数据列表
   */
  @NonNull
  List<T> listRootResources();

  /**
   * 逻辑层获取直接子数据数量
   *
   * @param id ID
   * @return 数据数量
   */
  long getDirectChildrenCount(@Nullable I id);

  /**
   * 逻辑层获取直接子数据列表
   *
   * @param id ID
   * @return 数据列表
   */
  @NonNull
  List<T> listDirectChildren(@Nullable I id);

  /**
   * 逻辑层获取直接子数据列表
   *
   * @param ids ID 集合
   * @return 数据列表
   */
  @NonNull
  List<T> listDirectChildren(@Nullable Collection<I> ids);

  /**
   * 是否存在重复字段行
   *
   * @param entity 对象
   * @param newRecord 是否新建数据
   * @return boolean
   */
  boolean isUniqueDuplicated(@NonNull T entity, boolean newRecord);

  /**
   * 是否只读对象
   *
   * @param entity 对象
   * @return boolean
   */
  default boolean isReadOnly(@NonNull T entity) {
    return EntityUtils.isReadonly(entity);
  }

  /**
   * 是否所有都是只读对象
   *
   * @param entities 对象集合
   * @return boolean
   */
  default boolean isAllReadOnly(@Nullable Collection<T> entities) {
    if (Objects.nonNull(entities)) {
      for (T entity : entities) {
        if (!isReadOnly(entity)) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  /**
   * 是否任一是只读对象
   *
   * @param entities 对象集合
   * @return boolean
   */
  default boolean isAnyReadOnly(@Nullable Collection<T> entities) {
    if (Objects.nonNull(entities)) {
      for (T entity : entities) {
        if (isReadOnly(entity)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 数据层存储数据
   *
   * @param data 数据对象
   * @return boolean
   */
  boolean saveData(@NonNull T data);

  /**
   * 创建对象前检查
   *
   * @param entity 实体对象
   * @return 检查后的实体对象
   */
  @Nullable
  default T checkBeforeCreateObject(@Nullable T entity) {
    if (Objects.isNull(entity)) {
      return null;
    }
    // 检查是否重复
    if (isUniqueDuplicated(entity, true)) {
      throw new DuplicatedException();
    }
    return entity;
  }

  /**
   * 逻辑层创建对象，检查数据权限
   *
   * @param entity 实体对象
   * @return boolean
   */
  default boolean createResource(@Nullable T entity) {
    // 创建前检查
    T t = checkBeforeCreateObject(entity);
    if (Objects.isNull(t)) {
      return false;
    }
    if (EntityUtils.isOperationLogEnabled(getEntityClass())) {
      // 发布创建前操作事件
      this.publishOperationEvent(getEntityClass(), OperationEventType.BEFORE_CREATE_EVENT, t);
    }
    // 执行创建
    boolean ret = saveData(t);
    if (ret && EntityUtils.isOperationLogEnabled(getEntityClass())) {
      // 发布创建操作事件
      this.publishOperationEvent(
          getEntityClass(), OperationEventType.CREATED_EVENT, t.getId().toString(), t);
    }
    return ret;
  }

  /**
   * 数据层批量创建数据
   *
   * @param entities 数据集合
   * @return boolean
   */
  boolean batchSaveData(@NonNull Collection<T> entities);

  /**
   * 批量创建对象前检查
   *
   * @param entities 数据集合
   * @return 检查后的数据集合
   */
  @Nullable
  default Collection<T> checkBeforeBatchCreateObjects(@Nullable Collection<T> entities) {
    if (Objects.isNull(entities)) {
      return null;
    }
    // 检查是否重复
    entities.forEach(
        d -> {
          if (isUniqueDuplicated(d, true)) {
            throw new DuplicatedException();
          }
        });
    return entities;
  }

  /**
   * 逻辑层批量创建对象，检查数据权限
   *
   * @param entities 实体对象集合
   * @return boolean
   */
  default boolean batchCreateResources(@Nullable Collection<T> entities) {
    // 创建前检查
    Collection<T> ts = checkBeforeBatchCreateObjects(entities);
    if (Objects.isNull(ts)) {
      return false;
    }
    if (EntityUtils.isOperationLogEnabled(getEntityClass())) {
      // 发布创建前操作事件
      ts.forEach(
          t ->
              this.publishOperationEvent(
                  getEntityClass(), OperationEventType.BEFORE_CREATE_EVENT, t));
    }
    // 执行创建
    boolean ret = batchSaveData(ts);
    if (ret && EntityUtils.isOperationLogEnabled(getEntityClass())) {
      // 发布创建操作事件
      ts.forEach(
          t ->
              this.publishOperationEvent(
                  getEntityClass(), OperationEventType.CREATED_EVENT, t.getId().toString(), t));
    }
    return ret;
  }

  /**
   * 数据层更新数据
   *
   * @param data 数据
   * @return boolean
   */
  boolean updateData(@NonNull T data);

  /**
   * 更新对象前检查
   *
   * @param entity 实体对象
   * @return 检查后的实体对象
   */
  @Nullable
  default T checkBeforeUpdateObject(@Nullable T entity) {
    if (Objects.isNull(entity)) {
      return null;
    }
    IDataAuthorizer dataAuthorizer = getDataAuthorizer();
    if (Objects.nonNull(dataAuthorizer)) {
      // 限制数据权限
      // 根据 ID 获取原数据
      T original = viewResource(entity.getId());
      if (Objects.isNull(original)) {
        return null;
      }
      // 检查数据权限
      if (!dataAuthorizer.hasDataAuthority(original)) {
        return null;
      }
    }
    if (isReadOnly(entity)) {
      // 只读
      return null;
    }
    // 检查是否重复
    if (isUniqueDuplicated(entity, false)) {
      throw new DuplicatedException();
    }
    return entity;
  }

  /**
   * 逻辑层更新对象，检查数据权限
   *
   * @param entity 实体对象
   * @return boolean
   */
  default boolean updateResource(@Nullable T entity) {
    // 更新前检查
    T t = checkBeforeUpdateObject(entity);
    if (Objects.isNull(t)) {
      return false;
    }
    if (EntityUtils.isOperationLogEnabled(getEntityClass())) {
      // 发布更新前操作事件
      this.publishOperationEvent(
          getEntityClass(), OperationEventType.BEFORE_UPDATE_EVENT, t.getId().toString(), t);
    }
    // 执行更新
    boolean ret = updateData(t);
    if (ret && EntityUtils.isOperationLogEnabled(getEntityClass())) {
      // 发布更新操作事件
      this.publishOperationEvent(
          getEntityClass(), OperationEventType.UPDATED_EVENT, t.getId().toString(), t);
    }
    return ret;
  }

  /**
   * 数据层删除数据
   *
   * @param data 数据
   * @return boolean
   */
  boolean deleteData(@NonNull T data);

  /**
   * 删除数据前检查
   *
   * @param entity 实体对象
   * @return 检查后的实体对象
   */
  @Nullable
  default T checkBeforeDeleteObject(@Nullable T entity) {
    if (Objects.isNull(entity)) {
      return null;
    }
    IDataAuthorizer dataAuthorizer = getDataAuthorizer();
    if (Objects.nonNull(dataAuthorizer)) {
      // 限制数据权限
      if (!dataAuthorizer.hasDataAuthority(entity)) {
        return null;
      }
    }
    if (isReadOnly(entity)) {
      // 只读
      return null;
    }
    return entity;
  }

  /**
   * 逻辑层删除对象，检查数据权限
   *
   * @param entity 实体对象
   * @return boolean
   */
  default boolean deleteResource(@Nullable T entity) {
    // 删除前检查
    T t = checkBeforeDeleteObject(entity);
    if (Objects.isNull(t)) {
      return false;
    }
    if (EntityUtils.isOperationLogEnabled(getEntityClass())) {
      // 发布删除前操作事件
      this.publishOperationEvent(
          getEntityClass(), OperationEventType.BEFORE_DELETE_EVENT, t.getId().toString(), t);
    }
    // 执行删除
    boolean ret = deleteData(t);
    if (ret && EntityUtils.isOperationLogEnabled(getEntityClass())) {
      // 发布删除操作事件
      this.publishOperationEvent(
          getEntityClass(), OperationEventType.DELETED_EVENT, t.getId().toString(), t);
    }
    return ret;
  }

  /**
   * 数据层批量删除数据
   *
   * @param entities 数据集合
   * @return boolean
   */
  boolean batchDeleteData(@NonNull Collection<T> entities);

  /**
   * 批量删除数据前检查
   *
   * @param entities 数据集合
   * @return 检查后的数据集合
   */
  @Nullable
  default Collection<T> checkBeforeBatchDeleteObjects(@Nullable Collection<T> entities) {
    if (Objects.isNull(entities) || entities.isEmpty()) {
      return null;
    }
    // 获取数据，如果缺少则有数据没有数据权限
    List<T> viewEntities =
        viewResources(entities.stream().map(IdEntity::getId).collect(Collectors.toSet()));
    if (viewEntities.size() != entities.size()) {
      return null;
    }
    // 判断只读
    if (isAnyReadOnly(viewEntities)) {
      return null;
    }
    return entities;
  }

  /**
   * 逻辑层删除对象，检查数据权限
   *
   * @param entities 实体集合
   * @return boolean
   */
  default boolean batchDeleteResources(@Nullable Collection<T> entities) {
    // 删除前检查
    Collection<T> ts = checkBeforeBatchDeleteObjects(entities);
    if (Objects.isNull(ts)) {
      return false;
    }
    if (EntityUtils.isOperationLogEnabled(getEntityClass())) {
      // 发布删除前操作事件
      ts.forEach(
          t ->
              this.publishOperationEvent(
                  getEntityClass(),
                  OperationEventType.BEFORE_DELETE_EVENT,
                  t.getId().toString(),
                  t));
    }
    // 执行删除
    boolean ret = batchDeleteData(ts);
    if (ret && EntityUtils.isOperationLogEnabled(getEntityClass())) {
      // 发布删除操作事件
      ts.forEach(
          t ->
              this.publishOperationEvent(
                  getEntityClass(), OperationEventType.DELETED_EVENT, t.getId().toString(), t));
    }
    return ret;
  }
}
