package com.joindata.inf.common.support.session.bootconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.joindata.inf.common.support.redis.support.RedisProperties;
import com.joindata.inf.common.util.network.NetworkUtil;
import com.joindata.inf.common.util.network.entity.HostPort;

/**
 * SpringSession 配置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 22, 2016 12:06:03 PM
 */
@Configuration
@EnableRedisHttpSession
public class SpringSessionConfig
{
    @Autowired
    private RedisProperties properties;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory()
    {
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();

        HostPort[] hostPorts = NetworkUtil.parseHostPort(properties.getHost());

        for(HostPort hostPort: hostPorts)
        {
            clusterConfig.addClusterNode(new RedisNode(hostPort.getHost(), hostPort.getPort()));
        }

        JedisConnectionFactory factory = new JedisConnectionFactory(clusterConfig);
        
        return factory;
    }
}
