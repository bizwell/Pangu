package com.joindata.inf.boot.sterotype.handler;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;

import com.joindata.inf.common.basic.exceptions.GenericException;

/**
 * Web 请求拦截器(SpringMVC 版)
 * 
 * @author 宋翔
 * @date 2016-3-22 21:01:25
 */
public abstract class WebRequestInterceptor implements org.springframework.web.context.request.WebRequestInterceptor
{
    @Override
    public void preHandle(WebRequest request) throws Exception
    {
        // 执行自定义拦截处理
        beforeRequest(request);
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception
    {
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception
    {
        // 执行自定义拦截处理
        afterRequest(request, ex);
    }

    /**
     * 请求之前所做的工作
     * 
     * @param request 请求参数
     * @throws GenericException 遇到任何不允许后续再访问的情况，抛出异常
     */
    protected abstract void beforeRequest(WebRequest request) throws GenericException;

    /**
     * 请求之后所做的工作
     * 
     * @param request 请求参数
     * @param ex 请求后发生异常
     * @throws GenericException 遇到任何不允许后续再访问的情况，抛出异常
     */
    protected abstract void afterRequest(WebRequest request, Exception ex) throws GenericException;
}
