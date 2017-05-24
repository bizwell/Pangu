package com.joindata.inf.common.support.camunda.bootconfig;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.apache.zookeeper.server.auth.AuthenticationProvider;
import org.camunda.bpm.engine.rest.impl.CamundaRestResources;

import com.joindata.inf.common.support.camunda.core.CustomNamedProcessEngineRestServiceImpl;
import com.joindata.inf.common.support.camunda.core.CustomProcessEngineRestServiceImpl;

public class RestProcessEngineDeployment extends Application
{
    @Override
    public Set<Class<?>> getClasses()
    {
        Set<Class<?>> classes = new HashSet<Class<?>>();

        // 自定义的 REST 接口
        classes.add(CustomProcessEngineRestServiceImpl.class);
        classes.add(CustomNamedProcessEngineRestServiceImpl.class);

        // 原封不动
        classes.addAll(CamundaRestResources.getConfigurationClasses());
        classes.add(AuthenticationProvider.class);

        return classes;
    }

}