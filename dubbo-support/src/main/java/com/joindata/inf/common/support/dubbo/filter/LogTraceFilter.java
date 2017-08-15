package com.joindata.inf.common.support.dubbo.filter;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.joindata.inf.common.basic.cst.RequestLogCst;
import com.joindata.inf.common.util.tools.UuidUtil;

/**
 * dubbo 分布式服务调用， 提供唯一的请求id进行日志追溯
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年8月7日
 */
public class LogTraceFilter implements Filter
{
    public static final String FILTER_NAME = "logTraceFilter";

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException
    {
        Map<String, String> attachment = invocation.getAttachments();
        try
        {
            String requestIdKey = RequestLogCst.REQUEST_ID;
            String reqId = attachment.get(requestIdKey);
            if(!StringUtils.isEmpty(reqId))
            {
                MDC.put(requestIdKey, reqId);
            }
            else
            {
                reqId = MDC.get(requestIdKey);
                if(StringUtils.isEmpty(reqId))
                {
                    reqId = UuidUtil.makeNoSlash();
                    MDC.put(requestIdKey, reqId);
                }

                attachment.put(requestIdKey, reqId);
            }
        }
        catch(RpcException e)
        {
            throw e;
        }

        catch(Exception e)
        {
            throw new RpcException(e.getMessage(), e);
        }
        return invoker.invoke(invocation);
    }
}
