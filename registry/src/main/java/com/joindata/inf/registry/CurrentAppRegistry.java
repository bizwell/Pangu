package com.joindata.inf.registry;

import java.io.IOException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import com.joindata.inf.common.basic.errors.SystemError;
import com.joindata.inf.common.basic.exceptions.SystemException;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.util.basic.BeanUtil;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.registry.core.ZookeeperClientFactroy;
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

        // 临时节点，如果没有该节点表示未启动
        ZkClient.create().withMode(CreateMode.EPHEMERAL).forPath(startedNode, BeanUtil.serializeObject(info));
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
        ZkClient.create().withMode(CreateMode.PERSISTENT).forPath(fatalNode, BeanUtil.serializeObject(e));
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
