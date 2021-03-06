package com.joindata.inf.common.support.dubbo.properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

@Service
@Scope("singleton")
@DisconfFile(filename = "dubbo.properties")
public class DubboProperties
{
    /** 维护人 */
    private String appOwner;

    /** 机构 */
    private String appOrg;

    /** 注册中心地址 */
    private String registryAddress;

    /** 注册中心连接超时时间 */
    private int registryTimeout;

    /** 服务默认提供的超时时间 */
    private int providerTimeout;

    /** 服务默认提供的重试次数 */
    private int providerRetries;

    /** 服务默认提供的的负载均衡策略 */
    private String providerLoadbalance;

    /** 服务默认的拦截扩展 */
    private String providerFilter;

    /** 线程池数量 */
    private int dubboThreads;

    /** 序列化方式 */
    private String dubboSerialization;

    /** 主机前缀（如果在内网中，用来过滤网卡地址很有用） */
    private String dubboHostPrefix;

    /** 远程调用超时时间 */
    private int consumerTimeout;

    /** 远程调用重试次数 */
    private int consumerRetries;

    /** 远程调用负载均衡方式 */
    private String consumerLoadbalance;

    /** 远程调用默认的调用拦截扩展 */
    private String consumerFilter;

    /** 默认是否检测服务是否存在 */
    private boolean consumerCheck;

    /**
     * 服务端口号
     * 
     * @return
     */
    private Integer port;

    @DisconfFileItem(name = "port", associateField = "port")
    public Integer getPort()
    {
        return port;
    }

    public void setPort(Integer port)
    {
        this.port = port;
    }

    @DisconfFileItem(name = "app.owner", associateField = "appOwner")
    public String getAppOwner()
    {
        return appOwner;
    }

    public void setAppOwner(String appOwner)
    {
        this.appOwner = appOwner;
    }

    @DisconfFileItem(name = "app.org", associateField = "appOrg")
    public String getAppOrg()
    {
        return appOrg;
    }

    public void setAppOrg(String appOrg)
    {
        this.appOrg = appOrg;
    }

    @DisconfFileItem(name = "registry.address", associateField = "registryAddress")
    public String getRegistryAddress()
    {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress)
    {
        this.registryAddress = registryAddress;
    }

    @DisconfFileItem(name = "registry.timeout", associateField = "registryTimeout")
    public int getRegistryTimeout()
    {
        return registryTimeout;
    }

    public void setRegistryTimeout(int registryTimeout)
    {
        this.registryTimeout = registryTimeout;
    }

    @DisconfFileItem(name = "provider.timeout", associateField = "providerTimeout")
    public int getProviderTimeout()
    {
        return providerTimeout;
    }

    public void setProviderTimeout(int providerTimeout)
    {
        this.providerTimeout = providerTimeout;
    }

    @DisconfFileItem(name = "provider.retries", associateField = "providerRetries")
    public int getProviderRetries()
    {
        return providerRetries;
    }

    public void setProviderRetries(int providerRetries)
    {
        this.providerRetries = providerRetries;
    }

    @DisconfFileItem(name = "provider.loadbalance", associateField = "providerLoadbalance")
    public String getProviderLoadbalance()
    {
        return providerLoadbalance;
    }

    public void setProviderLoadbalance(String providerLoadbalance)
    {
        this.providerLoadbalance = providerLoadbalance;
    }

    @DisconfFileItem(name = "provider.filter", associateField = "providerFilter")
    public String getProviderFilter()
    {
        return providerFilter;
    }

    public void setProviderFilter(String providerFilter)
    {
        this.providerFilter = providerFilter;
    }

    @DisconfFileItem(name = "dubbo.threads", associateField = "dubboThreads")
    public int getDubboThreads()
    {
        return dubboThreads;
    }

    public void setDubboThreads(int dubboThreads)
    {
        this.dubboThreads = dubboThreads;
    }

    @DisconfFileItem(name = "dubbo.serialization", associateField = "dubboSerialization")
    public String getDubboSerialization()
    {
        return dubboSerialization;
    }

    public void setDubboSerialization(String dubboSerialization)
    {
        this.dubboSerialization = dubboSerialization;
    }

    @DisconfFileItem(name = "dubbo.hostPrefix", associateField = "dubboHostPrefix")
    public String getDubboHostPrefix()
    {
        return dubboHostPrefix;
    }

    public void setDubboHostPrefix(String dubboHostPrefix)
    {
        this.dubboHostPrefix = dubboHostPrefix;
    }

    @DisconfFileItem(name = "consumer.timeout", associateField = "consumerTimeout")
    public int getConsumerTimeout()
    {
        return consumerTimeout;
    }

    public void setConsumerTimeout(int consumerTimeout)
    {
        this.consumerTimeout = consumerTimeout;
    }

    @DisconfFileItem(name = "consumer.retries", associateField = "consumerRetries")
    public int getConsumerRetries()
    {
        return consumerRetries;
    }

    public void setConsumerRetries(int consumerRetries)
    {
        this.consumerRetries = consumerRetries;
    }

    @DisconfFileItem(name = "consumer.loadbalance", associateField = "consumerLoadbalance")
    public String getConsumerLoadbalance()
    {
        return consumerLoadbalance;
    }

    public void setConsumerLoadbalance(String consumerLoadbalance)
    {
        this.consumerLoadbalance = consumerLoadbalance;
    }

    @DisconfFileItem(name = "consumer.filter", associateField = "consumerFilter")
    public String getConsumerFilter()
    {
        return consumerFilter;
    }

    public void setConsumerFilter(String consumerFilter)
    {
        this.consumerFilter = consumerFilter;
    }

    @DisconfFileItem(name = "consumer.check", associateField = "consumerCheck")
    public boolean isConsumerCheck()
    {
        return consumerCheck;
    }

    public void setConsumerCheck(boolean consumerCheck)
    {
        this.consumerCheck = consumerCheck;
    }

}