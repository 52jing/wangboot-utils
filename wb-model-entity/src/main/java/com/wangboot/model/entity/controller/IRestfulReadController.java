package com.wangboot.model.entity.controller;

import com.wangboot.core.web.response.DetailBody;
import com.wangboot.core.web.response.ListBody;
import com.wangboot.core.web.utils.ResponseUtils;
import com.wangboot.core.web.utils.ServletUtils;
import com.wangboot.model.entity.IRestfulService;
import com.wangboot.model.entity.IdEntity;
import com.wangboot.model.entity.exception.NotFoundException;
import com.wangboot.model.entity.request.FieldFilter;
import com.wangboot.model.entity.request.ParamFilterDefinition;
import com.wangboot.model.entity.request.SearchStrategy;
import com.wangboot.model.entity.request.SortFilter;
import com.wangboot.model.entity.utils.RequestUtils;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Restful 只读控制器接口
 *
 * @param <I> 主键类型
 * @param <T> 实体类型
 */
public interface IRestfulReadController<I extends Serializable, T extends IdEntity<I>> {

  /** 获取服务 */
  @NonNull
  IRestfulService<I, T> getReadService();

  @Nullable
  default HttpServletRequest getRequest() {
    return ServletUtils.getRequest();
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
  default T view(@Nullable I id) {
    T entity = getReadService().viewResource(id);
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
  default ResponseEntity<?> viewResponse(@Nullable I id) {
    return ResponseUtils.success(DetailBody.ok(view(id)));
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
        .listResourcesAll(parseSortFilters(), parseParamFilters(), parseSearchFilters());
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
        .listResourcesPage(
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
  default List<T> listRoot() {
    return getReadService().listRootResources();
  }

  /**
   * 获取根节点数据接口
   *
   * @return 响应对象
   */
  @NonNull
  default ResponseEntity<?> listRootResponse() {
    List<T> data = listRoot();
    return ResponseUtils.success(ListBody.ok(data));
  }

  /**
   * 获取直接子节点数据
   *
   * @param id ID
   * @return 数据
   */
  @NonNull
  default List<T> listDirectChildren(@Nullable I id) {
    return getReadService().listDirectChildren(id);
  }

  /**
   * 获取直接子节点数据接口
   *
   * @param id ID
   * @return 响应对象
   */
  @NonNull
  default ResponseEntity<?> listDirectChildrenResponse(@Nullable I id) {
    List<T> data = listDirectChildren(id);
    return ResponseUtils.success(ListBody.ok(data));
  }

  /**
   * 获取直接子节点数据
   *
   * @param ids ID集合
   * @return 数据
   */
  @NonNull
  default List<T> listDirectChildren(@Nullable Collection<I> ids) {
    return getReadService().listDirectChildren(ids);
  }

  /**
   * 获取直接子节点数据接口
   *
   * @param ids ID集合
   * @return 响应对象
   */
  @NonNull
  default ResponseEntity<?> listDirectChildrenResponse(@Nullable Collection<I> ids) {
    List<T> data = listDirectChildren(ids);
    return ResponseUtils.success(ListBody.ok(data));
  }
}
