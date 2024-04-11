package com.wangboot.core.web.interceptor;

import com.wangboot.core.web.exception.InMaintenanceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 维护中拦截器
 *
 * @author wwtg99
 */
@AllArgsConstructor
@Generated
public class InMaintenanceInterceptor implements HandlerInterceptor {

  @Getter @Setter private boolean inMaintenance;

  @Getter @Setter private String maintenanceNotice;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (this.inMaintenance) {
      throw new InMaintenanceException(this.maintenanceNotice);
    }
    return true;
  }
}
