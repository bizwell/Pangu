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
        return String.class;
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
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.getAccessDecisionManager().decide(authentication, null, obtainSecurityMetadataSource().getAttributes(fi.getRequest().getRequestURI()));
        chain.doFilter(request, response);
    }

    @Override
    public void destroy()
    {
    }

}
