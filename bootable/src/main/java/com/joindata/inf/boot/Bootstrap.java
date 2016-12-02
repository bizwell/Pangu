package com.joindata.inf.boot;

import java.lang.annotation.Annotation;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.joindata.inf.common.basic.annotation.BindConfigHub;
import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.util.basic.StringUtil;

/**
 * 启动器提供者
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月2日 下午1:35:48
 */
public class Bootstrap
{
    /**
     * 启动应用<br />
     * <i>会在堆栈中自动寻找调用的启动类，放心地调用即可</i>
     * 
     * @param args 启动参数，实际上并没有什么软用，不要传
     */
    public static final void boot(String... args)
    {
        StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        for(StackTraceElement ste: stack)
        {
            if(!StringUtil.isEquals(ste.getClassName(), "java.lang.Thread") && !StringUtil.isEquals(ste.getClassName(), Bootstrap.class.getName()))
            {
                try
                {
                    boot(Class.forName(ste.getClassName()));
                }
                catch(ClassNotFoundException e)
                {
                    e.printStackTrace();
                    throw new RuntimeException("很神奇地找不到启动类，(ーー゛)");
                }
            }
        }
    }

    /**
     * 启动应用，并返回 Spring 的上下文
     * 
     * @param bootClz 启动类
     * @param args 没什么软用的参数，不要传
     * @return Spring 上下文
     */
    public static final ApplicationContext boot(Class<?> bootClz, String... args)
    {
        configureBootInfo(bootClz);
        checkEnv();

        ApplicationContext context = SpringApplication.run(bootClz);
        return context;
    }

    /**
     * 设置启动信息，以供其他组件使用
     */
    private static void configureBootInfo(Class<?> bootClz)
    {
        // 告诉大家启动类是哪个
        BootInfoHolder.setBootClass(bootClz);

        Annotation[] annos = bootClz.getAnnotations();
        for(Annotation anno: annos)
        {
            BindConfigHub bindAnno = anno.annotationType().getAnnotation(BindConfigHub.class);
            if(bindAnno == null)
            {
                continue;
            }

            BootInfoHolder.addConfigHub(bindAnno.value());
        }
    }

    /**
     * 检查环境是否干净整洁
     */
    private static void checkEnv()
    {
        for(AbstractConfigHub configHub: BootInfoHolder.getConfigHubs())
        {
            configHub.executeCheck();
        }
    }
}
