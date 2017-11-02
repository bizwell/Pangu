package com.joindata.inf.common.support.mybatis.mapper;

 import com.joindata.inf.common.support.mybatis.bean.BasePojo;
 import tk.mybatis.mapper.common.Mapper;

/**
 * 单表操作 支持 删除,通用查询删除修改插入,mysql批量插入
 */
public interface BaseMapper<T extends BasePojo> extends Mapper<T>, CustomInsertListMapper<T> {

}
