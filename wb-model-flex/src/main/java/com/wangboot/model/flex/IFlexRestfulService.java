package com.wangboot.model.flex;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.wangboot.core.utils.ObjectUtils;
import com.wangboot.core.web.response.ListBody;
import com.wangboot.model.dataauthority.DataAuthority;
import com.wangboot.model.dataauthority.authorizer.IDataAuthorizer;
import com.wangboot.model.dataauthority.utils.DataAuthorityUtils;
import com.wangboot.model.entity.FieldConstants;
import com.wangboot.model.entity.IRestfulService;
import com.wangboot.model.entity.IUniqueEntity;
import com.wangboot.model.entity.request.FieldFilter;
import com.wangboot.model.entity.request.SortFilter;
import com.wangboot.model.flex.utils.FlexRestHelper;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 基于 MyBatis Flex 的 Restful 服务接口
 *
 * @param <T> 实体类
 * @author wwtg99
 */
public interface IFlexRestfulService<T> extends IService<T>, IRestfulService<T> {

  /** 获取实体类型 */
  @Override
  @NonNull
  default Class<T> getEntityClass() {
    return ObjectUtils.getTypeArgumentClass(this.getClass().getGenericSuperclass(), 0);
  }

  /**
   * 数据层根据 ID 获取对象，不存在则返回 null
   *
   * @param id ID
   * @return 实体对象
   */
  @Override
  @Nullable
  default T getDataById(@NonNull Serializable id) {
    return getById(id);
  }

  /**
   * 数据层根据 ID 集合获取对象详情集合，不存在或无权限则返回空集合
   *
   * @param ids ID 集合
   * @return 实体对象集合
   */
  @Override
  @NonNull
  default List<T> getDataByIds(@NonNull Collection<? extends Serializable> ids) {
    return list(query().in(FieldConstants.PRIMARY_KEY, ids));
  }

  /**
   * 获取数据权限筛选查询构建器
   *
   * @param wrapper 查询构建器
   * @param entityClass 实体类
   * @param dataAuthorizer 数据权限管理者
   * @return 查询构建器
   */
  @NonNull
  default QueryWrapper buildDataAuthorityQueryWrapper(
      @NonNull QueryWrapper wrapper,
      @NonNull Class<T> entityClass,
      @Nullable IDataAuthorizer dataAuthorizer) {
    DataAuthority dataAuthority = DataAuthorityUtils.getDataAuthority(entityClass);
    return FlexRestHelper.buildDataAuthorityQueryWrapper(wrapper, dataAuthority, dataAuthorizer);
  }

  /**
   * 获取列表查询器
   *
   * @return 查询构建器
   */
  @NonNull
  default QueryWrapper getListQueryWrapper() {
    return this.query();
  }

  /**
   * 逻辑层获取对象所有列表，支持搜索、排序、对象筛选
   *
   * @param sortFilters 排序条件
   * @param fieldFilters 筛选条件
   * @param searchFilters 搜索条件
   * @return 数据列表
   */
  @Override
  @NonNull
  default ListBody<T> getListAll(
      @Nullable SortFilter[] sortFilters,
      @Nullable FieldFilter[] fieldFilters,
      @Nullable FieldFilter[] searchFilters) {
    QueryWrapper wrapper = getListQueryWrapper();
    if (Objects.nonNull(getDataAuthorizer())) {
      // 限制数据权限
      wrapper = buildDataAuthorityQueryWrapper(wrapper, getEntityClass(), getDataAuthorizer());
    }
    List<T> data =
        list(
            FlexRestHelper.buildListQueryWrapper(
                wrapper, sortFilters, fieldFilters, searchFilters));
    ListBody<T> listBody = new ListBody<>();
    listBody.setData(data);
    listBody.setTotal(data.size());
    return listBody;
  }

  /**
   * 逻辑层获取对象分页列表，支持搜索、分页、排序、对象筛选
   *
   * @param sortFilters 排序参数
   * @param fieldFilters 筛选参数
   * @param searchFilters 搜索参数
   * @param page 页码
   * @param pageSize 每页数量
   * @return 数据列表
   */
  @Override
  @NonNull
  default ListBody<T> getListPage(
      @Nullable SortFilter[] sortFilters,
      @Nullable FieldFilter[] fieldFilters,
      @Nullable FieldFilter[] searchFilters,
      long page,
      long pageSize) {
    QueryWrapper wrapper = getListQueryWrapper();
    if (Objects.nonNull(getDataAuthorizer())) {
      // 限制数据权限
      wrapper = buildDataAuthorityQueryWrapper(wrapper, getEntityClass(), getDataAuthorizer());
    }
    Page<T> data =
        page(
            new Page<>(page, pageSize),
            FlexRestHelper.buildListQueryWrapper(
                wrapper, sortFilters, fieldFilters, searchFilters));
    ListBody<T> listBody = new ListBody<>();
    listBody.setData(data.getRecords());
    listBody.setTotal(data.getTotalRow());
    listBody.setPage(data.getPageNumber());
    listBody.setPageSize(data.getPageSize());
    return listBody;
  }

  /**
   * 获取根节点查询器
   *
   * @return 查询构建器
   */
  @NonNull
  default QueryWrapper getRootQueryWrapper() {
    return this.query();
  }

  /**
   * 逻辑层获取根数据列表
   *
   * @return 数据列表
   */
  @Override
  @NonNull
  default ListBody<T> getRootData() {
    QueryWrapper wrapper = getRootQueryWrapper();
    if (Objects.nonNull(getDataAuthorizer())) {
      // 限制数据权限
      wrapper = buildDataAuthorityQueryWrapper(wrapper, getEntityClass(), getDataAuthorizer());
    }
    List<T> data = list(FlexRestHelper.buildRootQueryWrapper(wrapper));
    ListBody<T> listBody = new ListBody<>();
    listBody.setData(data);
    listBody.setTotal(data.size());
    return listBody;
  }

  /**
   * 获取直接子节点查询器
   *
   * @return 查询构建器
   */
  @NonNull
  default QueryWrapper getDirectChildrenQueryWrapper() {
    return this.query();
  }

  /**
   * 逻辑层获取直接子数据数量
   *
   * @param id ID
   * @return 数据数量
   */
  @Override
  default long getDirectChildrenCount(@Nullable Serializable id) {
    if (Objects.isNull(id)) {
      return 0;
    }
    QueryWrapper wrapper = getDirectChildrenQueryWrapper();
    if (Objects.nonNull(getDataAuthorizer())) {
      // 限制数据权限
      wrapper = buildDataAuthorityQueryWrapper(wrapper, getEntityClass(), getDataAuthorizer());
    }
    return count(FlexRestHelper.buildDirectChildrenQueryWrapper(wrapper, id));
  }

  /**
   * 逻辑层获取直接子数据列表
   *
   * @param id ID
   * @return 数据列表
   */
  @Override
  @NonNull
  default ListBody<T> getDirectChildren(@Nullable Serializable id) {
    if (Objects.isNull(id)) {
      return new ListBody<>();
    }
    QueryWrapper wrapper = getDirectChildrenQueryWrapper();
    if (Objects.nonNull(getDataAuthorizer())) {
      // 限制数据权限
      wrapper = buildDataAuthorityQueryWrapper(wrapper, getEntityClass(), getDataAuthorizer());
    }
    List<T> data = list(FlexRestHelper.buildDirectChildrenQueryWrapper(wrapper, id));
    ListBody<T> listBody = new ListBody<>();
    listBody.setData(data);
    listBody.setTotal(data.size());
    return listBody;
  }

  /**
   * 逻辑层获取直接子数据列表
   *
   * @param ids ID 集合
   * @return 数据列表
   */
  @Override
  @NonNull
  default ListBody<T> getDirectChildren(@Nullable Collection<? extends Serializable> ids) {
    if (Objects.isNull(ids) || ids.isEmpty()) {
      return new ListBody<>();
    }
    QueryWrapper wrapper = getDirectChildrenQueryWrapper();
    if (Objects.nonNull(getDataAuthorizer())) {
      // 限制数据权限
      wrapper = buildDataAuthorityQueryWrapper(wrapper, getEntityClass(), getDataAuthorizer());
    }
    List<T> data = list(FlexRestHelper.buildDirectChildrenQueryWrapper(wrapper, ids));
    ListBody<T> listBody = new ListBody<>();
    listBody.setData(data);
    listBody.setTotal(data.size());
    return listBody;
  }

  /**
   * 是否存在重复字段行
   *
   * @param entity 对象
   * @param newRecord 是否新建数据
   * @return boolean
   */
  @Override
  default boolean isUniqueDuplicated(@NonNull T entity, boolean newRecord) {
    if (entity instanceof IUniqueEntity) {
      try {
        QueryWrapper wrapper = FlexRestHelper.buildUniqueCheckQueryWrapper(query(), entity);
        if (newRecord) {
          return count(wrapper) > 0;
        } else {
          return count(wrapper) > 1;
        }
      } catch (IllegalArgumentException ignored) {
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * 数据层存储数据
   *
   * @param data 数据对象
   * @return boolean
   */
  @Override
  default boolean saveData(@NonNull T data) {
    return save(data);
  }

  /**
   * 数据层批量创建数据
   *
   * @param data 数据集合
   * @return boolean
   */
  @Override
  default boolean batchSaveData(@NonNull Collection<T> data) {
    return saveBatch(data);
  }

  /**
   * 数据层更新数据
   *
   * @param data 数据
   * @return boolean
   */
  @Override
  default boolean updateData(@NonNull T data) {
    return updateById(data, false);
  }

  /**
   * 数据层删除数据
   *
   * @param id 数据ID
   * @return boolean
   */
  @Override
  default boolean deleteDataById(@NonNull Serializable id) {
    return removeById(id);
  }

  /**
   * 数据层删除数据
   *
   * @param data 数据
   * @return boolean
   */
  @Override
  default boolean deleteData(@NonNull T data) {
    return removeById(data);
  }

  /**
   * 数据层批量删除数据
   *
   * @param ids 数据 ID 集合
   * @return boolean
   */
  @Override
  default boolean batchDeleteDataByIds(@NonNull Collection<? extends Serializable> ids) {
    return removeByIds(ids);
  }
}
