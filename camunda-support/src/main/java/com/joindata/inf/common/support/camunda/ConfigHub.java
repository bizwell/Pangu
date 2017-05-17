package com.joindata.inf.common.support.camunda;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import org.camunda.bpm.admin.Admin;
import org.camunda.bpm.admin.impl.DefaultAdminRuntimeDelegate;
import org.camunda.bpm.cockpit.Cockpit;
import org.camunda.bpm.cockpit.impl.DefaultCockpitRuntimeDelegate;
import org.camunda.bpm.tasklist.Tasklist;
import org.camunda.bpm.tasklist.impl.DefaultTasklistRuntimeDelegate;
import org.camunda.bpm.welcome.Welcome;
import org.camunda.bpm.welcome.impl.DefaultWelcomeRuntimeDelegate;
import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.joindata.inf.common.basic.annotation.FilterConfig;
import com.joindata.inf.common.basic.annotation.WebAppFilterItem;
import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.camunda.bootconfig.CamundaConfig;
import com.joindata.inf.common.support.camunda.bootconfig.CamundaServiceConfig;
import com.joindata.inf.common.support.camunda.bootconfig.ProcessEngineAware;
import com.joindata.inf.common.support.camunda.core.filter.CustomProcessEnginesFilter;
import com.joindata.inf.common.support.disconf.EnableDisconf;

/**
 * Camunda 支持配置器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 16, 2017 4:06:12 PM
 */
@Configuration
@EnableDisconf
@Import({CamundaConfig.class, CamundaServiceConfig.class, ProcessEngineAware.class})
@FilterConfig({@WebAppFilterItem(filter = CustomProcessEnginesFilter.class, config = @WebFilter(filterName = "ProcessEngines", urlPatterns = {"/app/*", "/lib/*", "/plugin/*"})), @WebAppFilterItem(filter = FilterDispatcher.class, config = @WebFilter(filterName = "RestEasy", urlPatterns = "/*", initParams = {@WebInitParam(name = "javax.ws.rs.Application", value = "com.joindata.inf.common.support.camunda.bootconfig.RestProcessEngineDeployment")}))})
public class ConfigHub extends AbstractConfigHub
{
    static
    {
        Cockpit.setCockpitRuntimeDelegate(new DefaultCockpitRuntimeDelegate());
        Admin.setAdminRuntimeDelegate(new DefaultAdminRuntimeDelegate());
        Tasklist.setTasklistRuntimeDelegate(new DefaultTasklistRuntimeDelegate());
        Welcome.setRuntimeDelegate(new DefaultWelcomeRuntimeDelegate());
    }

    @Override
    protected void check()
    {
    }
}