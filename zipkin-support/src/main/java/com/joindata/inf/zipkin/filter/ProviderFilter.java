package com.joindata.inf.zipkin.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.alibaba.dubbo.rpc.*;
import com.joindata.inf.common.basic.cst.RequestLogCst;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.zipkin.TraceContext;
import com.joindata.inf.zipkin.agent.TraceAgent;
import com.joindata.inf.zipkin.cst.TraceConstants;
import com.joindata.inf.zipkin.properties.ZipkinProperties;
import org.slf4j.MDC;

import java.util.Map;

/**
 * Created by Rayee on 2017/10/23.
 */
@Activate(group = {Constants.PROVIDER})
public class ProviderFilter implements Filter {

    private TraceAgent agent = new TraceAgent(ServiceBean.getSpringContext().getBean(ZipkinProperties.class).getServer());

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Map<String, String> attaches = invocation.getAttachments();
        if (!attaches.containsKey(TraceConstants.TRACE_ID)) {
            return invoker.invoke(invocation);
        }
        startTrace(attaches);
        Result result = invoker.invoke(invocation);
        result.getAttachments().put(TraceConstants.APP_ID, BootInfoHolder.getAppId());
        endTrace();
        return result;
    }

    private void startTrace(Map<String, String> attaches) {
        TraceContext.start();
        TraceContext.setTraceId(Long.parseLong(attaches.get(TraceConstants.TRACE_ID)));
        TraceContext.setSpanId(Long.parseLong(attaches.get(TraceConstants.SPAN_ID)));
        MDC.put(RequestLogCst.REQUEST_ID, Long.toHexString(Long.parseLong(attaches.get(TraceConstants.TRACE_ID))));
    }

    private void endTrace() {
        agent.send(TraceContext.getSpans());
        TraceContext.clear();
        MDC.clear();
    }
}
