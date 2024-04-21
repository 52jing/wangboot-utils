package com.wangboot.model.entity.controller;

import com.wangboot.model.entity.IdEntity;
import java.io.Serializable;

/**
 * Restful 读写分离控制器接口
 *
 * @param <I> 主键类型
 * @param <R> 读实体类型
 * @param <W> 写实体类型
 */
public interface IRestfulReadWriteController<
        I extends Serializable, R extends IdEntity<I>, W extends IdEntity<I>>
    extends IRestfulReadController<I, R>, IRestfulWriteController<I, W> {}
