package com.joindata.inf.common.util.log;

import java.util.Map;

import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;

/**
 * 日志记录器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月9日 上午9:41:12
 */
public class Logger
{
    private static final Map<Class<?>, Logger> LogMap = CollectionUtil.newMap();

    private org.apache.logging.log4j.Logger logger;

    private Logger(Class<?> clz)
    {
        logger = org.apache.logging.log4j.LogManager.getLogger();
    }

    /**
     * 获取当前类的日志记录器<br />
     * <i>可以在类的任何位置调用本方法，当前类的 Logger 会被创建为单例对象，不会每调用一次创建一次。但还是建议将其定义在类的开头作为该类的公共常量使用，如：</i><br />
     * 
     * <pre>
     * private static final Logger log = Logger.get();
     * </pre>
     * 
     * @return 日志记录器
     */
    public static final Logger get()
    {
        Class<?> clz = ClassUtil.getCaller();
        if(!LogMap.containsKey(clz))
        {
            LogMap.put(clz, new Logger(clz));
        }

        return LogMap.get(clz);
    }

    /**
     * DEBUG 日志
     * 
     * @param message 消息内容
     */
    public void debug(String message)
    {
        logger.debug(message);
    }

    /**
     * DEBUG 日志
     * 
     * @param message 消息内容
     * @param params 日志消息参数，可用于替换日志内容中的占位符
     */
    public void debug(String message, Object... params)
    {
        logger.debug(message, params);
    }

    /**
     * DEBUG 日志
     * 
     * @param message 消息内容
     * @param throwable 堆栈
     */
    public void debug(String message, Throwable throwable)
    {
        logger.debug(message, throwable);
    }

    /**
     * INFO 日志
     * 
     * @param message 消息内容
     */
    public void info(String message)
    {
        logger.info(message);
    }

    /**
     * INFO 日志
     * 
     * @param message 消息内容
     * @param params 日志消息参数，可用于替换日志内容中的占位符
     */
    public void info(String message, Object... params)
    {
        logger.info(message, params);
    }

    /**
     * INFO 日志
     * 
     * @param message 消息内容
     * @param throwable 堆栈
     */
    public void info(String message, Throwable throwable)
    {
        logger.info(message, throwable);
    }

    /**
     * WARN 日志
     * 
     * @param message 消息内容
     */
    public void warn(String message)
    {
        logger.warn(message);
    }

    /**
     * WARN 日志
     * 
     * @param message 消息内容
     * @param params 日志消息参数，可用于替换日志内容中的占位符
     */
    public void warn(String message, Object... params)
    {
        logger.warn(message, params);
    }

    /**
     * WARN 日志
     * 
     * @param message 消息内容
     * @param throwable 堆栈
     */
    public void warn(String message, Throwable throwable)
    {
        logger.warn(message, throwable);
    }

    /**
     * ERROR 日志
     * 
     * @param message 消息内容
     */
    public void error(String message)
    {
        logger.error(message);
    }

    /**
     * ERROR 日志
     * 
     * @param message 消息内容
     * @param params 日志消息参数，可用于替换日志内容中的占位符
     */
    public void error(String message, Object... params)
    {
        logger.error(message, params);
    }

    /**
     * ERROR 日志
     * 
     * @param message 消息内容
     * @param throwable 堆栈
     */
    public void error(String message, Throwable throwable)
    {
        logger.error(message, throwable);
    }

    /**
     * FATAL 日志
     * 
     * @param message 消息内容
     */
    public void fatal(String message)
    {
        logger.fatal(message);
    }

    /**
     * FATAL 日志
     * 
     * @param message 消息内容
     * @param params 日志消息参数，可用于替换日志内容中的占位符
     */
    public void fatal(String message, Object... params)
    {
        logger.fatal(message, params);
    }

    /**
     * FATAL 日志
     * 
     * @param message 消息内容
     * @param throwable 堆栈
     */
    public void fatal(String message, Throwable throwable)
    {
        logger.fatal(message, throwable);
    }
}