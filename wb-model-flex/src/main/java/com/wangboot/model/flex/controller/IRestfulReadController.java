package com.wangboot.model.flex.controller;

import com.wangboot.core.web.exception.NotFoundException;
import com.wangboot.core.web.response.DetailBody;
import com.wangboot.core.web.response.ListBody;
import com.wangboot.core.web.utils.ResponseUtils;
import com.wangboot.model.entity.IRestfulService;
import com.wangboot.model.entity.request.FieldFilter;
import com.wangboot.model.entity.request.ParamFilterDefinition;
import com.wangboot.model.entity.request.SearchStrategy;
import com.wangboot.model.entity.request.SortFilter;
import com.wangboot.model.entity.utils.RequestUtils;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Restful 只读控制器接口
 *
 * @param <T> 实体类型
 */
public interface IRestfulReadController<T> extends IRestfulController {

  /** 获取服务 */
  @NonNull
  IRestfulService<T> getReadService();

  /** 获取实体类型 */
  @NonNull
  default Class<T> getReadEntityClass() {
    return getReadService().getEntityClass();
  }

  /**
   * 获取排序参数
   *
   * @return 排序参数
   */
  default String getSort() {
    return RequestUtils.getSortParam(getRequest());
  }

  /**
   * 配置方法<br>
   * 限制可排序字段，为空则不限制
   *
   * @return 可排序字段数组
   */
  @Nullable
  default String[] getSortableFields() {
    return new String[0];
  }

  /**
   * 配置方法<br>
   * 提供默认的排序，为空则默认不排序
   *
   * @return 默认排序数组
   */
  @Nullable
  default SortFilter[] getDefaultSort() {
    return new SortFilter[0];
  }

  /**
   * 生成排序过滤器
   *
   * @return 排序过滤器数组
   */
  @Nullable
  default SortFilter[] parseSortFilters() {
    return RequestUtils.buildSortFilter(getSort(), getDefaultSort(), getSortableFields());
  }

  /**
   * 获取搜索参数
   *
   * @return 搜索参数
   */
  default String getQuery() {
    return RequestUtils.getSearchParam(getRequest());
  }

  /**
   * 配置方法<br>
   * 获取可搜索字段，为空则不支持搜索
   *
   * @return 可搜索字段数组
   */
  @Nullable
  default String[] getSearchableFields() {
    return new String[0];
  }

  /**
   * 配置方法<br>
   * 获取搜索匹配策略
   *
   * @return 搜索匹配策略
   */
  @NonNull
  default SearchStrategy getSearchStrategy() {
    return SearchStrategy.LEFT_LIKE;
  }

  /**
   * 生成搜索过滤器
   *
   * @return 搜索过滤器数组
   */
  default FieldFilter[] parseSearchFilters() {
    return RequestUtils.buildSearchFilter(getQuery(), getSearchableFields(), getSearchStrategy());
  }

  /**
   * 配置方法<br>
   * 获取筛选规则定义
   *
   * @return 筛选规则
   */
  @Nullable
  default ParamFilterDefinition getParamFilterDefinition() {
    return null;
  }

  /**
   * 获取筛选参数
   *
   * @return 筛选参数字典
   */
  @Nullable
  default Map<String, String> getFilterParameters() {
    return RequestUtils.getParametersMap(getRequest());
  }

  /**
   * 生成筛选参数过滤器
   *
   * @return 筛选参数过滤器
   */
  default FieldFilter[] parseParamFilters() {
    return RequestUtils.buildParamFieldFilter(getParamFilterDefinition(), getFilterParameters());
  }

  /**
   * 配置方法<br>
   * 获取最大每页数量，为 null 则不限制
   *
   * @return 最大每页数量
   */
  @Nullable
  default Long getMaxPageSize() {
    return 500L;
  }

  /**
   * 获取页码
   *
   * @return 页码
   */
  default long getPage() {
    return RequestUtils.getPageParam(getRequest());
  }

  /**
   * 获取每页数量
   *
   * @return 每页数量
   */
  default long getPageSize() {
    return RequestUtils.getPageSizeParam(getRequest(), getMaxPageSize());
  }

  /**
   * 单个查询
   *
   * @param id ID
   * @return 数据
   */
  @NonNull
  default T detail(@Nullable Serializable id) {
    T entity = getReadService().getDetailById(id);
    if (Objects.isNull(entity)) {
      throw new NotFoundException();
    }
    return entity;
  }

  /**
   * 单个查询接口
   *
   * @param id ID
   * @return 响应对象
   */
  @NonNull
  default ResponseEntity<?> detailResponse(@Nullable Serializable id) {
    return ResponseUtils.success(DetailBody.ok(detail(id)));
  }

  /**
   * 获取所有数据<br>
   * 基于排序过滤器、搜索过滤器和筛选过滤器
   *
   * @return 数据
   */
  @NonNull
  default ListBody<T> listAll() {
    return getReadService()
        .getListAll(parseSortFilters(), parseParamFilters(), parseSearchFilters());
  }

  /**
   * 获取所有数据接口
   *
   * @return 响应对象
   */
  @NonNull
  default ResponseEntity<?> listAllResponse() {
    return ResponseUtils.success(listAll());
  }

  /**
   * 获取分页数据<br>
   * 基于排序过滤器、搜索过滤器和筛选过滤器
   *
   * @return 数据
   */
  @NonNull
  default ListBody<T> listPage() {
    return getReadService()
        .getListPage(
            parseSortFilters(),
            parseParamFilters(),
            parseSearchFilters(),
            getPage(),
            getPageSize());
  }

  /**
   * 获取分页数据接口
   *
   * @return 响应对象
   */
  @NonNull
  default ResponseEntity<?> listPageResponse() {
    return ResponseUtils.success(listPage());
  }

  /**
   * 获取根节点数据
   *
   * @return 数据
   */
  @NonNull
  default ListBody<T> listRoot() {
    return getReadService().getRootData();
  }

  /**
   * 获取根节点数据接口
   *
   * @return 响应对象
   */
  @NonNull
  default ResponseEntity<?> listRootResponse() {
    return ResponseUtils.success(listRoot());
  }

  /**
   * 获取直接子节点数据
   *
   * @param id ID
   * @return 数据
   */
  @NonNull
  default ListBody<T> listDirectChildren(@Nullable Serializable id) {
    return getReadService().getDirectChildren(id);
  }

  /**
   * 获取直接子节点数据接口
   *
   * @param id ID
   * @return 响应对象
   */
  @NonNull
  default ResponseEntity<?> listDirectChildrenResponse(@Nullable Serializable id) {
    return ResponseUtils.success(listDirectChildren(id));
  }

  /**
   * 获取直接子节点数据
   *
   * @param ids ID集合
   * @return 数据
   */
  @NonNull
  default ListBody<T> listDirectChildren(@Nullable Collection<? extends Serializable> ids) {
    return getReadService().getDirectChildren(ids);
  }

  /**
   * 获取直接子节点数据接口
   *
   * @param ids ID集合
   * @return 响应对象
   */
  @NonNull
  default ResponseEntity<?> listDirectChildrenResponse(
      @Nullable Collection<? extends Serializable> ids) {
    return ResponseUtils.success(listDirectChildren(ids));
  }
}
