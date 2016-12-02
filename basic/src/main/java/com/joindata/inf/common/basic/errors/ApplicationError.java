package com.joindata.inf.common.basic.errors;

import com.joindata.inf.common.basic.entities.ErrorEntity;

/**
 * 程序错误代码
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015-11-11 14:14:03
 */
public interface ApplicationError
{
    /** 通用程序错误 */
    public static final ErrorEntity APPLICATION_ERROR = ErrorEntity.define(3000, "程序错误");
}
