package com.wangboot.model.entity.utils;

import cn.hutool.core.util.ArrayUtil;
import com.wangboot.core.web.utils.ServletUtils;
import com.wangboot.model.entity.RequestConstants;
import com.wangboot.model.entity.request.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 请求工具类
 *
 * @author wwtg99
 */
public class RequestUtils {
  private RequestUtils() {}

  /**
   * 构建排序筛选
   *
   * @param sort 排序参数
   * @param defaultSorts 默认排序数组
   * @param sortableFields 可排序字段
   * @return 排序筛选数组
   */
  @Nullable
  public static SortFilter[] buildSortFilter(
      String sort, @Nullable SortFilter[] defaultSorts, @Nullable String[] sortableFields) {
    String[] sortList;
    if (!StringUtils.hasText(sort)) {
      // 未提供排序字段
      if (Objects.nonNull(defaultSorts) && defaultSorts.length > 0) {
        // 提供了默认排序
        return defaultSorts;
      } else {
        // 无默认排序
        return null;
      }
    } else {
      // 获取排序字段数组
      if (sort.contains(RequestConstants.REQUEST_PARAM_SORT_DELIMITER)) {
        sortList = sort.split(RequestConstants.REQUEST_PARAM_SORT_DELIMITER);
      } else {
        sortList = new String[] {sort};
      }
    }
    List<SortFilter> filterList = new ArrayList<>();
    // 获取可排序字段
    boolean sortFieldsSet = Objects.nonNull(sortableFields) && sortableFields.length > 0;
    for (String s : sortList) {
      // 获取字段
      String field = s;
      boolean asc = true;
      if (s.endsWith("-")) {
        asc = false;
        field = s.substring(0, s.length() - 1);
      }
      // 若没有设置可排序字段或设置了可排序字段且在字段中，则添加排序筛选
      if (!sortFieldsSet || ArrayUtil.contains(sortableFields, field)) {
        filterList.add(new SortFilter(field, asc));
      }
    }
    return filterList.toArray(new SortFilter[] {});
  }

  /**
   * 构建筛选
   *
   * @param paramFilterDefinition 筛选定义
   * @param parameters 参数
   * @return 筛选数组
   */
  @NonNull
  public static FieldFilter[] buildParamFieldFilter(
      @Nullable ParamFilterDefinition paramFilterDefinition,
      @Nullable Map<String, String> parameters) {
    List<FieldFilter> fieldFilters = new ArrayList<>();
    // 字段筛选
    if (Objects.nonNull(paramFilterDefinition)
        && !paramFilterDefinition.getParams().isEmpty()
        && Objects.nonNull(parameters)) {
      for (Map.Entry<String, FieldFilter> entry : paramFilterDefinition.getParams().entrySet()) {
        // 跳过空参数
        if (!StringUtils.hasText(entry.getKey())) {
          continue;
        }
        // 获取请求参数
        String val = parameters.get(entry.getKey());
        // 跳过空值
        if (!StringUtils.hasText(val)) {
          continue;
        }
        // 获取字段
        String field = entry.getKey();
        FilterOperator operator = FilterOperator.EQ;
        ParamValType type = ParamValType.STR;
        if (Objects.nonNull(entry.getValue())) {
          field = entry.getValue().getField();
          operator = entry.getValue().getOperator();
          type = entry.getValue().getType();
        }
        fieldFilters.add(new FieldFilter(field, val, operator, type));
      }
    }
    return fieldFilters.toArray(new FieldFilter[] {});
  }

  /**
   * 构建搜索
   *
   * @param query 搜索字符串
   * @param searchFields 搜索字段
   * @param searchStrategy 搜索策略
   * @return 筛选数组
   */
  @NonNull
  public static FieldFilter[] buildSearchFilter(
      @Nullable String query,
      @Nullable String[] searchFields,
      @Nullable SearchStrategy searchStrategy) {
    List<FieldFilter> fieldFilters = new ArrayList<>();
    if (StringUtils.hasText(query) && Objects.nonNull(searchFields) && searchFields.length > 0) {
      // 处理字符串
      query = StringUtils.trimTrailingCharacter(StringUtils.trimLeadingCharacter(query, '%'), '%');
      for (String field : searchFields) {
        FilterOperator operator = FilterOperator.EQ;
        if (Objects.nonNull(searchStrategy)) {
          switch (searchStrategy) {
            case LEFT_LIKE:
              operator = FilterOperator.STARTSWITH;
              break;
            case RIGHT_LIKE:
              operator = FilterOperator.ENDSWITH;
              break;
            case BOTH_LIKE:
              operator = FilterOperator.CONTAINS;
              break;
            default:
              break;
          }
        }
        fieldFilters.add(new FieldFilter(field, query, operator, ParamValType.STR));
      }
    }
    return fieldFilters.toArray(new FieldFilter[] {});
  }

  /** 获取请求参数 */
  @NonNull
  public static Map<String, String> getParametersMap(@Nullable HttpServletRequest request) {
    if (Objects.isNull(request)) {
      request = ServletUtils.getRequest();
    }
    Map<String, String[]> map = ServletUtils.getParameterMap(request);
    Map<String, String> out = new HashMap<>();
    map.forEach(
        (k, v) -> {
          if (Objects.nonNull(v) && v.length > 0) {
            out.put(k, v[0]);
          }
        });
    return out;
  }

  /** 获取搜索参数 */
  public static String getSearchParam(@Nullable HttpServletRequest request) {
    if (Objects.isNull(request)) {
      request = ServletUtils.getRequest();
    }
    return ServletUtils.getParameter(request, RequestConstants.REQUEST_PARAM_SEARCH, "");
  }

  /** 获取排序参数 */
  public static String getSortParam(@Nullable HttpServletRequest request) {
    if (Objects.isNull(request)) {
      request = ServletUtils.getRequest();
    }
    return ServletUtils.getParameter(request, RequestConstants.REQUEST_PARAM_SORT, "");
  }

  /** 获取页码参数 */
  public static long getPageParam(@Nullable HttpServletRequest request) {
    if (Objects.isNull(request)) {
      request = ServletUtils.getRequest();
    }
    Long i =
        ServletUtils.getParameterToLong(
            request,
            RequestConstants.REQUEST_PARAM_PAGE,
            RequestConstants.REQUEST_PARAM_PAGE_DEFAULT);
    if (Objects.isNull(i)) {
      return RequestConstants.REQUEST_PARAM_PAGE_DEFAULT;
    } else {
      return i;
    }
  }

  /** 获取每页数量参数 */
  public static long getPageSizeParam(
      @Nullable HttpServletRequest request, @Nullable Long maxPageSize) {
    if (Objects.isNull(request)) {
      request = ServletUtils.getRequest();
    }
    Long i =
        ServletUtils.getParameterToLong(
            request,
            RequestConstants.REQUEST_PARAM_PAGE_SIZE,
            RequestConstants.REQUEST_PARAM_PAGE_SIZE_DEFAULT);
    long pageSize;
    if (Objects.isNull(i)) {
      pageSize = RequestConstants.REQUEST_PARAM_PAGE_SIZE_DEFAULT;
    } else {
      pageSize = i;
    }
    // 检查是否超过最大分页数
    if (Objects.nonNull(maxPageSize) && pageSize > maxPageSize) {
      return maxPageSize;
    }
    return pageSize;
  }
}
