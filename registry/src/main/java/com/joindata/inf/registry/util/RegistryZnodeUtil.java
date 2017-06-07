package com.joindata.inf.registry.util;

import com.joindata.inf.common.util.basic.StringUtil;

/**
 * 注册中心的工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 26, 2017 3:27:57 PM
 */
public class RegistryZnodeUtil
{
    /** {@value #ROOT_NODE} */
    public static final String ROOT_NODE = "/app";

    /** {@value #ROOT_NODE} */
    public static final String getRoot()
    {
        return ROOT_NODE;
    }

    /**
     * {@value #ROOT_NODE}/应用ID
     */
    public static final String getAppNode(String appId)
    {
        return ROOT_NODE + "/" + appId;
    }

    /**
     * {@value #ROOT_NODE}/应用ID/版本号
     */
    public static final String getVersionNode(String appId, String version)
    {
        return getAppNode(appId) + "/" + version;
    }

    /**
     * {@value #ROOT_NODE}/应用ID/版本号/instance
     */
    public static final String getInstanceDirNode(String appId, String version)
    {
        return getVersionNode(appId, version) + "/instance";
    }

    /**
     * {@value #ROOT_NODE}/应用ID/版本号/instance/实例ID
     */
    public static final String getInstanceNode(String appId, String version, String instanceId)
    {
        return getVersionNode(appId, version) + "/instance/" + instanceId;
    }

    /**
     * {@value #ROOT_NODE}/应用ID/版本号/instance/实例ID/FATAL
     */
    public static final String getFatalNode(String appId, String version, String instanceId)
    {
        return getVersionNode(appId, version) + "/instance/" + instanceId + "/FATAL";
    }

    /**
     * {@value #ROOT_NODE}/应用ID/版本号/instance/实例ID/HEARTBEAT
     */
    public static final String getHeartbeatNode(String appId, String version, String instanceId)
    {
        return getVersionNode(appId, version) + "/instance/" + instanceId + "/HEARTBEAT";
    }

    /**
     * {@value #ROOT_NODE}/应用ID/版本号/instance/实例ID/STARTING
     */
    public static final String getStartingNode(String appId, String version, String instanceId)
    {
        return getVersionNode(appId, version) + "/instance/" + instanceId + "/STARTING";
    }

    /**
     * {@value #ROOT_NODE}/应用ID/版本号/instance/实例ID/RUNNING
     */
    public static final String getRunningNode(String appId, String version, String instanceId)
    {
        return getVersionNode(appId, version) + "/instance/" + instanceId + "/RUNNING";
    }

    /**
     * {@value #ROOT_NODE}/应用ID/版本号/instance/实例ID/RUNNING
     */
    public static final String getStartedNode(String appId, String version, String instanceId)
    {
        return getVersionNode(appId, version) + "/instance/" + instanceId + "/STARTED";
    }

    /**
     * 解析出应用 ID
     * 
     * @param node 节点路径
     * @return 应用 ID
     */
    public static final String getAppId(String node)
    {
        return StringUtil.substringBeforeFirst(StringUtil.trimLeft(StringUtil.substringAfterLast(node, ROOT_NODE), '/') + "/", "/");
    }

    /**
     * 解析出版本号
     * 
     * @param node 节点路径
     * @return 版本号
     */
    public static final String getVersion(String node)
    {
        String appId = getAppId(node);
        String version = StringUtil.trimLeft(StringUtil.replaceAll(node, getAppNode(appId), ""), '/');
        return StringUtil.substringBeforeFirst(version, "/");
    }

    /**
     * 解析出实例 ID
     * 
     * @param node 节点路径
     * @return 实例 ID
     */
    public static final String getInstanceId(String node)
    {
        String appId = getAppId(node);
        String version = getVersion(node);
        String instanceId = StringUtil.trimLeft(StringUtil.replaceAll(node, getVersionNode(appId, version) + "/instance", ""), '/');
        return StringUtil.substringBeforeFirst(instanceId, "/");
    }
}
