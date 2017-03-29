package com.joindata.inf.common.support.idgen.component.sequence.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.framework.api.SetDataBuilder;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.zookeeper.CreateMode;

import com.joindata.inf.common.basic.support.SpringContextHolder;
import com.joindata.inf.common.support.idgen.bootconfig.IdgenConfig;
import com.joindata.inf.common.support.idgen.component.Sequence;
import com.joindata.inf.common.util.log.Logger;

/**
 * 5位时间戳+2位命名空间+序号的递增序列
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 24, 2017 7:36:26 PM
 */
public class TimestampSequence implements Sequence
{
    private static final Logger log = Logger.get();
    
    private CuratorFramework zkClient;

    public static final String BASE_NODE = "/seq-tmp";

    private String sequenceName;

    private String node;

    private SetDataBuilder setDataBuilder;

    private GetDataBuilder getDataBuilder;

    private ExistsBuilder existsBuilder;

    // TODO
    private long base = 101000000000000L;

    public TimestampSequence(String sequenceName)
    {
        this.sequenceName = sequenceName;
        this.node = BASE_NODE + "/" + this.sequenceName;
    }

    @Override
    public synchronized long next()
    {
        if(zkClient == null)
        {
            zkClient = SpringContextHolder.getBean(IdgenConfig.ZK_CLIENT_BEAN_NAME);
        }

        log.info("Zookeeper 连接状态: {}", zkClient.getState());
        if(zkClient.getState() != CuratorFrameworkState.STARTED)
        {
            zkClient.start();
        }

        setDataBuilder = zkClient.setData();
        getDataBuilder = zkClient.getData();
        existsBuilder = zkClient.checkExists();

        try
        {
            if(existsBuilder.forPath(this.node) == null)
            {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(this.node, String.valueOf(base).getBytes());
            }
        }
        catch(Exception e)
        {
            log.error("创建序列生成器出错, {}", e.getMessage(), e);
        }

        long value = -1;
        try
        {
            int version = existsBuilder.forPath(this.node).getVersion();
            String val = new String(getDataBuilder.forPath(this.node));

            log.info("取到的值: {}", val);
            value = Long.parseLong(val);
            log.info("原始序列值: {}", value);

            while(true)
            {
                try
                {
                    value++;
                    log.info("value++ 后的值: {}", value);
                    setDataBuilder.withVersion(version).forPath(this.node, String.valueOf(value).getBytes());
                    break;
                }
                catch(Exception e)
                {
                    log.error("更新序列失败, {}", e.getMessage(), e);
                    log.error("正在重试, 重试序列值 {}", value);
                }
            }

            log.info("新序列值: {}", value);

        }
        catch(Exception e)
        {
            log.error("获取序列出错, {}", e.getMessage(), e);
        }

        // zkClient.close();

        return value;
    }
}
