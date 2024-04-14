package com.wangboot.model.entity.utils;

import cn.hutool.core.bean.BeanException;
import cn.hutool.core.bean.DynaBean;
import com.wangboot.core.utils.ObjectUtils;
import com.wangboot.model.entity.*;
import com.wangboot.model.entity.event.EnableOperationLog;
import java.io.Serializable;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 实体工具类
 *
 * @author wwtg99
 */
public class EntityUtils {
  private EntityUtils() {}

  /** 判断对象是否可编辑 */
  public static boolean isReadonly(@Nullable Object obj) {
    if (obj instanceof IEditableEntity) {
      return ((IEditableEntity) obj).readonly();
    }
    return false;
  }

  /** 是否可编辑实体 */
  public static boolean isEditableEntity(@Nullable Object obj) {
    return obj instanceof IEditableEntity;
  }

  /** 是否可编辑实体 */
  public static <T> boolean isEditableEntity(@Nullable Class<T> entityClass) {
    if (Objects.isNull(entityClass)) {
      return false;
    }
    return ObjectUtils.hasInterface(entityClass, IEditableEntity.class);
  }

  /** 判断对象是否树状 */
  public static boolean isTreeEntity(@Nullable Object obj) {
    return obj instanceof ITreeEntity;
  }

  /** 是否树状实体 */
  public static <T> boolean isTreeEntity(@Nullable Class<T> entityClass) {
    if (Objects.isNull(entityClass)) {
      return false;
    }
    return ObjectUtils.hasInterface(entityClass, ITreeEntity.class);
  }

  /** 判断对象是否仅插入实体 */
  public static boolean isAppendOnlyEntity(@Nullable Object obj) {
    return obj instanceof IAppendOnlyEntity;
  }

  /** 判断对象是否仅插入实体 */
  public static <T> boolean isAppendOnlyEntity(@Nullable Class<T> entityClass) {
    if (Objects.isNull(entityClass)) {
      return false;
    }
    return ObjectUtils.hasInterface(entityClass, IAppendOnlyEntity.class);
  }

  /** 判断对象是否普通实体 */
  public static boolean isCommonEntity(@Nullable Object obj) {
    return obj instanceof ICommonEntity;
  }

  /** 判断对象是否普通实体 */
  public static <T> boolean isCommonEntity(@Nullable Class<T> entityClass) {
    if (Objects.isNull(entityClass)) {
      return false;
    }
    return ObjectUtils.hasInterface(entityClass, ICommonEntity.class);
  }

  /** 判断对象是否支持唯一性 */
  public static boolean isUniqueEntity(@Nullable Object obj) {
    return obj instanceof IUniqueEntity;
  }

  /** 判断对象是否支持唯一性 */
  public static <T> boolean isUniqueEntity(@Nullable Class<T> entityClass) {
    if (Objects.isNull(entityClass)) {
      return false;
    }
    return ObjectUtils.hasInterface(entityClass, IUniqueEntity.class);
  }

  /**
   * 获取实体的某个字段值
   *
   * @param entity 实体对象
   * @param field 字段名称
   * @param throwEx 不存在的属性是否抛出异常
   * @param nonExistVal 不存在的属性返回值
   * @return 字段值
   */
  public static Object getEntityField(
      Object entity, String field, boolean throwEx, Object nonExistVal) {
    try {
      return DynaBean.create(entity).get(field);
    } catch (BeanException e) {
      if (throwEx) {
        throw e;
      } else {
        return nonExistVal;
      }
    }
  }

  /**
   * 获取实体的某个字段值，不存在的属性返回 null
   *
   * @param entity 实体对象
   * @param field 字段名称
   * @return 字段值
   */
  public static Object getEntityField(Object entity, String field) {
    return getEntityField(entity, field, false, null);
  }

  /**
   * 获取对象的识别码
   *
   * @param obj 对象
   * @return 主键
   */
  @NonNull
  public static Serializable getEntityIdentifier(@Nullable Object obj) {
    if (Objects.isNull(obj)) {
      return "";
    }
    if (obj instanceof IdEntity) {
      return ((IdEntity<? extends Serializable>) obj).getId();
    } else {
      return obj.toString();
    }
  }

  /**
   * 获取对象的识别码
   *
   * @param obj 对象
   * @return 字符串
   */
  @NonNull
  public static String getEntityIdentifierStr(@Nullable Object obj) {
    if (Objects.isNull(obj)) {
      return "";
    }
    if (obj instanceof IdEntity) {
      return ((IdEntity<?>) obj).getId().toString();
    } else {
      return obj.toString();
    }
  }

  /**
   * 设置对象的识别码
   *
   * @param obj 对象
   * @param id 识别码
   */
  @SuppressWarnings("unchecked")
  public static void setEntityIdentifier(@Nullable Object obj, @Nullable Serializable id) {
    if (Objects.isNull(obj) || Objects.isNull(id)) {
      return;
    }
    if (obj instanceof IdEntity) {
      ((IdEntity<Serializable>) obj).setId(id);
    }
  }

  /** 是否启用操作日志记录 */
  public static boolean isOperationLogEnabled(@NonNull Class<?> entityClass) {
    return entityClass.isAnnotationPresent(EnableOperationLog.class);
  }

  /** 获取操作日志注解 */
  @NonNull
  public static EnableOperationLog getOperationLogAnnotation(@NonNull Class<?> entityClass) {
    EnableOperationLog annotation = entityClass.getAnnotation(EnableOperationLog.class);
    if (Objects.nonNull(annotation)) {
      return annotation;
    } else {
      throw new IllegalArgumentException("No EnableOperationLog annotation found for entity!");
    }
  }
}
