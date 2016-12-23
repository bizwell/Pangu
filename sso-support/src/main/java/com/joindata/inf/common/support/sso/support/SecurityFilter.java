package com.joindata.inf.common.support.sso.support;

import javax.servlet.annotation.WebFilter;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.DelegatingFilterProxy;

@WebFilter(filterName = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, urlPatterns = "/*")
public class SecurityFilter extends DelegatingFilterProxy
{

}