package com.wangboot.model.dataauthority.datascope;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 简单数据权限范围
 *
 * @author wwtg99
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
public class SimpleDataScope implements IDataScopeModel {

  private String dataScopeName;
}
