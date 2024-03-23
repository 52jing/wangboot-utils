package com.wangboot.model.entity;

import com.wangboot.core.auth.utils.AuthUtils;
import com.wangboot.core.web.exception.DeleteCascadeFailedException;
import com.wangboot.core.web.exception.DuplicatedException;
import com.wangboot.core.web.response.ListBody;
import com.wangboot.model.dataauthority.DataAuthority;
import com.wangboot.model.dataauthority.authorizer.IDataAuthorizer;
import com.wangboot.model.dataauthority.utils.DataAuthorityUtils;
import com.wangboot.model.entity.request.DeletionPolicy;
import com.wangboot.model.entity.request.FieldFilter;
import com.wangboot.model.entity.request.SortFilter;
import com.wangboot.model.entity.utils.EntityUtils;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Restful 服务接口
 *
 * @param <T> 实体类型
 * @author wwtg99
 */
public interface IRestfulService<T> {

  /** ID 分隔符 */
  String IDS_SEP = ",";

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
    if (!getEntityClass().isAnnotationPresent(DataAuthority.class)) {
      return null;
    }
    return DataAuthorityUtils.getDataAuthorizer(AuthUtils.getUserModel(), getEntityClass());
  }

  /**
   * 数据层根据 ID 获取对象，不存在则返回 null
   *
   * @param id ID
   * @return 实体对象
   */
  @Nullable
  T getDataById(@NonNull Serializable id);

  /**
   * 逻辑层根据 ID 获取对象详情，检查数据权限，不存在或无权限则返回 null
   *
   * @param id ID
   * @return 实体对象
   */
  @Nullable
  default T getDetailById(@Nullable Serializable id) {
    if (Objects.isNull(id)) {
      return null;
    }
    T data = getDataById(id);
    if (Objects.nonNull(getDataAuthorizer())) {
      // 限制数据权限
      if (getDataAuthorizer().authorize(data)) {
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
  List<T> getDataByIds(@NonNull Collection<? extends Serializable> ids);

  /**
   * 逻辑层根据 ID 集合获取对象详情集合，检查数据权限，不存在或无权限则返回空集合
   *
   * @param ids ID 集合
   * @return 实体对象集合
   */
  @NonNull
  default List<T> getDetailByIds(@Nullable Collection<? extends Serializable> ids) {
    if (Objects.isNull(ids)) {
      return Collections.emptyList();
    }
    List<T> data = getDataByIds(ids);
    if (Objects.nonNull(getDataAuthorizer())) {
      // 限制数据权限
      IDataAuthorizer dataAuthorizer = getDataAuthorizer();
      return data.stream().filter(dataAuthorizer::authorize).collect(Collectors.toList());
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
  ListBody<T> getListAll(
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
  ListBody<T> getListPage(
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
  ListBody<T> getRootData();

  /**
   * 逻辑层获取直接子数据数量
   *
   * @param id ID
   * @return 数据数量
   */
  long getDirectChildrenCount(@Nullable Serializable id);

  /**
   * 逻辑层获取直接子数据列表
   *
   * @param id ID
   * @return 数据列表
   */
  @NonNull
  ListBody<T> getDirectChildren(@Nullable Serializable id);

  /**
   * 逻辑层获取直接子数据列表
   *
   * @param ids ID 集合
   * @return 数据列表
   */
  @NonNull
  ListBody<T> getDirectChildren(@Nullable Collection<? extends Serializable> ids);

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
   * 逻辑层创建对象，检查数据权限
   *
   * @param entity 实体对象
   * @return boolean
   */
  default boolean createObject(@Nullable T entity) {
    if (Objects.isNull(entity)) {
      return false;
    }
    // 检查是否重复
    if (isUniqueDuplicated(entity, true)) {
      throw new DuplicatedException();
    }
    return saveData(entity);
  }

  /**
   * 数据层批量创建数据
   *
   * @param data 数据集合
   * @return boolean
   */
  boolean batchSaveData(@NonNull Collection<T> data);

  /**
   * 逻辑层批量创建对象，检查数据权限
   *
   * @param entities 实体对象集合
   * @return boolean
   */
  default boolean batchCreateObjects(@Nullable Collection<T> entities) {
    if (Objects.isNull(entities)) {
      return false;
    }
    // 检查是否重复
    entities.forEach(
        d -> {
          if (isUniqueDuplicated(d, true)) {
            throw new DuplicatedException();
          }
        });
    return batchSaveData(entities);
  }

  /**
   * 数据层更新数据
   *
   * @param data 数据
   * @return boolean
   */
  boolean updateData(@NonNull T data);

  /**
   * 逻辑层更新对象，检查数据权限
   *
   * @param entity 实体对象
   * @return boolean
   */
  default boolean updateObject(@Nullable T entity) {
    if (Objects.isNull(entity)) {
      return false;
    }
    if (Objects.nonNull(getDataAuthorizer())) {
      // 限制数据权限
      // 根据 ID 获取原数据
      T original = getDetailById(EntityUtils.getEntityIdentifier(entity));
      if (Objects.isNull(original)) {
        return false;
      }
      // 检查数据权限
      if (!getDataAuthorizer().authorize(original)) {
        return false;
      }
    }
    if (isReadOnly(entity)) {
      // 只读
      return false;
    }
    // 检查是否重复
    if (isUniqueDuplicated(entity, false)) {
      throw new DuplicatedException();
    }
    return updateData(entity);
  }

  /**
   * 数据层删除数据
   *
   * @param id 数据ID
   * @return boolean
   */
  boolean deleteDataById(@NonNull Serializable id);

  /**
   * 逻辑层删除对象，检查数据权限
   *
   * @param id ID
   * @param policy 删除策略
   * @return boolean
   */
  default boolean deleteObjectById(@Nullable Serializable id, @Nullable DeletionPolicy policy) {
    if (Objects.isNull(id)) {
      return false;
    }
    if (Objects.nonNull(getDataAuthorizer())) {
      // 限制数据权限
      T entity = getDetailById(id);
      if (Objects.isNull(entity)) {
        // 没有数据权限
        return false;
      }
      if (isReadOnly(entity)) {
        // 只读
        return false;
      }
    } else {
      // 不限制数据权限
      // 获取数据，判断只读
      T entity = getDetailById(id);
      if (Objects.isNull(entity) || isReadOnly(entity)) {
        return false;
      }
    }
    if (DeletionPolicy.PROTECT.equals(policy) && EntityUtils.isTreeEntity(getEntityClass())) {
      // 保护策略，存在子节点则失败
      if (getDirectChildrenCount(id) > 0) {
        throw new DeleteCascadeFailedException(id);
      }
    }
    return deleteDataById(id);
  }

  /**
   * 数据层删除数据
   *
   * @param data 数据
   * @return boolean
   */
  boolean deleteData(@NonNull T data);

  /**
   * 逻辑层删除对象，检查数据权限
   *
   * @param entity 实体对象
   * @param policy 删除策略
   * @return boolean
   */
  default boolean deleteObject(@Nullable T entity, @Nullable DeletionPolicy policy) {
    if (Objects.isNull(entity)) {
      return false;
    }
    if (Objects.nonNull(getDataAuthorizer())) {
      // 限制数据权限
      if (!getDataAuthorizer().authorize(entity)) {
        return false;
      }
    }
    if (isReadOnly(entity)) {
      // 只读
      return false;
    }
    if (DeletionPolicy.PROTECT.equals(policy) && entity instanceof ITreeEntity) {
      // 保护策略，存在子节点则失败
      if (getDirectChildrenCount(EntityUtils.getEntityIdentifier(entity)) > 0) {
        throw new DeleteCascadeFailedException(EntityUtils.getEntityIdentifier(entity));
      }
    }
    return deleteData(entity);
  }

  /**
   * 数据层批量删除数据
   *
   * @param ids 数据 ID 集合
   * @return boolean
   */
  boolean batchDeleteDataByIds(@NonNull Collection<? extends Serializable> ids);

  /**
   * 逻辑层删除对象，检查数据权限
   *
   * @param ids ID 集合
   * @param policy 删除策略
   * @return boolean
   */
  default boolean batchDeleteObjectsByIds(
      @Nullable Collection<? extends Serializable> ids, @Nullable DeletionPolicy policy) {
    if (Objects.isNull(ids) || ids.isEmpty()) {
      return false;
    }
    if (Objects.nonNull(getDataAuthorizer())) {
      // 限制数据权限
      // 获取数据，如果缺少则有数据没有数据权限
      List<T> entities = getDetailByIds(ids);
      if (entities.size() != ids.size()) {
        return false;
      }
      // 判断只读
      if (isAnyReadOnly(entities)) {
        return false;
      }
    } else {
      // 不限制数据权限
      // 获取数据，判断只读
      List<T> entities = getDetailByIds(ids);
      if (isAnyReadOnly(entities)) {
        return false;
      }
    }
    if (DeletionPolicy.PROTECT.equals(policy) && EntityUtils.isTreeEntity(getEntityClass())) {
      // 保护策略，存在子节点则失败
      ListBody<T> children = getDirectChildren(ids);
      if (children.getTotal() > 0) {
        throw new DeleteCascadeFailedException(
            ids.stream().map(Objects::toString).collect(Collectors.joining(IDS_SEP)));
      }
    }
    return batchDeleteDataByIds(ids);
  }
}
