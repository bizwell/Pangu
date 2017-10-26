package com.joindata.inf.zipkin.filter;

import com.alibaba.dubbo.rpc.*;
import com.joindata.inf.zipkin.TraceContext;
import com.joindata.inf.zipkin.agent.TraceAgent;
import com.joindata.inf.zipkin.cst.TraceConstants;
import com.joindata.inf.zipkin.properties.ZipkinProperties;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by Rayee on 2017/10/23.
 */
public class ProviderFilter implements Filter {

//    @Resource
//    private ZipkinProperties zipkinProperties;

//    private TraceAgent agent = new TraceAgent(zipkinProperties.getServer());
    private TraceAgent agent = new TraceAgent("http://172.168.168.153:9411");

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

//        if (zipkinProperties.getEnable() != 1) {
//            return invoker.invoke(invocation);
//        }
        Map<String, String> attaches = invocation.getAttachments();

        if (!attaches.containsKey(TraceConstants.TRACE_ID)) {
            return invoker.invoke(invocation);
        }
        startTrace(attaches);

        Result result = invoker.invoke(invocation);

        endTrace();

        return result;
    }

    private void startTrace(Map<String, String> attaches) {
        TraceContext.start();
        TraceContext.setTraceId(Long.parseLong(attaches.get(TraceConstants.TRACE_ID)));
        TraceContext.setSpanId(Long.parseLong(attaches.get(TraceConstants.SPAN_ID)));
    }

    private void endTrace() {
        agent.send(TraceContext.getSpans());
        TraceContext.clear();
    }

}
