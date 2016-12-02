package com.joindata.inf.common.basic.errors;

import com.joindata.inf.common.basic.entities.ErrorEntity;

/**
 * 通用错误
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月17日 下午4:17:13
 */
public interface GenericError
{
    /** 通用错误 */
    public static final ErrorEntity ERROR = ErrorEntity.define(9999, "错误");
}
