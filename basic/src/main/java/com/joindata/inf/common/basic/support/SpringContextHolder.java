package com.joindata.inf.common.basic.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring 上下文工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 9, 2017 6:38:49 PM
 */
@Component
public class SpringContextHolder implements ApplicationContextAware
{
    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        context = applicationContext;
    }

    /**
     * 获取当前应用上下文对象
     * 
     * @return 上下文对象
     */
    public static ApplicationContext getContext()
    {
        return context;
    }

    /**
     * 获取当前应用上下文中的 bean
     * 
     * @param clz bean 的 class
     * @return Bean 实例
     */
    public static <T> T getBean(Class<T> clz)
    {
        return context.getBean(clz);
    }

    /**
     * 通过名字获取应用上下文中的 Bean
     * 
     * @param name Bean 名称
     * @return Bean 实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name)
    {
        return (T)context.getBean(name);
    }
}