package com.joindata.inf.zipkin.util;

import com.google.common.base.Stopwatch;
import com.joindata.inf.common.basic.cst.RequestLogCst;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.sterotype.mq.MessageListener;
import com.joindata.inf.zipkin.TraceContext;
import com.joindata.inf.zipkin.agent.TraceAgent;
import com.joindata.inf.zipkin.cst.TraceConstants;
import com.twitter.zipkin.gen.Annotation;
import com.twitter.zipkin.gen.BinaryAnnotation;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rayee on 2017/12/4.
 */
@SuppressWarnings("deprecation")
public class TraceUtils {

    public static Span startTrace(int port) {
        String apiName;
        if (port == 0) {
            //非web request
            apiName = BootInfoHolder.getAppId().concat("-listener");
        } else {
            //web request
            apiName = BootInfoHolder.getAppId().concat(" : ").concat(String.valueOf(port));
        }
        Span apiSpan = new Span();
        //调用链初始化信息
        long id = Ids.get();
        apiSpan.setId(id);
        apiSpan.setTrace_id(id);
        apiSpan.setName(apiName);
        long timestamp = Times.currentMicros();
        apiSpan.setTimestamp(timestamp);
        apiSpan.addToAnnotations(Annotation.create(timestamp, TraceConstants.ANNO_SR, Endpoint.create(apiName, ServerInfo.IP4, port)));
        apiSpan.addToBinary_annotations(BinaryAnnotation.create("name", BootInfoHolder.getAppId(), null));
        //日志显示traceId信息
        MDC.clear();
        MDC.put(RequestLogCst.REQUEST_ID, Long.toHexString(id));

        TraceContext.start();
        TraceContext.setTraceId(id);
        TraceContext.setSpanId(id);
        TraceContext.addSpan(apiSpan);

        return apiSpan;
    }

    public static Span startTrace() {
        String apiName = BootInfoHolder.getAppId().concat("-listener");
        Span apiSpan = new Span();
        //调用链初始化信息
        long id = Ids.get();
        apiSpan.setId(id);
        apiSpan.setTrace_id(id);
        apiSpan.setName(apiName);
        long timestamp = Times.currentMicros();
        apiSpan.setTimestamp(timestamp);
        apiSpan.addToAnnotations(Annotation.create(timestamp, TraceConstants.ANNO_SR, Endpoint.create(apiName, ServerInfo.IP4, 0)));
        apiSpan.addToBinary_annotations(BinaryAnnotation.create("name", BootInfoHolder.getAppId(), null));
        //日志显示traceId信息
        MDC.clear();
        MDC.put(RequestLogCst.REQUEST_ID, Long.toHexString(id));

        TraceContext.start();
        TraceContext.setTraceId(id);
        TraceContext.setSpanId(id);
        TraceContext.addSpan(apiSpan);

        return apiSpan;
    }

    public static void endTrace(int port, Span span, Stopwatch stopwatch, TraceAgent agent) {
        span.addToAnnotations(Annotation.create(Times.currentMicros(), TraceConstants.ANNO_SS, Endpoint.create(span.getName(), ServerInfo.IP4, port)));
        span.setDuration(stopwatch.stop().elapsed(TimeUnit.MICROSECONDS));
        agent.send(TraceContext.getSpans());
        TraceContext.clear();
        MDC.clear();
    }

}
