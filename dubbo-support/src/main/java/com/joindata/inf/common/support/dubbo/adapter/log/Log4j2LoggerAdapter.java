package com.joindata.inf.common.support.dubbo.adapter.log;

import java.io.File;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.appender.FileAppender;

import com.alibaba.dubbo.common.logger.Level;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerAdapter;
import com.joindata.inf.common.util.basic.CollectionUtil;

/**
 * Log4j2 的日志适配器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 29, 2016 2:45:56 PM
 */
public class Log4j2LoggerAdapter implements LoggerAdapter
{
    private File file;

    public Log4j2LoggerAdapter()
    {
        org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger)LogManager.getRootLogger();
        if(logger != null)
        {
            Set<String> appenders = logger.getAppenders().keySet();
            if(CollectionUtil.isNullOrEmpty(appenders))
            {
                for(String item: appenders)
                {
                    Appender appender = logger.getAppenders().get(item);
                    if(appender instanceof FileAppender)
                    {
                        file = new File(((FileAppender)appender).getFileName());
                    }
                }
            }
        }

    }

    @Override
    public Logger getLogger(Class<?> key)
    {
        return new Log4j2Logger(LogManager.getLogger(key));
    }

    @Override
    public Logger getLogger(String key)
    {
        return new Log4j2Logger(LogManager.getLogger(key));
    }

    @Override
    public void setLevel(Level level)
    {
        org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger)LogManager.getRootLogger();
        logger.setLevel(org.apache.logging.log4j.Level.valueOf(level.name()));
    }

    @Override
    public Level getLevel()
    {
        return Level.valueOf(LogManager.getRootLogger().getLevel().name());
    }

    @Override
    public File getFile()
    {
        return file;
    }

    @Override
    public void setFile(File file)
    {
    }

}