package com.joindata.inf.common.support.dubbo.adapter.log;

import com.alibaba.dubbo.common.logger.Logger;

/**
 * Dubbo 日志适配器的日志转换器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 29, 2016 3:05:42 PM
 */
public class Log4j2Logger implements Logger
{
    private final org.apache.logging.log4j.Logger logger;

    public Log4j2Logger(org.apache.logging.log4j.Logger logger)
    {
        this.logger = logger;
    }

    public void trace(String msg)
    {
        logger.trace(msg);
    }

    public void trace(Throwable e)
    {
        logger.trace(e);
    }

    public void trace(String msg, Throwable e)
    {
        logger.trace(msg, e);
    }

    public void debug(String msg)
    {
        logger.debug(msg);
    }

    public void debug(Throwable e)
    {
        logger.debug(e);
    }

    public void debug(String msg, Throwable e)
    {
        logger.debug(msg, e);
    }

    public void info(String msg)
    {
        logger.info(msg);
    }

    public void info(Throwable e)
    {
        logger.info(e);
    }

    public void info(String msg, Throwable e)
    {
        logger.info(msg, e);
    }

    public void warn(String msg)
    {
        logger.warn(msg);
    }

    public void warn(Throwable e)
    {
        logger.warn(e);
    }

    public void warn(String msg, Throwable e)
    {
        logger.warn(msg, e);
    }

    public void error(String msg)
    {
        logger.error(msg);
    }

    public void error(Throwable e)
    {
        logger.error(e);
    }

    public void error(String msg, Throwable e)
    {
        logger.error(msg, e);
    }

    public boolean isTraceEnabled()
    {
        return logger.isTraceEnabled();
    }

    public boolean isDebugEnabled()
    {
        return logger.isDebugEnabled();
    }

    public boolean isInfoEnabled()
    {
        return logger.isInfoEnabled();
    }

    public boolean isWarnEnabled()
    {
        return logger.isWarnEnabled();
    }

    public boolean isErrorEnabled()
    {
        return logger.isErrorEnabled();
    }

}
