package com.joindata.inf.common.support.redis.support;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * Redis 配置参数
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 6, 2017 4:31:20 PM
 */
@Service
@Scope("singleton")
@DisconfFile(filename = "redis.properties")
public class RedisProperties
{
    /** 集群方式（SHARD 或 CLUSTER） */
    private String type;

    /** 主机（可设置多个，格式为 host1:port,host2:port） */
    private String host;

    /** 密码，可设置也可不设置，如果要求授权必须设置（RedisCluster 不支持密码） */
    private String password;

    /** 最多活动数 */
    private int maxActive;

    /** 最多空闲数 */
    private int maxIdle;

    /** 最多等待时间（毫秒） */
    private long maxWait;

    /** 取到连接时测试 */
    private boolean testOnBorrow;

    /** 归还连接时测试 */
    private boolean testOnReturn;

    @DisconfFileItem(name = "ha.type", associateField = "type")
    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @DisconfFileItem(name = "redis.host", associateField = "host")
    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    @DisconfFileItem(name = "redis.maxActive", associateField = "maxActive")
    public int getMaxActive()
    {
        return maxActive;
    }

    public void setMaxActive(int maxActive)
    {
        this.maxActive = maxActive;
    }

    @DisconfFileItem(name = "redis.maxIdle", associateField = "maxIdle")
    public int getMaxIdle()
    {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle)
    {
        this.maxIdle = maxIdle;
    }

    @DisconfFileItem(name = "redis.maxWait", associateField = "maxWait")
    public long getMaxWait()
    {
        return maxWait;
    }

    public void setMaxWait(long maxWait)
    {
        this.maxWait = maxWait;
    }

    @DisconfFileItem(name = "redis.testOnBorrow", associateField = "testOnBorrow")
    public boolean isTestOnBorrow()
    {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow)
    {
        this.testOnBorrow = testOnBorrow;
    }

    @DisconfFileItem(name = "redis.testOnReturn", associateField = "testOnReturn")
    public boolean isTestOnReturn()
    {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn)
    {
        this.testOnReturn = testOnReturn;
    }

    @DisconfFileItem(name = "redis.password", associateField = "password")
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

}
