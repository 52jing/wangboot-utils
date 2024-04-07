package com.wangboot.model.dataauthority.utils;

import com.wangboot.model.dataauthority.DataAuthority;
import com.wangboot.model.dataauthority.authorizer.IDataAuthorizer;
import com.wangboot.model.dataauthority.factory.IDataAuthorizerFactory;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 数据权限工具类
 *
 * @author wwtg99
 */
public class DataAuthorityUtils {

  private DataAuthorityUtils() {}

  /** 是否限制数据权限 */
  public static boolean restrictDataAuthority(@NonNull Class<?> entityClass) {
    return entityClass.isAnnotationPresent(DataAuthority.class);
  }

  /** 获取并检查数据权限注解 */
  @NonNull
  public static DataAuthority getDataAuthority(@NonNull Class<?> entityClass) {
    DataAuthority dataAuthority = entityClass.getAnnotation(DataAuthority.class);
    if (Objects.nonNull(dataAuthority)) {
      return dataAuthority;
    } else {
      throw new IllegalArgumentException("No DataAuthority annotation found for entity!");
    }
  }

  /**
   * 获取数据权限授权者，如果不支持则返回 null
   *
   * @param entityClass 实体类型
   * @return 数据权限授权者
   */
  @Nullable
  public static IDataAuthorizer getDataAuthorizer(@NonNull Class<?> entityClass) {
    if (!restrictDataAuthority(entityClass)) {
      return null;
    }
    DataAuthority dataAuthority = getDataAuthority(entityClass);
    try {
      IDataAuthorizerFactory factory = dataAuthority.factory().newInstance();
      return factory.getDataAuthorizer(dataAuthority.field());
    } catch (InstantiationException | IllegalAccessException ignored) {
      return null;
    }
  }
}
