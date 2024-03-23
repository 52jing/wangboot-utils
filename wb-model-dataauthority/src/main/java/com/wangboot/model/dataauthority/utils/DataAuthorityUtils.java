package com.wangboot.model.dataauthority.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.wangboot.core.auth.user.IUserModel;
import com.wangboot.model.dataauthority.DataAuthority;
import com.wangboot.model.dataauthority.DataAuthorityType;
import com.wangboot.model.dataauthority.authorizer.IDataAuthorizer;
import com.wangboot.model.dataauthority.authorizer.IDataAuthorizerService;
import com.wangboot.model.dataauthority.authorizer.NoneDataAuthorizer;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 数据权限工具类
 *
 * @author wwtg99
 */
public class DataAuthorityUtils {

  private DataAuthorityUtils() {}

  /** 是否限制数据权限 */
  public static <T> boolean restrictDataAuthority(@NonNull Class<T> entityClass) {
    return entityClass.isAnnotationPresent(DataAuthority.class);
  }

  /** 获取并检查数据权限注解 */
  @NonNull
  public static <T> DataAuthority getDataAuthority(@NonNull Class<T> entityClass) {
    DataAuthority dataAuthority = entityClass.getAnnotation(DataAuthority.class);
    if (Objects.nonNull(dataAuthority)) {
      if (DataAuthorityType.DATA_SCOPE.equals(dataAuthority.value())) {
        // 必须提供 field
        if (!StringUtils.hasText(dataAuthority.field())) {
          throw new IllegalArgumentException("No field specified for DATA_SCOPE type!");
        }
      } else if (DataAuthorityType.USER_ID_FIELD.equals(dataAuthority.value())) {
        // 必须提供 field
        if (!StringUtils.hasText(dataAuthority.field())) {
          throw new IllegalArgumentException("No field specified for USER_ID_FIELD type!");
        }
      }
      return dataAuthority;
    } else {
      throw new IllegalArgumentException("No DataAuthority annotation found for entity!");
    }
  }

  @NonNull
  public static IDataAuthorizerService getDataAuthorizerService() {
    return Optional.ofNullable(SpringUtil.getBean(IDataAuthorizerService.class))
        .orElseThrow(
            () -> new NoSuchBeanDefinitionException("No IDataAuthorizerService is found!"));
  }

  @NonNull
  public static <T> IDataAuthorizer getDataAuthorizer(
      @Nullable IUserModel userModel, @Nullable Class<T> entityClass) {
    return Optional.ofNullable(getDataAuthorizerService().getDataAuthorizer(userModel, entityClass))
        .orElse(new NoneDataAuthorizer(false));
  }
}
