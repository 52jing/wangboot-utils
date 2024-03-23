package com.wangboot.model.flex.controller;

/**
 * Restful 读写分离控制器接口
 *
 * @param <R> 读实体类型
 * @param <W> 写实体类型
 */
public interface IRestfulReadWriteController<R, W>
    extends IRestfulReadController<R>, IRestfulWriteController<W> {}
