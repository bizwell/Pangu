package com.joindata.inf.common.support.sso;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.joindata.inf.common.basic.annotation.WebFilter;
import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.sso.bootconfig.SpringSecurityCasConfig;

/**
 * SSO 支持配置器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 19, 2016 3:38:12 PM
 */
@Configuration
@EnableDisconf
@Import(SpringSecurityCasConfig.class)
@WebFilter(DelegatingFilterProxy.class)
public class ConfigHub extends AbstractConfigHub
{
    @Override
    protected void check()
    {
    }
}