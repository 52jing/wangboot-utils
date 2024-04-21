package com.wangboot.model.entity;

/**
 * 请求常量
 *
 * @author wwtg99
 */
public class RequestConstants {

  private RequestConstants() {}

  /** 请求页码参数 */
  public static final String REQUEST_PARAM_PAGE = "_page";

  /** 请求每页大小参数 */
  public static final String REQUEST_PARAM_PAGE_SIZE = "_limit";

  /** 搜索参数 */
  public static final String REQUEST_PARAM_SEARCH = "_search";

  /** 排序参数 */
  public static final String REQUEST_PARAM_SORT = "_sort";

  /** 请求页码默认值 */
  public static final long REQUEST_PARAM_PAGE_DEFAULT = 1;

  /** 请求每页大小默认值 */
  public static final long REQUEST_PARAM_PAGE_SIZE_DEFAULT = 10;

  /** 请求筛选 IN 分隔符 */
  public static final String REQUEST_PARAM_IN_DELIMITER = ",";

  /** 请求参数 sort 分隔符 */
  public static final String REQUEST_PARAM_SORT_DELIMITER = ",";

  /** 获取子数据路径 */
  public static final String PATH_CHILDREN = "_children";

  /** 获取根数据路径 */
  public static final String PATH_ROOT = "_root";
}
