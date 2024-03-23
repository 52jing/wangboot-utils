package com.wangboot.core.auth.authorization.resource;

import com.wangboot.core.auth.authorization.IAuthorizationResource;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * API 接口资源
 *
 * @author wwtg99
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class ApiResource implements IAuthorizationResource {

  /** 权限操作 */
  public static final String REST_PERMISSION_ACTION_VIEW = "view";

  public static final String REST_PERMISSION_ACTION_CREATE = "create";
  public static final String REST_PERMISSION_ACTION_UPDATE = "update";
  public static final String REST_PERMISSION_ACTION_DELETE = "delete";

  /** 权限分隔符 */
  public static final String DEL = ":";

  private String group = "";

  private String name = "";

  private String action = "";

  @Override
  public String getResourceName() {
    return Stream.of(this.getGroup(), this.getName(), this.getAction())
        .filter(StringUtils::hasText)
        .collect(Collectors.joining(DEL));
  }

  @Override
  public String toString() {
    return this.getResourceName();
  }

  @Nullable
  public static ApiResource of(String str) {
    if (!StringUtils.hasText(str)) {
      return null;
    }
    String[] sp = str.split(DEL);
    if (sp.length >= 3) {
      String group = String.join(DEL, Arrays.copyOfRange(sp, 0, sp.length - 2));
      return new ApiResource(group, sp[sp.length - 2], sp[sp.length - 1]);
    } else if (sp.length == 2) {
      return new ApiResource("", sp[0], sp[1]);
    } else if (sp.length == 1) {
      return new ApiResource("", sp[0], "");
    } else {
      return null;
    }
  }
}
