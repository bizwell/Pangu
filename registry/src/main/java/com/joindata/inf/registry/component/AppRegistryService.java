package com.joindata.inf.registry.component;

import java.util.List;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.joindata.inf.common.util.basic.BeanUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.registry.core.ZookeeperClientFactroy;
import com.joindata.inf.registry.entity.AppHeartbeatInfo;
import com.joindata.inf.registry.entity.AppInstanceInfo;
import com.joindata.inf.registry.entity.AppInstanceSummary;
import com.joindata.inf.registry.entity.AppStartInfo;
import com.joindata.inf.registry.entity.AppSummary;
import com.joindata.inf.registry.entity.AppVersionSummary;
import com.joindata.inf.registry.enums.InstanceState;
import com.joindata.inf.registry.util.InstanceUtil;
import com.joindata.inf.registry.util.RegistryZnodeUtil;

import lombok.Getter;

@Component
@Getter
public class AppRegistryService implements InitializingBean
{
    private static final Logger log = Logger.get();

    private CuratorFramework zkClient;

    /**
     * 获取注册中心中的应用列表
     * 
     * @return 应用 ID 列表
     */
    public List<String> getAppList() throws Exception
    {
        List<String> list = CollectionUtil.newList();
        list.addAll(zkClient.getChildren().forPath(RegistryZnodeUtil.getRoot()));

        log.info("获取注册中心的全部应用名： {}", list);
        return list;
    }

    /**
     * 获取注册中心中的指定应用的版本列表
     * 
     * @return 版本列表
     */
    public List<String> getVersionList(String app) throws Exception
    {
        List<String> list = CollectionUtil.newList();
        list.addAll(zkClient.getChildren().forPath(RegistryZnodeUtil.getAppNode(app)));

        log.info("获取 {} 的全部版本： {}", app, list);
        return list;
    }

    /**
     * 获取指定应用的
     * 
     * @param app 应用 ID
     */
    public AppVersionSummary getAppSummary(String app) throws Exception
    {
        if(!isAppRegisted(app))
        {
            return null;
        }
        AppVersionSummary versionSummary = new AppVersionSummary();

        for(String version: getVersionList(app))
        {
            Map<String, AppInstanceSummary> instanceSummaryList = CollectionUtil.newMap();
            for(String instance: getInstanceList(app, version))
            {
                AppInstanceSummary instanceSummary = getInstanceSummary(app, version, instance);
                instanceSummaryList.put(instance, instanceSummary);
            }

            versionSummary.add(version, instanceSummaryList);
        }
        return versionSummary;
    }

    /**
     * 获取全部注册数据
     * 
     * @return 全部数据
     */
    public AppSummary getAll() throws Exception
    {
        AppSummary appSummary = new AppSummary();
        for(String app: getAppList())
        {
            AppVersionSummary versionSummary = new AppVersionSummary();
            for(String version: getVersionList(app))
            {
                Map<String, AppInstanceSummary> instanceSummaryList = CollectionUtil.newMap();
                for(String instance: getInstanceList(app, version))
                {
                    AppInstanceSummary instanceSummary = getInstanceSummary(app, version, instance);
                    instanceSummaryList.put(instance, instanceSummary);
                }

                versionSummary.add(version, instanceSummaryList);
            }

            appSummary.add(app, versionSummary);
        }

        return appSummary;
    }

    /**
     * 获取应用实例数据
     * 
     * @param app
     * @param version
     * @param instance
     * @return
     */
    public AppInstanceSummary getInstanceSummary(String app, String version, String instance) throws Exception
    {
        AppInstanceSummary instanceSummary = new AppInstanceSummary();
        instanceSummary.setFatalInfo(getFatalInfo(app, version, instance));
        instanceSummary.setStartInfo(getStartInfo(app, version, instance));
        instanceSummary.setInstanceInfo(getInstanceInfo(app, version, instance));
        instanceSummary.setHosts(InstanceUtil.parseInstanceSign(instance).getHosts());
        instanceSummary.setHeartbeatInfo(getHeartbeatInfo(app, version, instance));
        InstanceState state = null;
        if(isRunning(app, version, instance))
        {
            state = InstanceState.RUNNING;
        }
        else if(instanceSummary.getFatalInfo() != null)
        {
            state = InstanceState.FATAL;
        }
        else if(isStarting(app, version, instance))
        {
            state = InstanceState.STARTING;
        }
        else if(instanceSummary.getFatalInfo() == null && instanceSummary.getStartInfo() != null)
        {
            state = InstanceState.STOPPED;
        }
        else
        {
            state = InstanceState.ABNORMAL;
        }

        instanceSummary.setState(state);
        return instanceSummary;
    }

    /**
     * 获取全部注册数据
     * 
     * @return 全部数据
     */
    public List<AppInstanceSummary> getAllRunning() throws Exception
    {
        List<AppInstanceSummary> instanceSummaryList = CollectionUtil.newList();
        for(String app: getAppList())
        {
            for(String version: getVersionList(app))
            {
                for(String instance: getInstanceList(app, version))
                {
                    if(isRunning(app, version, instance))
                    {
                        AppInstanceSummary instanceSummary = new AppInstanceSummary();
                        instanceSummary.setFatalInfo(getFatalInfo(app, version, instance));
                        instanceSummary.setStartInfo(getStartInfo(app, version, instance));
                        instanceSummary.setInstanceInfo(getInstanceInfo(app, version, instance));
                        instanceSummary.setHosts(InstanceUtil.parseInstanceSign(instance).getHosts());
                        instanceSummary.setHeartbeatInfo(getHeartbeatInfo(app, version, instance));
                        instanceSummary.setState(InstanceState.RUNNING);
                        instanceSummaryList.add(instanceSummary);
                    }
                }
            }
        }

        return instanceSummaryList;
    }

    /**
     * 获取指定应用指定版本的实例列表
     */
    public List<String> getInstanceList(String appId, String version) throws Exception
    {
        List<String> list = CollectionUtil.newList();
        list.addAll(zkClient.getChildren().forPath(RegistryZnodeUtil.getInstanceDirNode(appId, version)));

        log.info("获取 {} 的版本 {} 的实例名列表： {}", appId, version, list);
        return list;
    }

    /**
     * 获取指定应用所有的实例列表(所有版本)
     */
    public List<String> getInstanceList(String appId) throws Exception
    {
        List<String> list = CollectionUtil.newList();

        zkClient.getChildren().forPath(RegistryZnodeUtil.getAppNode(appId)).forEach(version ->
        {
            try
            {
                list.addAll(getInstanceList(appId, version));
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        });

        log.info("获取 {} 的全部版本的实例名列表： {}", appId, list);

        return list;
    }

    /**
     * 获取指定实例信息
     */
    public AppInstanceInfo getInstanceInfo(String appId, String version, String instanceId) throws Exception
    {
        String instanceNode = RegistryZnodeUtil.getInstanceNode(appId, version, instanceId);
        if(zkClient.checkExists().forPath(instanceNode) == null)
        {
            return null;
        }

        AppInstanceInfo info = BeanUtil.deserializeObject(zkClient.getData().forPath(instanceNode));

        log.info("获取 {} 的版本 {} 的实例 {} 的实例信息： {}", appId, version, instanceId, info);

        return info;
    }

    /**
     * 获取指定实例的启动信息
     */
    public AppStartInfo getStartInfo(String appId, String version, String instanceId) throws Exception
    {
        String startNode = RegistryZnodeUtil.getStartedNode(appId, version, instanceId);
        if(zkClient.checkExists().forPath(startNode) == null)
        {
            return null;
        }

        AppStartInfo info = BeanUtil.deserializeObject(zkClient.getData().forPath(startNode));
        log.info("获取 {} 的版本 {} 的实例 {} 的启动信息： {}", appId, version, instanceId, info);

        return info;
    }

    /**
     * 获取指定实例的心跳信息
     */
    public AppHeartbeatInfo getHeartbeatInfo(String appId, String version, String instanceId) throws Exception
    {
        String heartbeatNode = RegistryZnodeUtil.getHeartbeatNode(appId, version, instanceId);
        if(zkClient.checkExists().forPath(heartbeatNode) == null)
        {
            return null;
        }

        AppHeartbeatInfo info = BeanUtil.deserializeObject(zkClient.getData().forPath(heartbeatNode));
        log.info("获取 {} 的版本 {} 的实例 {} 的心跳信息： {}", appId, version, instanceId, info);

        return info;
    }

    /**
     * 获取指定实例的失败信息
     */
    public String getFatalInfo(String appId, String version, String instanceId) throws Exception
    {
        String fatalNode = RegistryZnodeUtil.getFatalNode(appId, version, instanceId);
        if(zkClient.checkExists().forPath(fatalNode) == null)
        {
            return null;
        }

        String info = StringUtil.toString(zkClient.getData().forPath(fatalNode));
        log.info("获取 {} 的版本 {} 的实例 {} 的失败信息： {}", appId, version, instanceId, info);

        return info;
    }

    /**
     * 获取指定实例是否在运行
     */
    public boolean isRunning(String appId, String version, String instanceId) throws Exception
    {
        boolean running = zkClient.checkExists().forPath(RegistryZnodeUtil.getRunningNode(appId, version, instanceId)) != null;
        log.info("获取 {} 的版本 {} 的实例 {} 是否运行： {}", appId, version, instanceId, running);

        return running;
    }

    /**
     * 获取指定实例是否在启动
     */
    public boolean isStarting(String appId, String version, String instanceId) throws Exception
    {
        boolean running = zkClient.checkExists().forPath(RegistryZnodeUtil.getStartingNode(appId, version, instanceId)) != null;
        log.info("获取 {} 的版本 {} 的实例 {} 是否正在启动： {}", appId, version, instanceId, running);

        return running;
    }

    /**
     * 判断应用是否注册过
     * 
     * @param appId 应用 ID
     * @return true，如果注册过
     * @throws Exception
     */
    public boolean isAppRegisted(String appId) throws Exception
    {
        boolean registered = zkClient.checkExists().forPath(RegistryZnodeUtil.getAppNode(appId)) != null;
        log.info("获取应用 {} 是否注册过： {}", appId, registered);

        return registered;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        this.zkClient = ZookeeperClientFactroy.get();
    }
}
