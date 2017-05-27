package com.joindata.inf.common.util.basic;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * 对象相关工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 26, 2017 1:55:16 PM
 */
public class ObjectUtil
{
    /**
     * 将堆栈信息转成字符串
     */
    public static final String toString(Throwable t)
    {
        return ExceptionUtils.getStackTrace(t);
    }
}
