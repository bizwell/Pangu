package com.joindata.inf.registry.util;

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
     * {@value #ROOT_NODE}/应用ID/版本号/instance/实例ID/RUNNING
     */
    public static final String getStartedNode(String appId, String version, String instanceId)
    {
        return getVersionNode(appId, version) + "/instance/" + instanceId + "/STARTED";
    }

}
