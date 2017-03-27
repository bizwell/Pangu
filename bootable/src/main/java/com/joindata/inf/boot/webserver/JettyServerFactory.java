package com.joindata.inf.boot.webserver;

import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import com.joindata.inf.common.util.log.Logger;

/**
 * Jetty 配置
 * 
 * @author 宋翔
 * @date 2015年11月24日 下午5:37:43
 */
public class JettyServerFactory
{
    private static final Logger log = Logger.get();

    /**
     * 生成 WebAppContext 动态资源处理器
     * 
     * @param classLoader 生效的 ClassLoader
     * @param servlet SpringMVC 的 DispatcherServlet
     * @return
     */
    public static WebAppContext makeAppContext(ClassLoader classLoader, DispatcherServlet servlet)
    {
        log.info("配置 AppContext - 开始");

        // 基本信息
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setClassLoader(classLoader);
        webAppContext.setParentLoaderPriority(true);
        webAppContext.setResourceBase("");
        webAppContext.setContextPath("/");

        log.info("配置: WebAppContext = {}, ResourceBase = {}", webAppContext.getResourceBase(), webAppContext.getContextPath());

        // SpringMVC 转发器
        ServletHolder holder = new ServletHolder(servlet);
        holder.setName("dispatcher-servlet");
        webAppContext.addServlet(holder, "/");

        log.info("注册 Servlet: {} -> {}", "/", servlet.getClass());

        // 字符编码过滤器
        {
            CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
            encodingFilter.setEncoding("UTF-8");
            encodingFilter.setForceEncoding(true);

            FilterHolder encodingFilterHolder = new FilterHolder(encodingFilter);
            webAppContext.addFilter(encodingFilterHolder, "/*", EnumSet.allOf(DispatcherType.class));

            log.info("注册字符编码过滤器: {}, encoding = {}, InitParameters = {}", encodingFilter.getClass().getName(), encodingFilter.getEncoding(), encodingFilterHolder.getInitParameters());
        }

        // 隐藏方法过滤器，如果客户端不支持 PUT/DELETE 等方法，将会用到这个
        {
            HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
            hiddenHttpMethodFilter.setServletContext(servlet.getServletContext());
            FilterHolder hiddenHttpMethodFilterHolder = new FilterHolder(hiddenHttpMethodFilter);
            webAppContext.addFilter(hiddenHttpMethodFilterHolder, "/*", EnumSet.allOf(DispatcherType.class));

            log.info("注册隐藏方法过滤器: {}, servlet = {}, InitParameters = {}", hiddenHttpMethodFilter.getClass().getName(), servlet.getClass().getName(), hiddenHttpMethodFilterHolder.getInitParameters());
        }

        log.info("配置 AppContext - 完成");
        return webAppContext;
    }

    /**
     * 生成静态资源处理器
     * 
     * @param resourceDir 静态资源文件夹
     * @param enableListDir 是否开启目录列表（会允许 Web 访问列出目录）
     * @param minCacheLength 内存最小缓存数
     * @param cacheControl 缓存的控制语句，如果为 null，不缓存
     * @return 静态资源处理器
     */
    public static ResourceHandler makeResourceHandler(String resourceDir, boolean enableListDir, int minCacheLength, String cacheControl)
    {
        log.info("配置 ResourceHandler - 开始");

        // 有静态资源才处理，否则不处理
        if(resourceDir == null)
        {
            return null;
        }

        ResourceHandler handler = new ResourceHandler();

        handler.setMinMemoryMappedContentLength(minCacheLength);
        handler.setDirectoriesListed(enableListDir);
        handler.setResourceBase(resourceDir);
        
        log.info("添加静态目录: {}, {}, 内存最小缓存数: {}, {}", resourceDir, enableListDir ? "显示目录" : "不显示目录", minCacheLength, cacheControl == null ? "不缓存" : "缓存控制指令: " + cacheControl);

        if(cacheControl == null)
        {
            handler.setCacheControl("no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
        }

        log.info("配置 ResourceHandler - 完成");

        return handler;
    }

    /**
     * 配置 HTTP 服务器
     */
    public static Server newServer(int port, ApplicationContext context, Handler... handlers)
    {
        log.info("配置 Server - 开始");
        Server server = new Server(port);

        // 这里用 HandlerList，可以使 handler 短路，有 handler 处理过就不再处理，而不至于所有去处理所有 handler
        HandlerList handlerList = new HandlerList();

        for(Handler handler: handlers)
        {
            handlerList.addHandler(handler);
            log.info("注册 Handler: {}", handler.getClass());
        }
        server.setHandler(handlerList);

        log.info("配置 Server - 完成");

        return server;
    }

    /**
     * 配置 HTTP 服务器
     */
    public static Server newServer(int port, ApplicationContext context, List<Handler> handlers)
    {
        log.info("配置 Server - 开始");
        Server server = new Server(port);
        
        // 这里用 HandlerList，可以使 handler 短路，有 handler 处理过就不再处理，而不至于所有去处理所有 handler
        HandlerList handlerList = new HandlerList();

        for(Handler handler: handlers)
        {
            handlerList.addHandler(handler);
            log.info("注册 Handler: {}", handler.getClass());
        }

        server.setHandler(handlerList);

        log.info("配置 Server - 完成");

        return server;
    }
}
