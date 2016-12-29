package com.joindata.inf.common.support.sso;

import javax.servlet.annotation.WebFilter;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.joindata.inf.common.basic.annotation.FilterConfig;
import com.joindata.inf.common.basic.annotation.WebAppFilterItem;
import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.redis.EnableRedis;
import com.joindata.inf.common.support.session.EnableSession;
import com.joindata.inf.common.support.sso.bootconfig.SpringSecurityConfig;

/**
 * SSO 支持配置器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 19, 2016 3:38:12 PM
 */
@Configuration
@Import(SpringSecurityConfig.class)
@FilterConfig({@WebAppFilterItem(filter = DelegatingFilterProxy.class, config = @WebFilter(filterName = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, urlPatterns = "/*"))})
@EnableDisconf
@EnableRedis
@EnableSession
public class ConfigHub extends AbstractConfigHub
{
    @Override
    protected void check()
    {
    }
}