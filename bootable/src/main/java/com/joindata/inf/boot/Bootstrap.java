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
import com.joindata.inf.boot.mechanism.JoindataAnnotationBeanNameGenerator;
import com.joindata.inf.boot.webserver.JettyServerFactory;
import com.joindata.inf.common.basic.annotation.JoindataComponent;
import com.joindata.inf.common.basic.annotation.WebConfig;
import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.SystemUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * 启动器提供者
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月2日 下午1:35:48
 */
public class Bootstrap
{
    private static final Logger log = Logger.get();

    /**
     * 启动应用，<strong>启动后容器将继续运行</strong><br />
     * <i>会在堆栈中自动寻找调用的启动类，放心地调用即可</i>
     * 
     * @param args 启动参数，实际上并没有什么软用，不要传
     */
    public static final ApplicationContext boot(String... args)
    {
        Class<?> bootClz = ClassUtil.getCaller();

        try
        {
            // 如果是 Web 应用，启动 Web
            if(bootClz.getAnnotation(JoindataWebApp.class) != null)
            {
                configureBootInfo(bootClz);
                checkEnv();

                return bootWeb(bootClz, bootClz.getAnnotation(JoindataWebApp.class).value());
            }
            // 启动应用
            else if(bootClz.getAnnotation(JoindataApp.class) != null)
            {
                configureBootInfo(bootClz);
                checkEnv();

                return boot(bootClz);
            }
            // 没有标注，就报错
            else
            {
                log.fatal("缺少配置 >> 启动类中没有 @JoindataApp 或 @JoindataWebApp 注解，不知道你要启动什么样的应用 O__O \"…");
                System.exit(0);
            }
        }
        catch(Exception e)
        {
            log.fatal("错误: 启动错误 >> 启动时发生意外错误: {}" + e.getMessage(), e);
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
        context.setBeanNameGenerator(new JoindataAnnotationBeanNameGenerator());

        // 扫描 ConfigHub 的包
        for(Class<?> configHubClz: BootInfoHolder.getConfigHubClasses())
        {
            context.scan(configHubClz.getPackage().getName());
        }

        // 注册启动类，这样就可以在启动类中使用其他 Spring 注解
        context.register(bootClz);
        // 扫描启动类的包
        context.scan(bootClz.getPackage().getName());

        context.refresh();
        context.start();

        log.info("应用已启动, PID: " + SystemUtil.getProcessId());

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
        context.setBeanNameGenerator(new JoindataAnnotationBeanNameGenerator());

        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);

        // 启动 Jetty Jetty
        {
            WebAppContext webAppContext = JettyServerFactory.makeAppContext(context.getClassLoader(), dispatcherServlet);
            JettyServerFactory.newServer(port, context, webAppContext).start();
        }

        for(Class<?> configHubClz: BootInfoHolder.getConfigHubClasses())
        {
            // 注册支持组件的 Web 配置
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

            // 扫描 ConfigHub 包中的组件
            context.scan(configHubClz.getPackage().getName());
        }

        context.scan(bootClz.getPackage().getName());

        // 注册公共 WebMvc 配置
        context.register(WebMvcConfig.class);

        // 注册启动类
        context.register(bootClz);

        context.refresh();
        context.start();

        log.info("Web 应用已启动, PID: " + SystemUtil.getProcessId() + ", Port: " + port);

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
            JoindataComponent jc = anno.annotationType().getAnnotation(JoindataComponent.class);
            if(jc == null)
            {
                continue;
            }

            BootInfoHolder.addConfigHub(jc.value());
        }
    }

    /**
     * 检查环境是否干净整洁
     * 
     * @throws Exception 发生任何错误，抛出该异常
     */
    private static void checkEnv() throws Exception
    {
        for(AbstractConfigHub configHub: BootInfoHolder.getConfigHubs())
        {
            configHub.executeCheck();
        }
    }
}
