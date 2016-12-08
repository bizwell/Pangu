package com.joindata.inf.common.util.support;

import org.apache.logging.log4j.Logger;

import com.joindata.inf.common.util.basic.ClassUtil;

/**
 * 日志管理器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月8日 下午5:03:25
 */
public class LogManager
{
    /**
     * 创建日志打印器<br />
     * <i>无需传入 Class，程序会自动从堆栈中取出调用的类</i>
     * 
     * @return Log4J 的 Logger
     */
    public static final Logger getLogger()
    {
        return org.apache.logging.log4j.LogManager.getLogger(ClassUtil.getCaller());
    }
}
