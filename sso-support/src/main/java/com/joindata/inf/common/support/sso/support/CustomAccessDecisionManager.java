package com.joindata.inf.common.support.sso.support;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.joindata.inf.common.support.sso.entity.AuthInfo;
import com.joindata.inf.common.util.basic.CollectionUtil;

/**
 * 权限决策管理器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 22, 2016 12:11:18 PM
 */
public class CustomAccessDecisionManager implements AccessDecisionManager
{
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException
    {
        if(configAttributes == null)
        {
            throw new AccessDeniedException("没有访问权限");
        }

        AuthInfo user = (AuthInfo)((CasAuthenticationToken)authentication).getUserDetails();

        if(CollectionUtil.hasIntersection(configAttributes, user.getRoleList()))
        {
            return;
        }

        throw new AccessDeniedException("没有访问权限");
    }

    @Override
    public boolean supports(ConfigAttribute attribute)
    {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz)
    {
        return AuthInfo.class.isAssignableFrom(clazz);
    }

}
