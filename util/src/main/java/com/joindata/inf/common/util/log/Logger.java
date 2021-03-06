package com.joindata.inf.common.util.log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.joindata.inf.common.basic.support.BootInfoHolder.Env;
import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.ResourceUtil;

/**
 * 日志记录器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月9日 上午9:41:12
 */
public class Logger
{
    /** 存储 Logger 的 Map，保证 Logger 不会被创建多个实例 */
    private static final Map<Class<?>, Logger> LogMap = CollectionUtil.newMap();

    /** Log4j 的日志记录器，最终调用的是这个 */
    private org.apache.logging.log4j.Logger logger;

    /** 禁止使用 new 来创建 */
    private Logger(Class<?> clz)
    {
        logger = org.apache.logging.log4j.LogManager.getLogger(clz);
    }

    static
    {
        String env = "." + Env.get();

        String confFiles[] = {"log4j2" + env + ".xml", "log" + env + ".xml", "log4j2.xml", "log.xml"};

        try
        {
            String confFile = ResourceUtil.getFirstExistResource(confFiles);

            if(confFile == null)
            {
                throw new FileNotFoundException();
            }

            Configurator.initialize(null, new ConfigurationSource(ResourceUtil.getRootResourceAsStream(confFile)));

        }
        catch(IOException e)
        {
            System.err.println("没有配置 LOG4J2 的配置文件在工程目录下，将无法正常打印日志");
            System.out.println("可以配置 " + ArrayUtil.join(confFiles) + " 中的任何一个文件在工程编译根目录下");
        }
    }

    /**
     * 获取当前类的日志记录器<br />
     * <i>可以在类的任何位置调用本方法，当前类的 Logger 会被创建为单例对象，不会每调用一次创建一次。但还是建议将其定义在类的开头作为该类的公共常量使用，如：</i>
     * 
     * <pre>
     * private static final Logger log = Logger.get();
     * </pre>
     * 
     * @return 日志记录器
     */
    public synchronized static final Logger get()
    {
        Class<?> clz = ClassUtil.getCaller();
        if(!LogMap.containsKey(clz))
        {
            LogMap.put(clz, new Logger(clz));
        }

        return LogMap.get(clz);
    }

    /**
     * 获取指定类的日志记录器
     * 
     * @return 日志记录器
     */
    public synchronized static final Logger newLogger(Class<?> clz)
    {
        if(!LogMap.containsKey(clz))
        {
            LogMap.put(clz, new Logger(clz));
        }

        return LogMap.get(clz);
    }

    /**
     * trace 日志
     * 
     * @param message 消息内容
     */
    public void trace(String message)
    {
        logger.trace(message);
    }

    /**
     * trace 日志
     * 
     * @param message 消息内容
     * @param params 日志消息参数，可用于替换日志内容中的占位符
     */
    public void trace(String message, Object... params)
    {
        logger.trace(message, params);
    }

    /**
     * trace 日志
     * 
     * @param message 消息内容
     * @param throwable 堆栈
     */
    public void trace(String message, Throwable throwable)
    {
        logger.trace(message, throwable);
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

    /**
     * 取到 log4j2 的 Logger，一般不用
     * 
     * @return Log4j2 的 Logger
     */
    public org.apache.logging.log4j.Logger sourceLogger()
    {
        return this.logger;
    }
}