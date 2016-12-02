package com.joindata.inf.common.basic.errors;

import com.joindata.inf.common.basic.entities.ErrorEntity;

/**
 * 系统错误代码
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015-11-27 16:05:16
 */
public interface SystemError
{
    /** 通用系统错误 */
    public static final ErrorEntity SYSTEM_ERROR = ErrorEntity.define(4000, "系统错误");

    /** 依赖程序组件未准备好 */
    public static final ErrorEntity DEPEND_COMPONENT_NOT_READY = ErrorEntity.define(4001, "依赖组件未准备好");

    /** 依赖资源未准备好 */
    public static final ErrorEntity DEPEND_RESOURCE_NOT_READY = ErrorEntity.define(4002, "依赖资源未准备好");

    /** 依赖资源无法就绪 */
    public static final ErrorEntity DEPEND_RESOURCE_CANNOT_READY = ErrorEntity.define(4003, "依赖资源无法就绪");
}
