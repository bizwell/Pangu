package com.joindata.inf.common.support.elasticjob.bootconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.joindata.inf.common.support.elasticjob.component.ElasticJobInitializer;
import com.joindata.inf.common.support.elasticjob.properties.ElasticJobProperties;
import com.joindata.inf.common.util.log.Logger;

/**
 * ElasticJob 客户端配置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 9, 2017 1:03:18 PM
 */
@Configuration
public class ElasticJobConfig
{
    private static final Logger log = Logger.get();

    private static final String NAMESPACE = "elasticjob";

    @Autowired
    private ElasticJobProperties properties;

    @Bean
    public CoordinatorRegistryCenter coordinatorRegistryCenter()
    {
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(properties.getZkHosts(), NAMESPACE);
        CoordinatorRegistryCenter center = new ZookeeperRegistryCenter(zkConfig);
        center.init();

        log.info("ElasticJob 使用 Zookeeper 注册中心: {}, 命名空间: {}" + properties.getZkHosts(), NAMESPACE);
        return center;
    }

    @Bean(initMethod = "init")
    public ElasticJobInitializer elasticJobInitializer()
    {
        ElasticJobInitializer initializer = new ElasticJobInitializer(coordinatorRegistryCenter());
        return initializer;
    }
}