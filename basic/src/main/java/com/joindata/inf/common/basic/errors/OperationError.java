package com.joindata.inf.common.basic.errors;

import com.joindata.inf.common.basic.entities.ErrorEntity;

/**
 * 操作错误代码
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015-12-15 15:00:49
 */
public interface OperationError
{
    /** 通用操作错误 */
    public static final ErrorEntity OPERATION_ERROR = ErrorEntity.define(8000, "操作不正确");

    /** 重复操作 */
    public static final ErrorEntity REPEAT_OPERATE_ERROR = ErrorEntity.define(8001, "重复操作");

}
