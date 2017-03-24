package com.joindata.inf.common.support.redis.bootconfig;

import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.support.redis.component.RedisClient;
import com.joindata.inf.common.support.redis.component.impl.ClusterRedisClient;
import com.joindata.inf.common.support.redis.component.impl.SingleRedisClient;
import com.joindata.inf.common.support.redis.support.RedisProperties;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.common.util.network.NetworkUtil;
import com.joindata.inf.common.util.network.entity.HostPort;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@Configuration
public class RedisConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private RedisProperties properties;

    @Bean
    public RedisClient redisClient()
    {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(properties.getMaxActive());
        config.setMaxIdle(properties.getMaxIdle());
        config.setMaxWaitMillis(properties.getMaxWait());
        config.setTestOnBorrow(properties.isTestOnBorrow());
        config.setTestOnReturn(properties.isTestOnReturn());

        log.info("Redis 连接地址: {}, 最大活动连接数: {}, 最大空闲数: {}", properties.getHost(), properties.getMaxActive(), properties.getMaxIdle());

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

            JedisCluster jedisCluster = new JedisCluster(hostSet, config);
            redisClient = new ClusterRedisClient(jedisCluster);
        }
        else
        {
            Jedis jedis = new Jedis(hostPorts[0].getHost(), hostPorts[1].getPort());
            redisClient = new SingleRedisClient(jedis);
        }

        return redisClient;
    }
}