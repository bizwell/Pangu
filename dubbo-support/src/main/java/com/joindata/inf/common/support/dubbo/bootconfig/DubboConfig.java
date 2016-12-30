package com.joindata.inf.common.support.dubbo.bootconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.dubbo.properties.DubboProperties;
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
    // TODO 记得补充日志
    // private static final Logger log = Logger.get();

    @Autowired
    private DubboProperties properties;

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
        config.setCheck(true); // 必须检查注册中心是否存活

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
        config.setPort(NetworkUtil.nextUsableLocalPort(60000, 65000));
        config.setThreads(properties.getDubboThreads());
        config.setHost(NetworkUtil.getLocalIpv4(properties.getDubboHostPrefix()));
        config.setSerialization(properties.getDubboSerialization());

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