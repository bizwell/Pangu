package com.joindata.inf.registry.core;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.registry.component.AppRegistryService;
import com.joindata.inf.registry.entity.AppInstanceSummary;
import com.joindata.inf.registry.entity.AppSummary;
import com.joindata.inf.registry.entity.AppVersionSummary;
import com.joindata.inf.registry.enums.InstanceState;

/**
 * 用于缓存应用列表和实例信息，通过 Watcher 或其他方式来更新该 Map，避免重复遍历 Zookeeper 节点带来的性能问题
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jun 5, 2017 10:25:12 AM
 */
@Component
public class AppSummaryCache implements InitializingBean
{
    private static final Logger log = Logger.get();

    private static final AppSummary SUMMARY = new AppSummary();

    @Autowired
    private AppRegistryService service;

    /**
     * 获取全部缓存数据
     */
    public AppSummary getAll()
    {
        return SUMMARY;
    }

    /**
     * 更新应用实例数据
     * 
     * @param appId 应用 ID
     * @param version 版本号
     * @param instanceId 实例 ID
     */
    public void refreshInstanceSummary(String appId, String version, String instanceId) throws Exception
    {
        log.info("刷新实例缓存, 应用: {}, 版本: {}, 实例: {}", appId, version, instanceId);
        if(!SUMMARY.containsKey(appId))
        {
            log.info("没有应用缓存，将创建应用 {}", appId);
            SUMMARY.put(appId, new AppVersionSummary());
        }

        AppInstanceSummary summary = service.getInstanceSummary(appId, version, instanceId);

        if(!SUMMARY.get(appId).containsKey(version))
        {
            log.info("没有应用 {} 的版本缓存，将创建版本 {}", appId, version);
            SUMMARY.get(appId).put(version, CollectionUtil.newMap());
        }

        log.info("刷新的实例缓存是: {}", summary);

        SUMMARY.get(appId).get(version).put(instanceId, summary);
    }

    public Collection<String> getApps()
    {
        return SUMMARY.keySet();
    }

    public Collection<String> getVersions(String appId)
    {
        return SUMMARY.get(appId).keySet();
    }

    public AppVersionSummary getAppSummary(String appId)
    {
        return SUMMARY.get(appId);
    }

    public Collection<AppInstanceSummary> getAllRunning()
    {
        List<AppInstanceSummary> runningList = CollectionUtil.newList();
        SUMMARY.forEach((appId, versionSummary) ->
        {
            versionSummary.forEach((version, instances) ->
            {
                instances.values().forEach(instance ->
                {
                    if(instance.getState() == InstanceState.RUNNING)
                    {
                        runningList.add(instance);
                    }
                });
            });
        });

        return runningList;
    }

    public Collection<String> getInstanceList(String appId)
    {
        Collection<String> instanceList = CollectionUtil.newHashSet();

        SUMMARY.get(appId).forEach((version, instances) ->
        {
            instanceList.addAll(instances.keySet());
        });

        return instanceList;
    }

    public Collection<String> getInstanceList(String appId, String version)
    {
        return SUMMARY.get(appId).get(version).keySet();
    }

    public AppInstanceSummary getInstanceSummary(String appId, String version, String instanceId)
    {
        return SUMMARY.get(appId).get(version).get(instanceId);
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        SUMMARY.putAll(service.getAll());
    }
}
