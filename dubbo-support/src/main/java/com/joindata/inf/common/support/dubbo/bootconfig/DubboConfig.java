package com.joindata.inf.common.support.dubbo.bootconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.dubbo.adapter.log.Log4j2LoggerAdapter;
import com.joindata.inf.common.support.dubbo.cst.DubboCst;
import com.joindata.inf.common.support.dubbo.filter.LogTraceFilter;
import com.joindata.inf.common.support.dubbo.properties.DubboProperties;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.common.util.network.NetworkUtil;

/**
 * Dubbox 配置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 28, 2016 5:05:47 PM
 */
@Configuration
public class DubboConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private DubboProperties properties;

    static
    {
        System.setProperty("dubbo.application.logger", "slf4j");
        LoggerFactory.setLoggerAdapter(new Log4j2LoggerAdapter());
    }

    /**
     * 启用注解扫描
     */
    @Bean(destroyMethod = "destroy")
    @Scope("singleton")
    @Lazy
    public static AnnotationBean annotationBean()
    {
        AnnotationBean bean = new AnnotationBean();
        bean.setPackage(BootInfoHolder.getAppPackage());

        log.info("Dubbo 扫描包: {}", bean.getPackage());

        return bean;
    }

    /**
     * 应用配置
     */
    @Bean
    @Scope("singleton")
    public ApplicationConfig applicationConfig()
    {
        ApplicationConfig config = new ApplicationConfig();
        config.setName(BootInfoHolder.getAppId());
        config.setOwner(properties.getAppOwner());
        config.setOrganization(properties.getAppOrg());
        config.setLogger("log4j2");

        log.info("Dubbo 应用 ID: {}", config.getName());
        log.info("Dubbo 服务维护人: {}", config.getOwner());
        log.info("Dubbo 服务组织: {}", config.getOrganization());
        return config;
    }

    /**
     * 注册中心配置
     */
    @Bean
    @Scope("singleton")
    public RegistryConfig registryConfig()
    {
        RegistryConfig config = new RegistryConfig();
        config.setAddress(properties.getRegistryAddress());
        config.setTimeout(properties.getRegistryTimeout());
        config.setFile(DubboCst.CACHE_FILE);
        config.setCheck(true); // 必须检查注册中心是否存活

        log.info("Dubbo 注册中心: {}", config.getAddress());
        log.info("Dubbo 注册中心连接超时时间: {}", config.getTimeout());
        log.info("Dubbo 服务缓存文件: {}", config.getFile());
        log.info("Dubbo 启动时检测注册中心: {}", config.isCheck());

        return config;
    }

    /**
     * 提供者配置
     */
    @Bean
    @Scope("singleton")
    public ProviderConfig providerConfig()
    {
        ProviderConfig config = new ProviderConfig();

        config.setProtocol(protocolConfig());
        config.setTimeout(properties.getProviderTimeout());
        config.setRetries(properties.getProviderRetries());
        config.setLoadbalance(properties.getProviderLoadbalance());
        config.setHost(NetworkUtil.getLocalIpv4(properties.getDubboHostPrefix()));
        String filter = properties.getProviderFilter();
        if(!StringUtils.isEmpty(properties.getProviderFilter()))
        {
            filter = StringUtils.isEmpty(properties.getConsumerFilter()) ? LogTraceFilter.FILTER_NAME : filter + "," + LogTraceFilter.FILTER_NAME;
        }
        config.setFilter(filter);
        config.setDelay(-1);

        log.info("Dubbo 服务默认超时时间: {}", config.getTimeout());
        log.info("Dubbo 服务默认重试次数: {}", config.getRetries());
        log.info("Dubbo 服务负载均衡方式: {}", config.getLoadbalance());
        log.info("Dubbo 服务拦截扩展: {}", config.getFilter());
        log.info("Dubbo 服务地址: {}", config.getHost());
        log.info("Dubbo 延迟暴露时间: {}", config.getDelay() > -1 ? config.getDelay() : "容器启动后");

        return config;
    }

    /**
     * Dubbo 协议配置
     */
    @Bean
    @Scope("singleton")
    public ProtocolConfig protocolConfig()
    {
        ProtocolConfig config = new ProtocolConfig();
        config.setName("dubbo");
        config.setPort(-1);
        config.setThreads(properties.getDubboThreads());
        config.setHost(NetworkUtil.getLocalIpv4(properties.getDubboHostPrefix()));
        config.setSerialization(properties.getDubboSerialization());

        log.info("Dubbo 服务协议名: {}", config.getName());
        log.info("Dubbo 服务协议端口号: {}", config.getPort() == -1 ? "随机" : config.getPort());
        log.info("Dubbo 服务线程数: {}", config.getThreads());
        log.info("Dubbo 服务序列化方式: {}", config.getSerialization());
        return config;
    }

    /**
     * 消费者配置
     */
    @Bean
    @Scope("singleton")
    public ConsumerConfig consumerConfig()
    {
        ConsumerConfig config = new ConsumerConfig();
        config.setTimeout(properties.getConsumerTimeout());
        config.setRetries(properties.getConsumerRetries());
        config.setLoadbalance(properties.getConsumerLoadbalance());
        config.setCheck(properties.isConsumerCheck());
        String filter = properties.getConsumerFilter();
        if(!StringUtils.isEmpty(properties.getConsumerFilter()))
        {
            filter = StringUtils.isEmpty(properties.getConsumerFilter()) ? LogTraceFilter.FILTER_NAME : filter + "," + LogTraceFilter.FILTER_NAME;
        }
        config.setFilter(filter);

        log.info("Dubbo 调用默认超时时间: {}", config.getTimeout());
        log.info("Dubbo 调用默认重试次数: {}", config.getRetries());
        log.info("Dubbo 调用负载均衡方式: {}", config.getLoadbalance());
        log.info("Dubbo 调用默认是否开启服务检测: {}", config.isCheck());
        log.info("Dubbo 调用拦截扩展: {}", config.getFilter());

        return config;
    }

    /**
     * 监控中心配置
     */
    @Bean
    @Scope("singleton")
    public MonitorConfig monitorConfig()
    {
        MonitorConfig config = new MonitorConfig();
        config.setProtocol("registry");
        return config;
    }
}