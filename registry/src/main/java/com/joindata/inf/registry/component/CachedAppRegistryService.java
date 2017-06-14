package com.joindata.inf.registry.component;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joindata.inf.registry.core.AppSummaryCache;
import com.joindata.inf.registry.entity.AppHeartbeatInfo;
import com.joindata.inf.registry.entity.AppInstanceInfo;
import com.joindata.inf.registry.entity.AppInstanceSummary;
import com.joindata.inf.registry.entity.AppStartInfo;
import com.joindata.inf.registry.entity.AppSummary;
import com.joindata.inf.registry.entity.AppVersionSummary;
import com.joindata.inf.registry.enums.InstanceState;
import com.joindata.inf.registry.util.InstanceUtil;

/**
 * 缓存的应用注册操作服务
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jun 5, 2017 11:06:07 AM
 */
@Component
public class CachedAppRegistryService
{
    @Autowired
    private AppSummaryCache cache;

    /**
     * 更新实例数据
     * 
     * @param appId
     * @param version
     * @param instanceId
     * @throws Exception
     */
    public void refreshInstanceSummary(String appId, String version, String instanceId) throws Exception
    {
        cache.refreshInstanceSummary(appId, version, instanceId);
    }

    public void updateFatalInfo(String appId, String version, String instanceId, String fatalInfo) throws Exception
    {
        cache.updateFatalInfo(appId, version, instanceId, fatalInfo);
    }

    public void updateStartInfo(String appId, String version, String instanceId, AppStartInfo startInfo) throws Exception
    {
        cache.updateStartInfo(appId, version, instanceId, startInfo);
    }

    public void updateInstanceInfo(AppInstanceInfo instanceInfo) throws Exception
    {
        cache.updateInstanceInfo(instanceInfo);
        cache.updateHosts(instanceInfo.getAppId(), instanceInfo.getVersion(), instanceInfo.getInstanceId(), InstanceUtil.parseInstanceSign(instanceInfo.getInstanceId()).getHosts());
    }

    public void updateHeartbeatInfo(String appId, String version, String instanceId, AppHeartbeatInfo heartbeatInfo) throws Exception
    {
        cache.updateHeartbeatInfo(appId, version, instanceId, heartbeatInfo);
    }

    public void updateState(String appId, String version, String instanceId, InstanceState state) throws Exception
    {
        cache.updateState(appId, version, instanceId, state);
    }

    /**
     * 获取注册中心中的应用列表
     * 
     * @return 应用 ID 列表
     */
    public Collection<String> getAppList()
    {
        return cache.getAll().keySet();
    }

    /**
     * 获取注册中心中的指定应用的版本列表
     * 
     * @param appId 应用 ID
     * @return 版本列表
     */
    public Collection<String> getVersionList(String appId)
    {
        return cache.getVersions(appId);
    }

    /**
     * 获取指定应用的
     * 
     * @param appId 应用 ID
     * @return 应用所有版本的实例信息
     */
    public AppVersionSummary getAppSummary(String appId)
    {
        return cache.getAppSummary(appId);
    }

    /**
     * 获取全部注册数据
     * 
     * @return 全部数据
     */
    public AppSummary getAll()
    {
        return cache.getAll();
    }

    /**
     * 获取全部运行中的实例数据
     */
    public Collection<AppInstanceSummary> getAllRunning()
    {
        return cache.getAllRunning();
    }

    /**
     * 获取指定应用指定版本的实例列表
     */
    public Collection<String> getInstanceList(String appId, String version)
    {
        return cache.getInstanceList(appId, version);
    }

    /**
     * 获取指定应用所有的实例列表(所有版本)
     */
    public Collection<String> getInstanceList(String appId) throws Exception
    {
        return cache.getInstanceList(appId);
    }

    public void checkInstanceState()
    {
        cache.checkInstanceState();
    }
}
