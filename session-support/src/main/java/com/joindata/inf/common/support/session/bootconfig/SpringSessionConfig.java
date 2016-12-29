package com.joindata.inf.common.support.session.bootconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.joindata.inf.common.support.redis.cst.RedisType;
import com.joindata.inf.common.support.redis.support.RedisProperties;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.common.util.network.NetworkUtil;
import com.joindata.inf.common.util.network.entity.HostPort;

import redis.clients.jedis.JedisPoolConfig;

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
    private static final Logger log = Logger.get();

    @Autowired
    private RedisProperties properties;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory()
    {
        log.info("初始化  session 的 jedis 连接工厂");

        JedisConnectionFactory factory = null;

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(properties.getMaxIdle());
        config.setMaxTotal(properties.getMaxActive());
        config.setMaxWaitMillis(properties.getMaxWait());
        config.setTestOnBorrow(properties.isTestOnBorrow());
        config.setTestOnReturn(properties.isTestOnReturn());

        HostPort[] hostPorts = NetworkUtil.parseHostPort(properties.getHost());

        switch(properties.getType())
        {
            case RedisType.SHARD:
                log.warn("Spring Data 目前不支持使用 SHARD 集群，将通过 redis 集群来连接");
                // TODO SpringData 傻逼乎乎的不能用 SHARD POOL
                // log.info("HTTP Session 将通过 redis 分片来存储，主机列表: {}", properties.getHost());
                //
                // List<JedisShardInfo> shardInfo = CollectionUtil.newList();
                // for(HostPort hostPort: hostPorts)
                // {
                // shardInfo.add(new JedisShardInfo(hostPort.getHost(), hostPort.getPort()));
                // }
                //
                // ShardedJedisPool pool = new ShardedJedisPool(config, shardInfo);
                //
                // factory = new JedisConnectionFactory(shardInfo.get(0));
                // break;
            case RedisType.CLUSTER:
                log.info("HTTP Session 将通过 redis 集群来存储，主机列表: {}", properties.getHost());

                RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
                for(HostPort hostPort: hostPorts)
                {
                    clusterConfig.addClusterNode(new RedisNode(hostPort.getHost(), hostPort.getPort()));
                }

                factory = new JedisConnectionFactory(clusterConfig, config);
                break;
        }

        return factory;
    }
}
