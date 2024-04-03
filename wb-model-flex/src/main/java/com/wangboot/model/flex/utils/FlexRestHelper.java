package com.wangboot.model.flex.utils;

import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.wangboot.core.auth.authorization.IAuthorizationResource;
import com.wangboot.core.utils.StrUtils;
import com.wangboot.model.dataauthority.DataAuthority;
import com.wangboot.model.dataauthority.DataAuthorityType;
import com.wangboot.model.dataauthority.authorizer.IDataAuthorizer;
import com.wangboot.model.dataauthority.utils.DataAuthorityUtils;
import com.wangboot.model.entity.*;
import com.wangboot.model.entity.request.FieldFilter;
import com.wangboot.model.entity.request.SortFilter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * Restful 辅助工具类
 *
 * @author wwtg99
 */
public class FlexRestHelper {
  private FlexRestHelper() {}

  /**
   * 是否存在重复字段行
   *
   * @param wrapper 查询构建器
   * @param <T> obj 实体对象
   * @return 查询构建器
   */
  public static <T> QueryWrapper buildUniqueCheckQueryWrapper(
      @NonNull QueryWrapper wrapper, @NonNull T obj) {
    if (obj instanceof IUniqueEntity) {
      String[][] uniqueFields = ((IUniqueEntity) obj).getUniqueTogetherFields();
      if (uniqueFields.length == 0) {
        // 不限制唯一字段
        throw new IllegalArgumentException("No unique fields");
      }
      DynaBean bean = DynaBean.create(obj);
      for (String[] groups : uniqueFields) {
        wrapper.or(
            w -> {
              for (String field : groups) {
                w.eq(field, bean.get(StrUtils.toCamelCase(field, false)));
              }
            });
      }
      return wrapper;
    }
    // 非唯一字段实体
    throw new IllegalArgumentException("Not unique entity!");
  }

  /**
   * 是否具有对象的数据权限
   *
   * @param obj 对象
   * @param dataAuthorizer 数据权限授权者
   * @return boolean
   */
  public static boolean hasDataAuthority(
      @Nullable Object obj, @Nullable IDataAuthorizer dataAuthorizer) {
    if (Objects.isNull(obj)) {
      return false;
    }
    if (!DataAuthorityUtils.restrictDataAuthority(obj.getClass())) {
      // 实体类不支持数据权限
      return true;
    }
    if (Objects.isNull(dataAuthorizer)) {
      return false;
    }
    return dataAuthorizer.authorize(obj);
  }

  /**
   * 限制数据权限
   *
   * @param wrapper 查询构建器
   * @param dataAuthority 数据权限注解
   * @param dataAuthorizer 数据权限管理器
   * @return 查询构建器
   */
  @NonNull
  public static QueryWrapper buildDataAuthorityQueryWrapper(
      @NonNull QueryWrapper wrapper,
      @NonNull DataAuthority dataAuthority,
      @Nullable IDataAuthorizer dataAuthorizer) {
    if (Objects.isNull(dataAuthorizer)) {
      return wrapper;
    }
    if (DataAuthorityType.DATA_SCOPE.equals(dataAuthority.value())) {
      return buildDataScopeQueryWrapper(wrapper, dataAuthority.field(), dataAuthorizer);
    } else if (DataAuthorityType.USER_ID_FIELD.equals(dataAuthority.value())) {
      return buildUserIdFieldQueryWrapper(wrapper, dataAuthority.field(), dataAuthorizer);
    } else {
      return wrapper;
    }
  }

  /**
   * 获取数据权限筛选构建器
   *
   * @param wrapper 查询构建器
   * @param field 数据权限字段
   * @param dataAuthorizer 数据权限授权者
   * @return 查询构建器
   */
  @NonNull
  public static QueryWrapper buildDataScopeQueryWrapper(
      @NonNull QueryWrapper wrapper, String field, @Nullable IDataAuthorizer dataAuthorizer) {
    if (!StringUtils.hasText(field) || Objects.isNull(dataAuthorizer)) {
      return wrapper;
    }
    List<String> scopes =
        dataAuthorizer.getAuthorities().stream()
            .map(IAuthorizationResource::getResourceName)
            .collect(Collectors.toList());
    return wrapper.in(field, scopes);
  }

  /**
   * 获取限制创建用户筛选构建器
   *
   * @param wrapper 查询构建器
   * @param field 用户ID字段
   * @param dataAuthorizer 数据权限授权者
   * @return 查询构建器
   */
  @NonNull
  public static QueryWrapper buildUserIdFieldQueryWrapper(
      @NonNull QueryWrapper wrapper, String field, @Nullable IDataAuthorizer dataAuthorizer) {
    if (!StringUtils.hasText(field) || Objects.isNull(dataAuthorizer)) {
      return wrapper;
    }
    String userId =
        dataAuthorizer.getAuthorities().stream()
            .map(IAuthorizationResource::getResourceName)
            .collect(Collectors.joining());
    return wrapper.eq(field, userId);
  }

  /**
   * 构建列表查询器
   *
   * @param wrapper 查询构建器
   * @param sortFilters 排序条件
   * @param fieldFilters 筛选条件
   * @param searchFilters 搜索条件
   * @return 查询构建器
   */
  public static QueryWrapper buildListQueryWrapper(
      @NonNull QueryWrapper wrapper,
      @Nullable SortFilter[] sortFilters,
      @Nullable FieldFilter[] fieldFilters,
      @Nullable FieldFilter[] searchFilters) {
    // 排序
    if (Objects.nonNull(sortFilters) && sortFilters.length > 0) {
      FlexRestHelper.buildSortQueryWrapper(wrapper, sortFilters);
    }
    // 筛选
    if (Objects.nonNull(fieldFilters) && fieldFilters.length > 0) {
      FlexRestHelper.buildFilterQueryWrapper(wrapper, fieldFilters);
    }
    // 搜索
    if (Objects.nonNull(searchFilters) && searchFilters.length > 0) {
      FlexRestHelper.buildSearchQueryWrapper(wrapper, searchFilters);
    }
    return wrapper;
  }

  /**
   * 构建列表查询筛选对象
   *
   * @param wrapper 查询构建器
   * @param filters 筛选数组
   * @return 查询构建器
   */
  @NonNull
  public static QueryWrapper buildFilterQueryWrapper(
      @NonNull QueryWrapper wrapper, @Nullable FieldFilter[] filters) {
    if (Objects.isNull(filters) || filters.length == 0) {
      return wrapper;
    }
    // 遍历筛选
    for (FieldFilter filter : filters) {
      String field = filter.getField();
      String val = filter.getVal();
      // 跳过空值
      if (!StringUtils.hasText(field) || !StringUtils.hasText(val)) {
        continue;
      }
      Object parsedVal;
      // 类型转换
      switch (filter.getType()) {
        case INT:
          parsedVal = Convert.toInt(val);
          break;
        case FLOAT:
          parsedVal = Convert.toFloat(val);
          break;
        case DATE:
          parsedVal = Convert.toDate(val);
          break;
        case BOOL:
          parsedVal = Convert.toBool(val);
          break;
        default:
          // not parsed
          parsedVal = val;
          break;
      }
      // 操作判断
      switch (filter.getOperator()) {
        case EQ:
          wrapper.eq(field, parsedVal);
          break;
        case GT:
          wrapper.gt(field, parsedVal);
          break;
        case GE:
          wrapper.ge(field, parsedVal);
          break;
        case LT:
          wrapper.lt(field, parsedVal);
          break;
        case LE:
          wrapper.le(field, parsedVal);
          break;
        case NE:
          wrapper.ne(field, parsedVal);
          break;
        case IN:
          List<String> s =
              CharSequenceUtil.split(
                  parsedVal.toString(), RequestConstants.REQUEST_PARAM_IN_DELIMITER);
          if (!s.isEmpty()) {
            wrapper.in(field, s);
          }
          break;
        case STARTSWITH:
          wrapper.likeLeft(field, parsedVal);
          break;
        case ENDSWITH:
          wrapper.likeRight(field, parsedVal);
          break;
        case CONTAINS:
          wrapper.like(field, parsedVal);
          break;
        case NULL:
          wrapper.isNull(field);
          break;
        case NONNULL:
          wrapper.isNotNull(field);
          break;
        case EMPTY:
          wrapper.eq(field, "");
          break;
        case NONEMPTY:
          wrapper.ne(field, "");
          break;
      }
    }
    return wrapper;
  }

  /**
   * 构建搜索查询对象
   *
   * @param wrapper 查询构建器
   * @param filters 筛选数组
   * @return 查询构建器
   */
  @NonNull
  public static QueryWrapper buildSearchQueryWrapper(
      @NonNull QueryWrapper wrapper, @Nullable FieldFilter[] filters) {
    if (Objects.isNull(filters) || filters.length == 0) {
      return wrapper;
    }
    // 遍历筛选
    wrapper.and(
        w -> {
          for (FieldFilter filter : filters) {
            switch (filter.getOperator()) {
              case CONTAINS:
                w.or(ww -> ww.like(filter.getField(), filter.getVal()), true);
                break;
              case STARTSWITH:
                w.or(ww -> ww.likeLeft(filter.getField(), filter.getVal()), true);
                break;
              case ENDSWITH:
                w.or(ww -> ww.likeRight(filter.getField(), filter.getVal()), true);
                break;
              case EQ:
                w.or(ww -> ww.eq(filter.getField(), filter.getVal()), true);
                break;
              default:
                break;
            }
          }
        });
    return wrapper;
  }

  /**
   * 构建排序搜索对象 sort: 英文逗号分割多个排序字段，默认升序，-结尾的字段降序<br>
   * 例： create_time- => order by create_time desc create_time,update_time- => order by create_time
   * asc, update_time desc
   *
   * @param wrapper 查询构建器
   * @param filters 排序数组
   * @return 查询构建器
   */
  @NonNull
  public static QueryWrapper buildSortQueryWrapper(
      @NonNull QueryWrapper wrapper, @Nullable SortFilter[] filters) {
    if (Objects.isNull(filters) || filters.length == 0) {
      return wrapper;
    }
    for (SortFilter filter : filters) {
      String field = filter.getField();
      if (!StringUtils.hasText(field)) {
        continue;
      }
      wrapper.orderBy(field, filter.isAsc());
    }
    return wrapper;
  }

  /**
   * 构建根节点查询对象
   *
   * @param wrapper 查询构建器
   * @return 查询构建器
   */
  @NonNull
  public static QueryWrapper buildRootQueryWrapper(@NonNull QueryWrapper wrapper) {
    wrapper.isNull(FieldConstants.PARENT_ID).orderBy(FieldConstants.SORT, true);
    return wrapper;
  }

  /**
   * 构建查询子节点对象
   *
   * @param wrapper 查询构建器
   * @param id 节点ID
   * @return 查询构建器
   */
  @NonNull
  public static QueryWrapper buildDirectChildrenQueryWrapper(
      @NonNull QueryWrapper wrapper, @Nullable Serializable id) {
    if (Objects.nonNull(id)) {
      wrapper.eq(FieldConstants.PARENT_ID, id).orderBy(FieldConstants.SORT, true);
    }
    return wrapper;
  }

  /**
   * 构建查询子节点对象
   *
   * @param wrapper 查询构建器
   * @param ids 节点ID 集合
   * @return 查询构建器
   */
  @NonNull
  public static QueryWrapper buildDirectChildrenQueryWrapper(
      @NonNull QueryWrapper wrapper, @Nullable Collection<? extends Serializable> ids) {
    if (Objects.nonNull(ids) && !ids.isEmpty()) {
      wrapper.in(FieldConstants.PARENT_ID, ids).orderBy(FieldConstants.SORT, true);
    }
    return wrapper;
  }
  //
  //  /** 获取分页对象 */
  //  @NonNull
  //  public static <T> Page<T> getEmptyPage() {
  //    return new Page<>(1, RequestConstants.REQUEST_PARAM_PAGE_SIZE_DEFAULT, 0);
  //  }

  //  /**
  //   * 根据 ID 获取对象
  //   *
  //   * @param service 服务
  //   * @param id ID
  //   * @return 实体对象
  //   */
  //  @Nullable
  //  public static <T> T getDataById(@NonNull IService<T> service, @Nullable Serializable id) {
  //    if (Objects.isNull(id)) {
  //      return null;
  //    }
  //    return service.getById(id);
  //  }

  //  /**
  //   * 根据 ID 集合获取对象
  //   *
  //   * @param service 服务
  //   * @param ids ID集合
  //   * @return 实体对象集合
  //   */
  //  @NonNull
  //  public static <T> List<T> getDataListByIds(
  //      @NonNull IService<T> service, @Nullable Collection<? extends Serializable> ids) {
  //    if (Objects.isNull(ids) || ids.isEmpty()) {
  //      return Collections.emptyList();
  //    }
  //    QueryWrapper wrapper = service.query().in(RequestConstants.PRIMARY_FIELD, ids);
  //    return service.list(wrapper);
  //  }

  /**
   * 是否有子节点
   *
   * @param service 服务
   * @param id 主键
   * @param <T> 实体类
   * @return boolean
   */
  public static <T> boolean hasChildren(@NonNull IService<T> service, @NonNull Serializable id) {
    QueryWrapper wrapper = service.query().eq(FieldConstants.PARENT_ID, id);
    return service.count(wrapper) > 0;
  }

  /**
   * 是否有子节点
   *
   * @param service 服务
   * @param ids 主键
   * @param <T> 实体类
   * @return boolean
   */
  public static <T> boolean hasChildren(
      @NonNull IService<T> service, @NonNull Collection<Serializable> ids) {
    if (ids.isEmpty()) {
      return false;
    }
    QueryWrapper wrapper = service.query().in(FieldConstants.PARENT_ID, ids);
    return service.count(wrapper) > 0;
  }

  /**
   * 获取节点的所有直接子节点
   *
   * @param service 服务
   * @param id ID
   * @param <T> 实体类
   * @return 数据列表
   */
  @NonNull
  public static <T> List<T> getDirectChildren(
      @NonNull IService<T> service, @Nullable Serializable id) {
    if (Objects.isNull(id)) {
      return Collections.emptyList();
    }
    return service.list(buildDirectChildrenQueryWrapper(service.query(), id));
  }

  /**
   * 获取节点的所有直接子节点
   *
   * @param service 服务
   * @param ids ID 集合
   * @param <T> 实体类
   * @return 数据列表
   */
  @NonNull
  public static <T> List<T> getDirectChildren(
      @NonNull IService<T> service, @Nullable Collection<? extends Serializable> ids) {
    if (Objects.isNull(ids) || ids.isEmpty()) {
      return Collections.emptyList();
    }
    return service.list(buildDirectChildrenQueryWrapper(service.query(), ids));
  }

  /**
   * 获取节点的所有直接子节点ID
   *
   * @param service 服务
   * @param ids ID 集合
   * @param <T> 实体类
   * @return 数据列表
   */
  @NonNull
  public static <T> List<T> getDirectChildrenIds(
      @NonNull IService<T> service, @Nullable Collection<? extends Serializable> ids) {
    if (Objects.isNull(ids) || ids.isEmpty()) {
      return Collections.emptyList();
    }
    return service.list(
        buildDirectChildrenQueryWrapper(service.query().select(FieldConstants.PRIMARY_KEY), ids));
  }

  /**
   * 获取节点的所有直接子节点ID
   *
   * @param service 服务
   * @param id ID
   * @param <T> 实体类
   * @return 数据列表
   */
  @NonNull
  public static <T> List<T> getDirectChildrenIds(
      @NonNull IService<T> service, @Nullable Serializable id) {
    if (Objects.isNull(id)) {
      return Collections.emptyList();
    }
    return service.list(
        buildDirectChildrenQueryWrapper(service.query().select(FieldConstants.PRIMARY_KEY), id));
  }

  /**
   * 创建对象
   *
   * @param service 服务
   * @param obj 对象
   * @param <T> 实体类
   * @return boolean
   */
  public static <T> boolean createData(@NonNull IService<T> service, @NonNull T obj) {
    return service.save(obj);
  }

  /**
   * 批量创建对象
   *
   * @param service 服务
   * @param objList 对象
   * @param <T> 实体类
   * @return boolean
   */
  public static <T> boolean batchCreateData(
      @NonNull IService<T> service, @NonNull Collection<T> objList) {
    return service.saveBatch(objList);
  }

  /**
   * 对象更新
   *
   * @param service 服务
   * @param obj 对象
   * @param id 主键
   * @param <T> 实体类
   * @return boolean
   */
  public static <T> boolean updateData(
      @NonNull IService<T> service, @NonNull T obj, @NonNull Serializable id) {
    boolean ret;
    if (obj instanceof ICommonEntity) {
      // 检查更新时间
      QueryWrapper wrapper =
          service
              .query()
              .eq(FieldConstants.PRIMARY_KEY, id)
              .le(FieldConstants.UPDATED_TIME, ((ICommonEntity) obj).getUpdatedTime());
      ret = service.update(obj, wrapper);
    } else if (obj instanceof IAppendOnlyEntity) {
      // 不允许更新
      ret = false;
    } else {
      ret = service.updateById(obj);
    }
    return ret;
  }

  //  /**
  //   * 删除树状对象
  //   * @param service 服务
  //   * @param id 主键
  //   * @param deletionPolicy 删除策略
  //   * @param <T> 实体类
  //   * @return boolean
  //   */
  //  public static <T> boolean deleteTree(@NonNull IService<T> service, @NonNull Serializable id,
  // @NonNull DeletionPolicy deletionPolicy) {
  //    if (deletionPolicy.equals(DeletionPolicy.PROTECT)) {
  //      // 保护策略，存在子节点则失败
  //      return !hasChildren(service, id);
  //    } else if (deletionPolicy.equals(DeletionPolicy.CASCADE)) {
  //      // 级联删除策略，删除所有子节点
  //      return removeAllChildren(service, id);
  //    }
  //    return true;
  //  }

  //  /**
  //   * 删除树状对象
  //   * @param service 服务
  //   * @param ids 主键集合
  //   * @param deletionPolicy 删除策略
  //   * @param <T> 实体类
  //   * @return boolean
  //   */
  //  public static <T> boolean deleteTree(@NonNull IService<T> service, @NonNull
  // Collection<Serializable> ids, @NonNull DeletionPolicy deletionPolicy) {
  //    if (deletionPolicy.equals(DeletionPolicy.PROTECT)) {
  //      // 保护策略，存在子节点则失败
  //      return !hasChildren(service, ids);
  //    } else if (deletionPolicy.equals(DeletionPolicy.CASCADE)) {
  //      // 级联删除策略，删除所有子节点
  //      return removeAllChildren(service, ids);
  //    }
  //    return true;
  //  }

  /**
   * 对象删除
   *
   * @param service 服务
   * @param id 主键
   * @param <T> 实体类
   * @return boolean
   */
  public static <T> boolean deleteData(@NonNull IService<T> service, @NonNull Serializable id) {
    return service.removeById(id);
  }

  /**
   * 批量对象删除
   *
   * @param service 服务
   * @param ids id集合
   * @param <T> 实体类
   * @return boolean
   */
  public static <T> boolean batchDeleteData(
      @NonNull IService<T> service, @NonNull Collection<? extends Serializable> ids) {
    return service.removeByIds(ids);
  }

  /**
   * 删除所有子节点
   *
   * @param service 服务
   * @param id 主键
   * @param <T> 实体类
   * @return boolean
   */
  public static <T> boolean removeAllChildren(
      @NonNull IService<T> service, @NonNull Serializable id) {
    QueryWrapper wrapper = service.query().eq(FieldConstants.PARENT_ID, id);
    return service.remove(wrapper);
  }

  /**
   * 删除所有子节点
   *
   * @param service 服务
   * @param ids 主键集合
   * @param <T> 实体类
   * @return boolean
   */
  public static <T> boolean removeAllChildren(
      @NonNull IService<T> service, @NonNull Collection<Serializable> ids) {
    if (ids.isEmpty()) {
      return false;
    }
    QueryWrapper wrapper = service.query().in(FieldConstants.PARENT_ID, ids);
    return service.remove(wrapper);
  }

  /**
   * 是否存在重复字段行
   *
   * @param service 服务
   * @param obj 对象
   * @param newRecord 是否新建数据
   * @param <I> 主键类
   * @param <T> 实体类
   * @return boolean
   */
  //  public static <I extends Serializable, T extends IdEntity<I>> boolean isUniqueDuplicated(
  //      @NonNull IService<T> service, @NonNull T obj, boolean newRecord) {
  //    if (obj instanceof IUniqueEntity) {
  //      try {
  //        QueryWrapper wrapper = buildUniqueCheckQueryWrapper(service.query(), obj, newRecord);
  //        return service.count(wrapper) > 0;
  //      } catch (IllegalArgumentException ignored) {
  //        return false;
  //      }
  //    } else {
  //      return false;
  //    }
  //  }
}
