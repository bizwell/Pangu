package com.joindata.inf.registry;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import com.joindata.inf.common.basic.errors.SystemError;
import com.joindata.inf.common.basic.exceptions.SystemException;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.util.basic.BeanUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.registry.core.ZookeeperClientFactroy;
import com.joindata.inf.registry.entity.AppHeartbeatInfo;
import com.joindata.inf.registry.entity.AppInstanceInfo;
import com.joindata.inf.registry.entity.AppStartInfo;
import com.joindata.inf.registry.util.RegistryZnodeUtil;

/**
 * 当前应用的注册器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 26, 2017 3:48:07 PM
 */
public class CurrentAppRegistry
{
    private static final Logger log = Logger.get();

    private CuratorFramework ZkClient;

    private AppInstanceInfo appInfo;

    private static CurrentAppRegistry INSTANCE;

    // 保证单例
    private CurrentAppRegistry()
    {
        if(!BootInfoHolder.hasBootAnno(EnableDisconf.class))
        {
            throw new SystemException(SystemError.DEPEND_COMPONENT_NOT_READY, "要启用注册中心，必须启用 @Disconf 先");
        }

        ZkClient = ZookeeperClientFactroy.get();
    }

    /** 获取注册中心实例 */
    public static final CurrentAppRegistry get() throws IOException
    {
        if(INSTANCE == null)
        {
            INSTANCE = new CurrentAppRegistry();
        }

        return INSTANCE;
    }

    /**
     * 创建实例节点
     */
    public void createInstance() throws Exception
    {
        if(!BootInfoHolder.isRegistryEnabled())
        {
            log.debug("已禁用注册中心，将不注册应用实例");
            return;
        }

        this.appInfo = new AppInstanceInfo();

        String instanceNode = RegistryZnodeUtil.getInstanceNode(appInfo.getAppId(), appInfo.getVersion(), appInfo.getInstanceId());

        log.info("注册应用实例: {}", appInfo.getInstanceId());
        log.debug("实例注册节点: {}, 实例信息: {}", instanceNode, appInfo);

        ZkClient.start();

        // 持久节点，设置应用实例信息
        ZkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(instanceNode, BeanUtil.serializeObject(appInfo));

        // 临时节点，表示正在启动
        String startingNode = RegistryZnodeUtil.getStartingNode(appInfo.getAppId(), appInfo.getVersion(), appInfo.getInstanceId());
        log.debug("实例启动中节点: {}", startingNode);
        ZkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(startingNode);
    }

    /**
     * 设置已启动
     */
    public void setStarted() throws Exception
    {
        if(!BootInfoHolder.isRegistryEnabled())
        {
            log.debug("已禁用注册中心，将不报告程序已启动");
            return;
        }

        log.info("注册应用实例状态 - 已启动");

        AppStartInfo info = new AppStartInfo();
        info.setTimeCoast(System.currentTimeMillis() - this.appInfo.getBootTime());
        info.setMemoryUsage(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        info.setTotalMemory(Runtime.getRuntime().totalMemory());

        String startedNode = RegistryZnodeUtil.getStartedNode(appInfo.getAppId(), appInfo.getVersion(), appInfo.getInstanceId());

        log.debug("实例已启动状态节点: {}, 节点数据: {}", startedNode, info);

        // 启动信息节点
        ZkClient.create().withMode(CreateMode.PERSISTENT).forPath(startedNode, BeanUtil.serializeObject(info));

        // 创建心跳节点
        String heartbeatNode = RegistryZnodeUtil.getHeartbeatNode(appInfo.getAppId(), appInfo.getVersion(), appInfo.getInstanceId());
        ZkClient.create().withMode(CreateMode.PERSISTENT).forPath(heartbeatNode);

        int peroid = 30000;
        // 启动心跳线程，每 30 秒心跳一次
        Timer timer = new Timer(true);
        timer.schedule(new HeartbeatThread(ZkClient, heartbeatNode), 0, peroid);

        log.info("启动心跳发送线程, 节点: {}, 心跳间隔: {}ms", heartbeatNode, peroid);

        // 临时节点，有这个节点表示运行中，程序退出后就没了
        String runningNode = RegistryZnodeUtil.getRunningNode(appInfo.getAppId(), appInfo.getVersion(), appInfo.getInstanceId());
        ZkClient.create().withMode(CreateMode.EPHEMERAL).forPath(runningNode);

        // 注册关闭钩子，在收到退出信号时删除 running 节点
        Runtime.getRuntime().addShutdownHook(new ShutdownHook(ZkClient, runningNode));

        // 删除启动中节点，表示不算启动中了
        String startingNode = RegistryZnodeUtil.getStartingNode(appInfo.getAppId(), appInfo.getVersion(), appInfo.getInstanceId());
        ZkClient.delete().forPath(startingNode);

        log.info("实例运行中临时节点: {}", runningNode);
    }

    /**
     * 设置启动失败，并设置异常
     */
    public void setFatal(Throwable e) throws Exception
    {
        if(!BootInfoHolder.isRegistryEnabled())
        {
            log.debug("已禁用注册中心，将不报告程序启动失败");
            return;
        }

        log.info("注册应用实例状态 - 启动失败");

        String fatalNode = RegistryZnodeUtil.getFatalNode(appInfo.getAppId(), appInfo.getVersion(), appInfo.getInstanceId());

        log.debug("实例启动失败状态节点: {}, 节点数据: {}", fatalNode, e);

        // 持久节点，设置失败堆栈
        ZkClient.create().withMode(CreateMode.PERSISTENT).forPath(fatalNode, StringUtil.toBytes(e.toString()));
    }

    /**
     * 设置启动失败，设置消息
     */
    public void setFatal(String msg) throws Exception
    {
        if(!BootInfoHolder.isRegistryEnabled())
        {
            log.debug("已禁用注册中心，将不报告程序启动失败");
            return;
        }

        log.info("注册应用实例状态 - 启动失败");

        String fatalNode = RegistryZnodeUtil.getFatalNode(appInfo.getAppId(), appInfo.getVersion(), appInfo.getInstanceId());

        log.debug("实例启动失败状态节点: {}, 节点数据: {}", fatalNode, msg);

        // 持久节点，设置失败堆栈
        ZkClient.create().withMode(CreateMode.PERSISTENT).forPath(fatalNode, msg.getBytes("UTF-8"));
    }

}

/**
 * 心跳线程
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 31, 2017 12:18:38 PM
 */
class HeartbeatThread extends TimerTask
{
    private static final Logger log = Logger.get();

    private CuratorFramework zkClient;

    private String node;

    public HeartbeatThread(CuratorFramework zkClient, String node)
    {
        this.zkClient = zkClient;
        this.node = node;
    }

    @Override
    public void run()
    {
        log.debug("往注册中心发送心跳, 节点: {}", this.node);
        try
        {
            zkClient.setData().forPath(node, BeanUtil.serializeObject(new AppHeartbeatInfo()));
        }
        catch(Exception e)
        {
            log.error("注册中心心跳发送失败: {}", e.getMessage(), e);
        }
    }
}

/**
 * 关闭钩子
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jun 6, 2017 3:46:25 PM
 */
class ShutdownHook extends Thread
{
    private static final Logger log = Logger.get();

    private CuratorFramework zkClient;

    private String node;

    public ShutdownHook(CuratorFramework zkClient, String node)
    {
        this.zkClient = zkClient;
        this.node = node;
    }

    @Override
    public void run()
    {
        // 临时节点，有这个节点表示运行中，程序退出后就没了
        try
        {
            log.info("收到退出信号，正在删除 {} 节点", node);
            zkClient.delete().guaranteed().forPath(node);
        }
        catch(Exception e)
        {
            log.error("删除运行中节点出错: {}", e, e.getMessage());
        }
    }
}
