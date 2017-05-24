package com.joindata.inf.common.support.camunda.core.filter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;

@WebFilter(filterName = "RestEasy", urlPatterns = "/api/*", initParams = {@WebInitParam(name = "javax.ws.rs.Application", value = "com.joindata.inf.common.support.camunda.bootconfig.RestProcessEngineDeployment")})
public class CustomFilterDispatcher extends FilterDispatcher
{
}
