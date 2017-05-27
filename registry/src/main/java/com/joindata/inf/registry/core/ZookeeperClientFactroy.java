package com.joindata.inf.registry.core;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

import com.joindata.inf.common.support.disconf.core.DisconfApi;

/**
 * Zookeeper 客户端单例工厂
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 26, 2017 3:49:56 PM
 */
public class ZookeeperClientFactroy
{
    private static CuratorFramework INSTANCE;

    /**
     * 拿 Zookeeper 的客户端
     */
    public static final CuratorFramework get()
    {
        if(INSTANCE == null)
        {
            INSTANCE = CuratorFrameworkFactory.newClient(DisconfApi.getZkHost(), new RetryNTimes(10, 5000));
        }

        return INSTANCE;
    }
}
