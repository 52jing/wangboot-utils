package com.wangboot.model.entity.utils;

import com.wangboot.model.entity.IRestfulService;
import com.wangboot.model.entity.IdEntity;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * 数据同步搬运工
 *
 * @param <I> 主键类型
 * @param <T> 实体类型
 * @author wwtg99
 */
@AllArgsConstructor
public class DataPorter<I extends Serializable, T extends IdEntity<I>> {

  private final Iterable<T> dataGenerator;
  private final IRestfulService<I, T> service;
  private final CompareTwo<T> compareFunc;

  public DataPorter(Iterable<T> dataGenerator, IRestfulService<I, T> service) {
    this(dataGenerator, service, null);
  }

  public int syncData() {
    int n = 0;
    for (T data : this.dataGenerator) {
      T find = this.service.getDataById(data.getId());
      if (Objects.isNull(find)) {
        // 不存在
        boolean ret = this.service.saveData(data);
        n += 1;
        if (!ret) {
          throw new RuntimeException("保存数据失败！" + data.getId());
        }
      } else {
        // 已存在
        if (this.isDifferent(find, data)) {
          // 不相同
          boolean ret = this.service.updateData(data);
          n += 1;
          if (!ret) {
            throw new RuntimeException("更新数据失败！" + data.getId());
          }
        }
      }
    }
    return n;
  }

  private boolean isDifferent(@NonNull T one, @NonNull T another) {
    if (Objects.nonNull(this.compareFunc)) {
      return !this.compareFunc.compare(one, another);
    }
    return !one.equals(another);
  }

  @FunctionalInterface
  interface CompareTwo<T> {
    boolean compare(T one, T another);
  }
}
