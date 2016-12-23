package com.joindata.inf.common.support.sso.support;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;

import com.joindata.inf.common.support.sso.entity.AuthInfo;

/**
 * 权限拦截器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 21, 2016 3:22:18 PM
 */
public class CustomSecurityInterceptor extends AbstractSecurityInterceptor implements Filter
{
    private SecurityMetadataSource metadataSource;

    public CustomSecurityInterceptor(SecurityMetadataSource securityMetadataSource, AccessDecisionManager accessDecisionManager)
    {
        this.metadataSource = securityMetadataSource;
        super.setAccessDecisionManager(accessDecisionManager);
    }

    @Override
    public Class<?> getSecureObjectClass()
    {
        return AuthInfo.class.asSubclass(AuthInfo.class);
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource()
    {
        return metadataSource;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        FilterInvocation fi = new FilterInvocation(request, response, chain);

        this.getAccessDecisionManager().decide(authentication, null, obtainSecurityMetadataSource().getAttributes(fi.getRequest().getRequestURI()));
    }

    @Override
    public void destroy()
    {
    }

}
