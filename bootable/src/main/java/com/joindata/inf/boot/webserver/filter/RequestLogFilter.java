package com.joindata.inf.boot.webserver.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.joindata.inf.common.basic.annotation.FilterComponent;
import com.joindata.inf.common.basic.cst.RequestLogCst;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.common.util.tools.UuidUtil;

/**
 * 用于拦截所有web请求， 生成唯一的请求id
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年8月7日
 */
@Component
@FilterComponent(path = "/*")
public class RequestLogFilter implements Filter
{
    private static final Logger log = Logger.get();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        log.info("Filter 初始化了...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        String reqId = MDC.get(RequestLogCst.REQUEST_ID);
        if(StringUtils.isEmpty(reqId))
        {
            reqId = UuidUtil.makeNoSlash();
            MDC.put(RequestLogCst.REQUEST_ID, reqId);
        }

        chain.doFilter(request, response);
        MDC.clear();
    }

    @Override
    public void destroy()
    {
        log.info("Filter 挂了...");
    }

}
