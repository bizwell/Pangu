package com.joindata.inf.boot;

import java.lang.annotation.Annotation;

import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.joindata.inf.boot.annotation.JoindataApp;
import com.joindata.inf.boot.annotation.JoindataWebApp;
import com.joindata.inf.boot.bootconfig.WebMvcConfig;
import com.joindata.inf.boot.webserver.JettyServerFactory;
import com.joindata.inf.common.basic.annotation.BindConfigHub;
import com.joindata.inf.common.basic.annotation.WebConfig;
import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.basic.SystemUtil;

/**
 * 启动器提供者
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月2日 下午1:35:48
 */
public class Bootstrap
{
    /**
     * 启动应用，<strong>启动后容器将继续运行</strong><br />
     * <i>会在堆栈中自动寻找调用的启动类，放心地调用即可</i>
     * 
     * @param args 启动参数，实际上并没有什么软用，不要传
     */
    public static final ApplicationContext boot(String... args)
    {
        StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        for(StackTraceElement ste: stack)
        {
            if(!StringUtil.isEquals(ste.getClassName(), "java.lang.Thread") && !StringUtil.isEquals(ste.getClassName(), Bootstrap.class.getName()))
            {
                try
                {
                    Class<?> bootClz = Class.forName(ste.getClassName());

                    if(bootClz.getAnnotation(JoindataWebApp.class) != null)
                    {
                        configureBootInfo(bootClz);
                        checkEnv();
                        
                        return bootWeb(bootClz, bootClz.getAnnotation(JoindataWebApp.class).value());
                    }
                    else if(bootClz.getAnnotation(JoindataApp.class) != null)
                    {
                        configureBootInfo(bootClz);
                        checkEnv();
                       
                        return boot(bootClz);
                    }
                }
                catch(ClassNotFoundException e)
                {
                    e.printStackTrace();
                    throw new RuntimeException("错误: 很神奇地找不到启动类，(ーー゛)");
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    System.err.println("错误: 启动错误 >> 启动时发生意外错误：" + e.getMessage());
                    System.exit(0);
                }
            }
        }

        System.err.println("错误：缺少配置 >> 启动类中没有 @JoindataApp 或 @JoindataWebApp 注解，不知道你要启动什么样的应用 O__O \"…");
        System.exit(0);

        try
        {
            Thread.currentThread().join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
            System.err.println("错误：启动错误 >> 启动时发生意外错误：" + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    /**
     * 启动应用，并返回 Spring 的上下文
     * 
     * @param bootClz 启动类
     * @param args 没什么软用的参数，不要传
     * @return Spring 上下文
     */
    private static final ApplicationContext boot(Class<?> bootClz, String... args)
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(bootClz);
        context.scan(bootClz.getPackage().getName());
        context.refresh();
        context.start();
        
        System.err.println("应用已启动, PID: " + SystemUtil.getProcessId());

        return context;
    }

    /**
     * 启动 Web 应用，并返回 Spring 上下文
     * 
     * @param bootClz 启动类
     * @param args 没什么软用的参数，不要传
     * @return Spring 上下文
     * @throws Exception
     */
    private static final ApplicationContext bootWeb(Class<?> bootClz, int port, String... args) throws Exception
    {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);

        // 启动 Jetty Jetty
        {
            WebAppContext webAppContext = JettyServerFactory.makeAppContext(context.getClassLoader(), dispatcherServlet);
            JettyServerFactory.newServer(port, context, webAppContext).start();
        }

        // 注册公共 WebMvc 配置
        context.register(WebMvcConfig.class);

        // 注册启动类
        context.register(bootClz);

        // 注册支持组件的 Web 配置
        for(Class<?> configHubClz: BootInfoHolder.getConfigHubClasses())
        {
            if(configHubClz.getAnnotation(WebConfig.class) != null)
            {
                Class<?>[] webConfigClzes = configHubClz.getAnnotation(WebConfig.class).value();
                if(webConfigClzes == null)
                {
                    continue;
                }

                for(Class<?> webConfigClz: webConfigClzes)
                {
                    if(webConfigClz == null)
                    {
                        continue;
                    }

                    context.register(webConfigClz);
                }
            }
        }

        context.scan(bootClz.getPackage().getName());

        context.refresh();
        context.start();
        
        System.err.println("应用已启动, PID: " + SystemUtil.getProcessId());

        return context;
    }

    /**
     * 设置启动信息，以供其他组件使用
     */
    public static void configureBootInfo(Class<?> bootClz)
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
