package com.joindata.inf.common.basic.errors;

import com.joindata.inf.common.basic.entities.ErrorEntity;

/**
 * 资源访问错误代码
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月6日 上午11:04:39
 */
public interface ResourceErrors
{
    /** 通用资源错误 */
    public static final ErrorEntity RESOURCE_ERROR = ErrorEntity.define(2000, "资源错误");

    /** 资源不存在 */
    public static final ErrorEntity NOT_FOUND = ErrorEntity.define(2001, "资源不存在");

    /** 资源无法读取 */
    public static final ErrorEntity UNREADABLE = ErrorEntity.define(2002, "无法读取");

    /** 资源无法解析 */
    public static final ErrorEntity UNRESOLVABLE = ErrorEntity.define(2003, "无法解析");

    /** 资源被损坏 */
    public static final ErrorEntity BROKEN = ErrorEntity.define(2004, "资源已损坏");

    /** 资源无法写入到目的地 */
    public static final ErrorEntity CANNOT_WRITE = ErrorEntity.define(2005, "无法写入数据");

    /** 无法访问目的地 */
    public static final ErrorEntity CANNOT_ACCESS = ErrorEntity.define(2006, "无法访问目的地");
}
