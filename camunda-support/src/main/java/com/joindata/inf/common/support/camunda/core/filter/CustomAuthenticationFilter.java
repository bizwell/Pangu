package com.joindata.inf.common.support.camunda.core.filter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;

@WebFilter(filterName = "camunda-auth", urlPatterns = "/app/*", initParams = @WebInitParam(name = "authentication-provider", value = "org.camunda.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider"))
public class CustomAuthenticationFilter extends ProcessEngineAuthenticationFilter
{
}