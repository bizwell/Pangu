package com.joindata.inf.common.support.sso.properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * CAS 配置信息，从Disconf配置中心获取
 * 
 * @author <a href="mailto:zhangkai@joindata.com">Zhang Kai</a>
 * @since 2016年12月14日
 */
@Service
@Scope("singleton")
@DisconfFile(filename = "cas.properties")
public class CasProperties
{
    private String logoutFilterUrl;

    private String cookies;

    private String service;

    private String key;

    private String proxyCallbackUrl;

    private String filterProcessesUrl;

    private String defaultTargetUrl;

    private String casServerUrlPrefix;

    private String loginUrl;

    private String logoutUrl;

    @DisconfFileItem(name = "cas.client.logout.filter.url", associateField = "logoutFilterUrl")
    public String getLogoutFilterUrl()
    {
        return logoutFilterUrl;
    }

    public void setLogoutFilterUrl(String logoutFilterUrl)
    {
        this.logoutFilterUrl = logoutFilterUrl;
    }

    @DisconfFileItem(name = "cas.client.cookies", associateField = "cookies")
    public String getCookies()
    {
        return cookies;
    }

    public void setCookies(String cookies)
    {
        this.cookies = cookies;
    }

    @DisconfFileItem(name = "cas.client.service", associateField = "service")
    public String getService()
    {
        return service;
    }

    public void setService(String service)
    {
        this.service = service;
    }

    @DisconfFileItem(name = "cas.client.key", associateField = "key")
    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    @DisconfFileItem(name = "cas.client.proxyCallbackUrl", associateField = "proxyCallbackUrl")
    public String getProxyCallbackUrl()
    {
        return proxyCallbackUrl;
    }

    public void setProxyCallbackUrl(String proxyCallbackUrl)
    {
        this.proxyCallbackUrl = proxyCallbackUrl;
    }

    @DisconfFileItem(name = "cas.client.filterProcessesUrl", associateField = "filterProcessesUrl")
    public String getFilterProcessesUrl()
    {
        return filterProcessesUrl;
    }

    public void setFilterProcessesUrl(String filterProcessesUrl)
    {
        this.filterProcessesUrl = filterProcessesUrl;
    }

    @DisconfFileItem(name = "cas.client.defaultTargetUrl", associateField = "defaultTargetUrl")
    public String getDefaultTargetUrl()
    {
        return defaultTargetUrl;
    }

    public void setDefaultTargetUrl(String defaultTargetUrl)
    {
        this.defaultTargetUrl = defaultTargetUrl;
    }

    @DisconfFileItem(name = "cas.casServerUrlPrefix", associateField = "casServerUrlPrefix")
    public String getCasServerUrlPrefix()
    {
        return casServerUrlPrefix;
    }

    public void setCasServerUrlPrefix(String casServerUrlPrefix)
    {
        this.casServerUrlPrefix = casServerUrlPrefix;
    }

    @DisconfFileItem(name = "cas.login.url", associateField = "loginUrl")
    public String getLoginUrl()
    {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl)
    {
        this.loginUrl = loginUrl;
    }

    @DisconfFileItem(name = "cas.logout.url", associateField = "logoutUrl")
    public String getLogoutUrl()
    {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl)
    {
        this.logoutUrl = logoutUrl;
    }

}
