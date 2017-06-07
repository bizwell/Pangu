/**
 * 
 */
package com.joindata.inf.common.support.idgen.core;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;

import com.google.common.primitives.Longs;

/**
 * 基于zookeeper实现的分布式id 自增原子性操作
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年3月27日
 */
public class SequenceRepositoryZookeeper implements SequenceRepository
{
    private static final int RETRY_MAX_TIMES = 100;

    private CuratorFramework zkClient;

    public SequenceRepositoryZookeeper(CuratorFramework zkClient)
    {
        this.zkClient = zkClient;
    }

    @Override
    public boolean exist(String key) throws Exception
    {
        ensureStart();
        return zkClient.checkExists().forPath(key) != null;
    }

    @Override
    public long increaseAndGet(String key, long addValue) throws Exception
    {
        ensureStart();
        DistributedAtomicLong distributedAtomicLong = new DistributedAtomicLong(zkClient, key, new RetryOneTime(RETRY_MAX_TIMES));
        distributedAtomicLong.add(addValue);
        long startId = distributedAtomicLong.get().postValue();
        return startId;
    }

    @Override
    public long get(String key) throws Exception
    {
        ensureStart();
        return Longs.fromByteArray(zkClient.getData().forPath(key));
    }

    /**
     * 确保zkClient连接状态
     */
    private void ensureStart()
    {
        if(zkClient.getState() != CuratorFrameworkState.STARTED)
        {
            zkClient.start();
        }
    }

    @Override
    public long getAndIncrease(String key, long addValue) throws Exception
    {
        ensureStart();
        DistributedAtomicLong distributedAtomicLong = new DistributedAtomicLong(zkClient, key, new RetryOneTime(RETRY_MAX_TIMES));
        long startId = distributedAtomicLong.get().preValue();
        distributedAtomicLong.add(addValue);
        return startId;
    }

    @Override
    public long set(String key, long value) throws Exception
    {
        ensureStart();
        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(key, Longs.toByteArray(value));
        return value;
    }

    @Override
    public void delete(String key) throws Exception
    {
        ensureStart();
        zkClient.delete().forPath(key);
    }
}
