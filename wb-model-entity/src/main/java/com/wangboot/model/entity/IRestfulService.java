package com.wangboot.model.entity;

import com.wangboot.core.web.response.ListBody;
import com.wangboot.model.dataauthority.authorizer.IDataAuthorizer;
import com.wangboot.model.dataauthority.utils.DataAuthorityUtils;
import com.wangboot.model.entity.exception.DeleteFailedException;
import com.wangboot.model.entity.exception.DuplicatedException;
import com.wangboot.model.entity.exception.NotFoundException;
import com.wangboot.model.entity.exception.UpdateFailedException;
import com.wangboot.model.entity.request.FieldFilter;
import com.wangboot.model.entity.request.SortFilter;
import com.wangboot.model.entity.utils.EntityUtils;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Restful 服务接口
 *
 * @param <I> 主键类型
 * @param <T> 实体类型
 * @author wwtg99
 */
public interface IRestfulService<I extends Serializable, T extends IdEntity<I>> {

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
    // 获取数据
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
    // 获取数据
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
  @NonNull
  default T checkBeforeCreateObject(@NonNull T entity) {
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
  default boolean createResource(@NonNull T entity) {
    // 创建前检查
    T t = checkBeforeCreateObject(entity);
    // 执行创建
    return saveData(t);
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
  @NonNull
  default Collection<T> checkBeforeBatchCreateObjects(@NonNull Collection<T> entities) {
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
  default boolean batchCreateResources(@NonNull Collection<T> entities) {
    // 创建前检查
    Collection<T> ts = checkBeforeBatchCreateObjects(entities);
    // 执行创建
    return batchSaveData(ts);
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
  @NonNull
  default T checkBeforeUpdateObject(@NonNull T entity) {
    IDataAuthorizer dataAuthorizer = getDataAuthorizer();
    if (Objects.nonNull(dataAuthorizer)) {
      // 限制数据权限
      // 根据 ID 获取原数据
      T original = viewResource(entity.getId());
      if (Objects.isNull(original)) {
        throw new NotFoundException();
      }
      // 检查数据权限
      if (!dataAuthorizer.hasDataAuthority(original)) {
        throw new UpdateFailedException(entity.getId());
      }
    }
    if (isReadOnly(entity)) {
      // 只读
      throw new UpdateFailedException(entity.getId());
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
  default boolean updateResource(@NonNull T entity) {
    // 更新前检查
    T t = checkBeforeUpdateObject(entity);
    // 执行更新
    return updateData(t);
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
  @NonNull
  default T checkBeforeDeleteObject(@NonNull T entity) {
    IDataAuthorizer dataAuthorizer = getDataAuthorizer();
    if (Objects.nonNull(dataAuthorizer)) {
      // 限制数据权限
      if (!dataAuthorizer.hasDataAuthority(entity)) {
        throw new DeleteFailedException(entity.getId());
      }
    }
    if (isReadOnly(entity)) {
      // 只读
      throw new DeleteFailedException(entity.getId());
    }
    return entity;
  }

  /**
   * 逻辑层删除对象，检查数据权限
   *
   * @param entity 实体对象
   * @return boolean
   */
  default boolean deleteResource(@NonNull T entity) {
    // 删除前检查
    T t = checkBeforeDeleteObject(entity);
    // 执行删除
    return deleteData(t);
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
  @NonNull
  default Collection<T> checkBeforeBatchDeleteObjects(@NonNull Collection<T> entities) {
    if (entities.isEmpty()) {
      return entities;
    }
    // 获取数据，如果缺少则有数据没有数据权限
    List<T> viewEntities =
        viewResources(entities.stream().map(IdEntity::getId).collect(Collectors.toSet()));
    if (viewEntities.size() != entities.size()) {
      throw new DeleteFailedException(
          entities.stream()
              .map(d -> d.getId().toString())
              .collect(Collectors.joining(FieldConstants.FIELD_SEP)));
    }
    // 判断只读
    if (isAnyReadOnly(viewEntities)) {
      throw new DeleteFailedException(
          entities.stream()
              .map(d -> d.getId().toString())
              .collect(Collectors.joining(FieldConstants.FIELD_SEP)));
    }
    return entities;
  }

  /**
   * 逻辑层删除对象，检查数据权限
   *
   * @param entities 实体集合
   * @return boolean
   */
  default boolean batchDeleteResources(@NonNull Collection<T> entities) {
    // 删除前检查
    Collection<T> ts = checkBeforeBatchDeleteObjects(entities);
    // 执行删除
    return batchDeleteData(ts);
  }
}
