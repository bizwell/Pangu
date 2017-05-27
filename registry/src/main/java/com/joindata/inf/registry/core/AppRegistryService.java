package com.joindata.inf.registry.core;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;

import com.joindata.inf.common.util.basic.BeanUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.registry.entity.AppInstanceInfo;
import com.joindata.inf.registry.entity.AppInstanceSummary;
import com.joindata.inf.registry.entity.AppStartInfo;
import com.joindata.inf.registry.entity.AppSummary;
import com.joindata.inf.registry.entity.AppVersionSummary;
import com.joindata.inf.registry.util.RegistryZnodeUtil;

public class AppRegistryService
{
    private static final Logger log = Logger.get();

    private CuratorFramework zkClient;

    public AppRegistryService(CuratorFramework zkClient)
    {
        this.zkClient = zkClient;
    }

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
                List<AppInstanceSummary> instanceSummaryList = CollectionUtil.newList();
                for(String instance: getInstanceList(app, version))
                {
                    AppInstanceSummary instanceSummary = new AppInstanceSummary();
                    instanceSummary.setFatalInfo(getFatalInfo(app, version, instance));
                    instanceSummary.setStartInfo(getStartInfo(app, version, instance));
                    instanceSummary.setInstanceInfo(getInstanceInfo(app, version, instance));
                    instanceSummaryList.add(instanceSummary);
                }

                versionSummary.add(version, instanceSummaryList);
            }

            appSummary.add(app, versionSummary);
        }

        return appSummary;
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
     * 获取指定实例的失败信息
     */
    public String getFatalInfo(String appId, String version, String instanceId) throws Exception
    {
        String fatalNode = RegistryZnodeUtil.getFatalNode(appId, version, instanceId);
        if(zkClient.checkExists().forPath(fatalNode) == null)
        {
            return null;
        }

        String info = BeanUtil.deserializeObject(zkClient.getData().forPath(fatalNode));
        log.info("获取 {} 的版本 {} 的实例 {} 的失败信息： {}", appId, version, instanceId, info);

        return info;
    }
}
