package com.wangboot.core.auth.authorization;

import java.io.Serializable;

/**
 * 可授权资源接口
 *
 * @author wwtg99
 */
public interface IAuthorizationResource extends Serializable {

  String getResourceName();
}
