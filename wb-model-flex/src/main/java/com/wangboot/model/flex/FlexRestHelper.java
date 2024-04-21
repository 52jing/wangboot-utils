package com.wangboot.model.flex;

import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.wangboot.core.auth.authorization.IAuthorizationResource;
import com.wangboot.core.utils.StrUtils;
import com.wangboot.model.dataauthority.authorizer.IDataAuthorizer;
import com.wangboot.model.entity.FieldConstants;
import com.wangboot.model.entity.IUniqueEntity;
import com.wangboot.model.entity.RequestConstants;
import com.wangboot.model.entity.request.FieldFilter;
import com.wangboot.model.entity.request.SortFilter;
import java.io.Serializable;
import java.util.Collection;
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
   * 限制数据权限
   *
   * @param wrapper 查询构建器
   * @param dataAuthorizer 数据权限管理器
   * @return 查询构建器
   */
  @NonNull
  public static QueryWrapper buildDataAuthorityQueryWrapper(
      @NonNull QueryWrapper wrapper, @Nullable IDataAuthorizer dataAuthorizer) {
    if (Objects.isNull(dataAuthorizer)) {
      return wrapper;
    }
    List<String> scopes =
        dataAuthorizer.getAuthorities().stream()
            .map(IAuthorizationResource::getResourceName)
            .collect(Collectors.toList());
    return wrapper.in(dataAuthorizer.getField(), scopes);
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
}
