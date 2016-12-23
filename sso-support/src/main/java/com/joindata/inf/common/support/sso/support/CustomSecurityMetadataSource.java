package com.joindata.inf.common.support.sso.support;

import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;

import com.joindata.inf.common.support.sso.stereotype.GrantQueryService;

/**
 * 权限数据源
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 21, 2016 3:15:46 PM
 */
public class CustomSecurityMetadataSource implements SecurityMetadataSource
{
    private GrantQueryService grantQueryService;

    public CustomSecurityMetadataSource(GrantQueryService grantQueryService)
    {
        this.grantQueryService = grantQueryService;
    }

    /**
     * 获取资源权限列表
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object path) throws IllegalArgumentException
    {
        return grantQueryService.getConfigAttributes((String)path);
    }

    /**
     * 用不到
     * 
     * @return null
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes()
    {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz)
    {
        return String.class.equals(clazz);
    }

}
