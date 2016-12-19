package com.joindata.inf.common.support.sso.stereotype;

import javax.servlet.Filter;

import org.springframework.security.access.intercept.AbstractSecurityInterceptor;

/**
 * 过滤器模板
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 19, 2016 5:50:33 PM
 */
public abstract class SsoInterceptor extends AbstractSecurityInterceptor implements Filter
{

}
