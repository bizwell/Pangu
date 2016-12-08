package com.joindata.inf.boot.mechanism;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.eclipse.jetty.util.log.AbstractLogger;
import org.eclipse.jetty.util.log.Logger;

/**
 * 将 Jetty 的日志转换到 Log4j2 中
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月8日 下午4:16:00
 */
public class Jetty2Log4j2Bridge extends AbstractLogger
{
    private org.apache.logging.log4j.Logger logger;

    private String name;

    public Jetty2Log4j2Bridge(String name)
    {
        this.name = name;
        logger = LogManager.getLogger(name);
    }

    @Override
    protected Logger newLogger(String fullname)
    {
        return new Jetty2Log4j2Bridge(fullname);
    }

    public String getName()
    {
        return name;
    }

    public void warn(String msg, Object... args)
    {
        logger.warn(msg, args);
    }

    public void warn(Throwable thrown)
    {
        logger.catching(Level.WARN, thrown);
    }

    public void warn(String msg, Throwable thrown)
    {
        logger.warn(msg, thrown);
    }

    public void info(String msg, Object... args)
    {
        logger.info(msg, args);
    }

    public void info(Throwable thrown)
    {
        logger.catching(Level.INFO, thrown);
    }

    public void info(String msg, Throwable thrown)
    {
        logger.info(msg, thrown);
    }

    public boolean isDebugEnabled()
    {
        return logger.isDebugEnabled();
    }

    public void setDebugEnabled(boolean enabled)
    {
        warn("setDebugEnabled not implemented", null, null);
    }

    public void debug(String msg, Object... args)
    {
        logger.debug(msg, args);
    }

    public void debug(Throwable thrown)
    {
        logger.catching(Level.DEBUG, thrown);
    }

    public void debug(String msg, Throwable thrown)
    {
        logger.debug(msg, thrown);
    }

    public void ignore(Throwable ignored)
    {
        logger.catching(Level.TRACE, ignored);
    }
}