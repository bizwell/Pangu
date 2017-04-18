package com.joindata.inf.common.support.redis.bootconfig;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.joindata.inf.common.support.redis.component.RedisClient;
import com.joindata.inf.common.support.redis.component.impl.ClusterRedisClient;
import com.joindata.inf.common.support.redis.component.impl.SingleRedisPoolClient;
import com.joindata.inf.common.support.redis.support.RedisProperties;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.common.util.network.NetworkUtil;
import com.joindata.inf.common.util.network.entity.HostPort;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private RedisProperties properties;

    @Bean
    public JedisPoolConfig jedisConnectionPoolConfig()
    {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(properties.getMaxActive());
        config.setMaxIdle(properties.getMaxIdle());
        config.setMaxWaitMillis(properties.getMaxWait());
        config.setTestOnBorrow(properties.isTestOnBorrow());
        config.setTestOnReturn(properties.isTestOnReturn());
        config.setJmxEnabled(true);
        config.setJmxNameBase("Jedis 连接池");
        config.setJmxNamePrefix("Pangu");

        log.info("Redis 连接池最大活动连接数: {}, 最大空闲数: {}, 连接超时时间: {}, 获取连接时测试: {}", properties.getHost(), properties.getMaxActive(), properties.getMaxIdle(), properties.getTimeout(), properties.isTestOnBorrow());

        return config;
    }

    /**
     * 自己提供的客户端
     * 
     * @return Redis 客户端
     */
    @Bean
    public RedisClient redisClient()
    {
        log.info("创建 RedisClient");
        log.info("Redis 连接地址: {}", properties.getHost());

        HostPort[] hostPorts = NetworkUtil.parseHostPort(properties.getHost());

        RedisClient redisClient = null;
        if(hostPorts.length > 1)
        {
            Set<HostAndPort> hostSet = CollectionUtil.newHashSet();
            for(HostPort host: hostPorts)
            {
                HostAndPort hostAndPort = new HostAndPort(host.getHost(), host.getPort());
                hostSet.add(hostAndPort);
            }

            JedisCluster jedisCluster = null;
            if(!StringUtil.isNullOrEmpty(properties.getPassword()))
            {
                jedisCluster = new JedisCluster(hostSet, properties.getTimeout(), 2000, 5, properties.getPassword(), jedisConnectionPoolConfig());
                log.info("Redis 设置了密码，将启用认证");
            }
            else
            {
                jedisCluster = new JedisCluster(hostSet, jedisConnectionPoolConfig());
            }

            redisClient = new ClusterRedisClient(jedisCluster);
            log.info("用了 JedisCluster, 以集群方式连接 Redis");
        }
        else
        {
            JedisPool jedisPool = null;
            if(!StringUtil.isNullOrEmpty(properties.getPassword()))
            {
                log.info("Redis 设置了密码，将启用认证");
                jedisPool = new JedisPool(jedisConnectionPoolConfig(), hostPorts[0].getHost(), hostPorts[0].getPort(), properties.getTimeout(), properties.getPassword());
            }
            else
            {
                jedisPool = new JedisPool(jedisConnectionPoolConfig(), hostPorts[0].getHost(), hostPorts[0].getPort());
            }

            log.info("以单实例 Jedis 连接池方式连接 Redis");

            redisClient = new SingleRedisPoolClient(jedisPool);
        }

        return redisClient;
    }

    /**
     * Spring 的连接池工厂
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory()
    {
        log.info("创建 Spring 的 JedisConnectionFactory");
        log.info("Redis 连接地址: {}", properties.getHost());

        JedisConnectionFactory factory = null;

        HostPort[] hostPorts = NetworkUtil.parseHostPort(properties.getHost());

        if(hostPorts.length > 1)
        {
            Set<String> hostSet = CollectionUtil.newHashSet();
            for(HostPort host: hostPorts)
            {
                hostSet.add(host.getHost() + ":" + host.getPort());
            }

            factory = new JedisConnectionFactory(new RedisClusterConfiguration(hostSet), jedisConnectionPoolConfig());
            log.info("用了 JedisCluster, 以集群方式连接 Redis");
        }
        else
        {
            factory = new JedisConnectionFactory(jedisConnectionPoolConfig());
            log.info("以单实例 Jedis 连接池方式连接 Redis");
        }

        if(!StringUtil.isNullOrEmpty(properties.getPassword()))
        {
            factory.setPassword(properties.getPassword());
            log.info("Redis 设置了密码，将启用认证");
        }

        return factory;
    }

    /**
     * RedisTemplate<br />
     * <i>Key 的序列化方式是 String，Value 序列化用 JDK 的</i>
     * 
     * 
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<StringRedisSerializer, JdkSerializationRedisSerializer> redisTemplate()
    {
        log.info("创建 Spring 的 RedisTemplate");

        RedisTemplate<StringRedisSerializer, JdkSerializationRedisSerializer> template = new RedisTemplate<StringRedisSerializer, JdkSerializationRedisSerializer>();
        template.setConnectionFactory(jedisConnectionFactory());

        return template;
    }

}